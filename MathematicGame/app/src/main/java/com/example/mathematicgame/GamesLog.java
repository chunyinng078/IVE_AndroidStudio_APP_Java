package com.example.mathematicgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GamesLog extends AppCompatActivity {

    //variables
    public ListView lv;
    SQLiteDatabase db;
    Cursor cursor = null;
    String[] columns = { "gameID", "playDate", "playTime", "duration" , "correctCount" , "wrongCount" };


    //use for order by button
    int tmp =1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_log);

        //find view by id things
        lv=findViewById(R.id.lv);

        try {
            // Create a database if it does not exist, to avoid player cleared the data in menu (Clear data function)
            String sql;

            //open the database
            db = SQLiteDatabase.openDatabase("/data/data/com.example.mathematicgame/eBidDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);

            // Create a database if it does not exist, to avoid player cleared the data in menu (Clear data function)
            sql = "CREATE TABLE  IF NOT EXISTS QuestionsLog ('questionID' INTEGER  PRIMARY KEY AUTOINCREMENT, 'question' text, 'answer' text, 'yourAnswer' text);";

            //run the sql above
            db.execSQL(sql);

            // Create a database if it does not exist, to avoid player cleared the data in menu (Clear data function)
            sql = "CREATE TABLE IF NOT EXISTS GamesLog ('gameID' INTEGER  PRIMARY KEY AUTOINCREMENT, 'playDate' text, 'playTime' text, 'duration' INTEGER ,'correctCount' INTEGER , 'wrongCount' INTEGER );";

            //run the sql above
            db.execSQL(sql);

            //close connection to database
            db.close();


        } catch (SQLiteException e) {
            //show error message
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        try{

            //connect database
            db = SQLiteDatabase.openDatabase("/data/data/com.example.mathematicgame/eBidDB", null, SQLiteDatabase.OPEN_READONLY);

            //query on GamesLog table
            cursor = db.query("GamesLog",columns,null,null,null,null,null);

            //create a string array list to store db data
            List<String> record = new ArrayList<String>();
            while (cursor.moveToNext()) {

                //get data from database
                int gameID = cursor.getInt(cursor.getColumnIndex("gameID"));
                String playDate = cursor.getString(cursor.getColumnIndex("playDate"));
                String playTime = cursor.getString(cursor.getColumnIndex("playTime"));
                String duration = cursor.getString(cursor.getColumnIndex("duration"));
                String correctCount = cursor.getString(cursor.getColumnIndex("correctCount"));
                String wrongCount = cursor.getString(cursor.getColumnIndex("wrongCount"));



                //add string to string list array
                record.add(String.format("%10s\n %-10s %15s\n %15s %15s %15s", "Game ID: "+gameID, "\nPlayed on  "+playDate, "  at  "+playTime,
                        "\nGame duration: "+duration+"s" , "Correct: "+correctCount , "Wrong: "+wrongCount));;

            }

            //close connection to database
            db.close();

            //store array list things in to array
            String[] record_ = record.toArray(new String[0]);

            //set array adapter
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, record_);
            lv.setAdapter(arrayAdapter);

        }catch (SQLiteException e){
            //nothing to do, not affect app usage

        }


    }

    //button to go back to menu Activity
    public void btnBack (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    //button to go back to pie chart Activity
    public void btnChart (View view){


        Intent intent = new Intent(this, GamesLogChart.class);
        startActivity(intent);

    }

    //order list view things
    public void btnFilter (View view){
        lv=findViewById(R.id.lv);
        tmp++;

        //tmp will +1 on each click, and %2 will find out to order by asc or desc on each click
        if(tmp%2==0){
            Toast.makeText(this, "Descending Order",Toast.LENGTH_SHORT).show();
            try{

                //same database work will order by question id desc, nearly same with onCreate database section

                String sortOrder= "gameID Desc";

                //connect database
                db = SQLiteDatabase.openDatabase("/data/data/com.example.mathematicgame/eBidDB", null, SQLiteDatabase.OPEN_READONLY);

                // order by question id desc,
                cursor = db.query("GamesLog",columns,null,null,null,null,sortOrder);

                int i =0;

                //create a string array list to store db data
                List<String> record = new ArrayList<String>();
                while (cursor.moveToNext()) {

                    //get data from database
                    int gameID = cursor.getInt(cursor.getColumnIndex("gameID"));
                    String playDate = cursor.getString(cursor.getColumnIndex("playDate"));
                    String playTime = cursor.getString(cursor.getColumnIndex("playTime"));
                    String duration = cursor.getString(cursor.getColumnIndex("duration"));
                    String correctCount = cursor.getString(cursor.getColumnIndex("correctCount"));
                    String wrongCount = cursor.getString(cursor.getColumnIndex("wrongCount"));

                    record.add(String.format("%10s\n %-10s %15s\n %15s %15s %15s", "Game ID: "+gameID, "\nPlayed on  "+playDate, "  at  "+playTime,
                            "\nGame duration: "+duration+"s" , "Correct: "+correctCount , "Wrong: "+wrongCount));;

                }

                String[] record_ = record.toArray(new String[0]);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, record_);
                lv.setAdapter(arrayAdapter);

                db.close();
            }catch (Exception e){
                //nothing to do, not affect app usage
            }
        }else{
            Toast.makeText(this, "Ascending Order",Toast.LENGTH_SHORT).show();

            //same database work will order by question id asc, nearly same with onCreate database section
            try{
                String sortOrder= "gameID Asc";

                //connect database
                db = SQLiteDatabase.openDatabase("/data/data/com.example.mathematicgame/eBidDB", null, SQLiteDatabase.OPEN_READONLY);

                // order by question id asc,
                cursor = db.query("GamesLog",columns,null,null,null,null,sortOrder);

                int i =0;

                //create a string array list to store db data
                List<String> record = new ArrayList<String>();
                while (cursor.moveToNext()) {

                    //get data from database
                    int gameID = cursor.getInt(cursor.getColumnIndex("gameID"));
                    String playDate = cursor.getString(cursor.getColumnIndex("playDate"));
                    String playTime = cursor.getString(cursor.getColumnIndex("playTime"));
                    String duration = cursor.getString(cursor.getColumnIndex("duration"));
                    String correctCount = cursor.getString(cursor.getColumnIndex("correctCount"));
                    String wrongCount = cursor.getString(cursor.getColumnIndex("wrongCount"));



                    record.add(String.format("%10s\n %-10s %15s\n %15s %15s %15s", "Game ID: "+gameID, "\nPlayed on  "+playDate, "  at  "+playTime,
                            "\nGame duration: "+duration+"s" , "Correct: "+correctCount , "Wrong: "+wrongCount));;

                }

                String[] record_ = record.toArray(new String[0]);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, record_);
                lv.setAdapter(arrayAdapter);
                db.close();
            }catch (Exception e){
                //nothing to do, not affect app usage
            }
        }

    }



}