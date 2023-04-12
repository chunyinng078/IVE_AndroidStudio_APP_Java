package com.example.mathematicgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class QuestionsLog extends AppCompatActivity {

    //variables
    SQLiteDatabase db;
    public Button btnMenu;
    public Button btnDrop;
    public ListView lvList;

    Cursor cursor = null;

    //columns in the question log table
    String[] columns = { "questionID", "question", "answer", "yourAnswer" };

    //use for order by button
    int tmp =1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_log);

        //find view by id things
        btnMenu=findViewById(R.id.btnMenu);
        btnDrop=findViewById(R.id.btnDrop);
        lvList=findViewById(R.id.lvList);


        try{

            //connect database
            db = SQLiteDatabase.openDatabase("/data/data/com.example.mathematicgame/eBidDB", null, SQLiteDatabase.OPEN_READONLY);

            //query on questionLog table
            cursor = db.query("QuestionsLog",columns,null,null,null,null,null);


            //create a string array list to store db data
            List<String> record = new ArrayList<String>();
            while (cursor.moveToNext()) {

                //get data from database
                int questionID = cursor.getInt(cursor.getColumnIndex("questionID"));
                String question = cursor.getString(cursor.getColumnIndex("question"));
                String answer = cursor.getString(cursor.getColumnIndex("answer"));
                String yourAnswer = cursor.getString(cursor.getColumnIndex("yourAnswer"));

                //add string to string list array
                record.add(String.format("%10s %-10s %15s %15s", "Question ID:"+questionID+"\n\n   ", "Question: "+question+"   ", "\n\nAnswer: "+answer+"   ","Your Answer: "+ yourAnswer+"   \n"));;

            }
            //close connection to database
            db.close();

            //store array list things in to array
            String[] record_ = record.toArray(new String[0]);

            //set array adapter
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, record_);
            lvList.setAdapter(arrayAdapter);


        }catch (Exception e){
            //nothing to do, not affect app usage
        }


    }


    //back to menu button
    public void btnMenu (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    //order list view things
    public void btnFilter (View view){
        tmp++;
        lvList=findViewById(R.id.lvList);

        //tmp will +1 on each click, and %2 will find out to order by asc or desc on each click
        if(tmp%2==0){
            try{
                //show what now order now
                Toast.makeText(this, "Descending Order",Toast.LENGTH_SHORT).show();

                //same database work will order by question id desc, nearly same with onCreate database section

                String sortOrder= "questionID Desc";
                db = SQLiteDatabase.openDatabase("/data/data/com.example.mathematicgame/eBidDB", null, SQLiteDatabase.OPEN_READONLY);

                //query with order
                cursor = db.query("QuestionsLog",columns,null,null,null,null,sortOrder);


                //create a string array list to store db data
                List<String> record = new ArrayList<String>();
                while (cursor.moveToNext()) {

                    //get data from database
                    int questionID = cursor.getInt(cursor.getColumnIndex("questionID"));
                    String question = cursor.getString(cursor.getColumnIndex("question"));
                    String answer = cursor.getString(cursor.getColumnIndex("answer"));
                    String yourAnswer = cursor.getString(cursor.getColumnIndex("yourAnswer"));

                    record.add(String.format("%10s %-10s %15s %15s", "Question ID:"+questionID+"\n\n   ", "Question: "+question+"   ", "\n\nAnswer: "+answer+"   ","Your Answer: "+ yourAnswer+"   \n"));;

                }

                //store array list things in to array
                String[] record_ = record.toArray(new String[0]);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, record_);
                lvList.setAdapter(arrayAdapter);

                //close connection to database
                db.close();
            }catch (Exception e){
                //nothing to do, not affect app usage
            }
        }else{


            //same database work will order by question id asc, nearly same with onCreate database section
            try{
                //show what now order now
                Toast.makeText(this, "Ascending Order",Toast.LENGTH_SHORT).show();

                String sortOrder= "questionID ASC";
                db = SQLiteDatabase.openDatabase("/data/data/com.example.mathematicgame/eBidDB", null, SQLiteDatabase.OPEN_READONLY);

                //query with order
                cursor = db.query("QuestionsLog",columns,null,null,null,null,sortOrder);

                //create a string array list to store db data
                List<String> record = new ArrayList<String>();
                while (cursor.moveToNext()) {

                    //get data from database
                    int questionID = cursor.getInt(cursor.getColumnIndex("questionID"));
                    String question = cursor.getString(cursor.getColumnIndex("question"));
                    String answer = cursor.getString(cursor.getColumnIndex("answer"));
                    String yourAnswer = cursor.getString(cursor.getColumnIndex("yourAnswer"));

                    record.add(String.format("%10s %-10s %15s %15s", "Question ID:"+questionID+"\n\n   ", "Question: "+question+"   ", "\n\nAnswer: "+answer+"   ","Your Answer: "+ yourAnswer+"   \n"));;

                }

                //store array list things in to array
                String[] record_ = record.toArray(new String[0]);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, record_);
                lvList.setAdapter(arrayAdapter);

                //close connection to database
                db.close();
            }catch (Exception e){
                //nothing to do, not affect app usage
            }
        }
    }

}