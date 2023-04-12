package com.example.mathematicgame;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class Game extends AppCompatActivity {

    //declare variable
    //layout
    private EditText et;
    private TextView tv;
    private TextView tvCheck;
    private Button btnQuit;
    private Button btnContinue;
    private Button btnSubmit;
    private Button btnClear;
    private Button btnPeek;

    //function stuff
    SQLiteDatabase db;
    String sql;
    Cursor cursor = null;
    getQuestion task = null;

    //variables
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    public String ans;          //store answer
    public String question;     //store question
    final String QUrl = "https://2vtyxazuaa.execute-api.us-east-1.amazonaws.com/default/ITP4501AssignmentAPI"; //get json from here
    private String nowTime;     //get now time
    private String nowDate;     //get now date
    private int rightCount=0;   //store how many question right
    private int wrongCount=0;   //store how many question wrong
    public int playCount=0;     //store how many question answered
    public long startTime=0;    //declared start time, use to calculate duration

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);
        //find view by id stuff
        tv=findViewById(R.id.tv);
        et=findViewById(R.id.et);
        tvCheck=findViewById(R.id.tvCheck);
        btnQuit=findViewById(R.id.btnQuit);
        btnContinue=findViewById(R.id.btnContinue);
        btnSubmit=findViewById(R.id.btnSubmit);
        btnClear=findViewById(R.id.btnClear);
        btnPeek=findViewById(R.id.btnPeek);

        //set quit and continue INVISIBLE at start of game,UI control
        btnQuit.setVisibility(View.INVISIBLE);
        btnContinue.setVisibility(View.INVISIBLE);

        try {
            //database stuff, create database and table if not exist
            // Create a database if it does not exist, prevent player drop all table in the menu (clear data function)
            String sql;

            db = SQLiteDatabase.openDatabase("/data/data/com.example.mathematicgame/eBidDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);

            // Create a table if it does not exist
            sql = "CREATE TABLE  IF NOT EXISTS QuestionsLog ('questionID' INTEGER  PRIMARY KEY AUTOINCREMENT, 'question' text, 'answer' text, 'yourAnswer' text);";

            //run the sql above
            db.execSQL(sql);

            // Create a table if it does not exist
            sql = "CREATE TABLE IF NOT EXISTS GamesLog ('gameID' INTEGER  PRIMARY KEY AUTOINCREMENT, 'playDate' text, 'playTime' text, 'duration' INTEGER ,'correctCount' INTEGER , 'wrongCount' INTEGER );";

            //run the sql above
            db.execSQL(sql);

            //close connection to database
            db.close();

        } catch (SQLiteException e) {
            //catch error
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //find now date and time
        SimpleDateFormat time = new SimpleDateFormat("dd/MM/yyyy");
        this.nowDate = time.format(new Date());

        SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");
        this.nowTime = date.format(new Date());

        //find game start time (for duration)
        startTime= System.currentTimeMillis();

        //run the get question task
        task = new getQuestion();
        task.execute(QUrl);


    }




    //get json online
    private class getQuestion extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... values) {

            //set question text to "loading" when downloading question from the api
            tv.setText("Loading Question...");

            //store how questions has been answered
            playCount++;

            //start get json from api
            InputStream inputStream = null;
            et.setText("");
            String result = "";
            URL url = null;

            try {
                url = new URL(values[0]);
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");        //using get method to get json
                con.connect();                      //connect to website
                inputStream = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;             //get every line in the json text

                }

                Log.d("doInBackGround", "get data complete");   //log the get data complete
                inputStream.close();


            } catch (Exception e) {
                result = e.getMessage();        //get error message

            }
            Log.d("doInBackGround", "Before result");  //logging the get data complete

            return result;  //return the json text for further use


        }

        protected void onPostExecute(String result) {       //after get json
            Log.d("doInBackGround", "After result");

            try {
                JSONObject jObj = new JSONObject(result);
                String question = jObj.getString("question");       //get question from json

                ans = jObj.getString("answer");         //get answer from json
                setAns(ans);                     //set the ans
                setQuestion(question);           //set the question
                tv.setText("\uD83D\uDC69\u200D\uD83C\uDFEB \n What is the answer of \n"+question+" ❓"); //ask player the question
            } catch (JSONException e) {
                e.printStackTrace();        //get error message
            }




        }


    }
    public void btnClear(View view){
        et.setText("");
    }   //clear the input

    public void btnPeek(View view){     //let player to peek the answer
        if (ans==""||ans==null){
            btnPeek.setText("PEEK ANSWER");
        }else{
            btnPeek.setText(ans);
        }


    }
    public void btnQuit(View view){ //quit the game


        //play the end game sound effect
        final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.end);
        mediaPlayer.start();

        long finishTime= System.currentTimeMillis();    //get finish time
        int elapsedTime=(int)(finishTime-startTime)/1000;   //get duration

        Intent intent = new Intent(this, timeSpent.class);  //create intent
        intent.putExtra("playTime",Integer.toString(elapsedTime));  //pass play time to next activity
        intent.putExtra("playCount",Integer.toString(playCount));

        startActivity(intent);      //start next activity


        //insert gamelog data to table
        db = SQLiteDatabase.openDatabase("/data/data/com.example.mathematicgame/eBidDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.execSQL("INSERT INTO GamesLog(playDate, playTime, duration,correctCount,wrongCount) values ('"+nowDate+"','"+nowTime+"','"+Integer.toString(elapsedTime)+"','"+rightCount+"','"+wrongCount+"'); ");
        db.close();



    }
    public void btnContinue(View view){     //player choose to continue

        tvCheck.setText("");
        //hide those button,UI control
        btnQuit.setVisibility(View.INVISIBLE);
        btnContinue.setVisibility(View.INVISIBLE);

        //show those button,UI control
        et.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.VISIBLE);
        btnClear.setVisibility(View.VISIBLE);
        btnPeek.setVisibility(View.VISIBLE);
        btnPeek.setText("PEEK ANSWER");

        //get next question
        task = new getQuestion();
        task.execute(QUrl);


    }


    public void btnSubmit(View view){   //player submit the answer

        //hide keyboard, UI control
        dismissKeyboard(this);

        //do some input checking on player input
    if(et.getText().toString().matches("")){    //reject empty answer
        Toast.makeText(this,"Your input is empty, please enter the answer again",Toast.LENGTH_LONG).show();
    }else if(et.getText().toString().length()>3){       //reject number >999
        Toast.makeText(this,"Your input must is integer in 3 number, please enter the answer again",Toast.LENGTH_LONG).show();
    }else if(!isNumeric(et.getText().toString())){      //reject any non number string
        Toast.makeText(this,"Your input is not a number, please enter the answer with number ",Toast.LENGTH_LONG).show();
    }else if(!isInt(et.getText().toString())){          //reject not integer number
        Toast.makeText(this,"Your input is not a integer number, please enter the answer with integer number ",Toast.LENGTH_LONG).show();
    }else {


        //hide input box,UI control
        et.setVisibility(View.INVISIBLE);

        //get user input
        String AA = et.getText().toString();

        //start database work, insert data in question log
        db = SQLiteDatabase.openDatabase("/data/data/com.example.mathematicgame/eBidDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.execSQL("INSERT INTO QuestionsLog(question, answer, yourAnswer) values ('"+question+"','"+ans+"','"+AA+"'); ");
        db.close();

        //things to do if player's answer is right
        if (ans.equals(et.getText().toString())){

            //play correct sound effect
            final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.correct);
            mediaPlayer.start();

            //right question count +1
            rightCount++;
            //show the text to user
            tv.setVisibility(View.VISIBLE);
            tv.setText("✅✅✅\nGOOD JOB!\n\uD83D\uDC4D\uD83D\uDC4D\uD83D\uDC4D");
            tvCheck.setText(" Your Answer Is Right!\n You want to\n Continue or Quit❓");

            //UI control
            btnQuit.setVisibility(View.VISIBLE);
            btnContinue.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.INVISIBLE);
            btnClear.setVisibility(View.INVISIBLE);
            btnPeek.setVisibility(View.INVISIBLE);


        }else{         //things to do if player's answer is wrong

            //play wrong sound effect
            final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.wrong);
            mediaPlayer.start();

            //wrong question count +1
            wrongCount++;

            tv.setVisibility(View.VISIBLE);
            tv.setText("❌❌❌\nYour answer "+et.getText().toString()+"\nis wrong\uD83D\uDE2D\n");
            tvCheck.setText("The right Answer "+ ans+"\nYou want to\n Continue or Quit❓");

            //UI control
            btnQuit.setVisibility(View.VISIBLE);
            btnContinue.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.INVISIBLE);
            btnClear.setVisibility(View.INVISIBLE);
            btnPeek.setVisibility(View.INVISIBLE);

        }
    }

    }

    //set method, set answer
    public void setAns(String ans){
        this.ans=ans;

    }

    //set method, set question
    public void setQuestion(String question){
        this.question=question;

    }

    //set check string is number or not
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    //set check string is integer number or not
    public static boolean isInt(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void dismissKeyboard(Activity activity) {        //hide keyboard function
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
}