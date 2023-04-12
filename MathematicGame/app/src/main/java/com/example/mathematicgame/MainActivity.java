package com.example.mathematicgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    //declare variable
    SQLiteDatabase db;
    String QLsql;
    SQLiteDatabase GLdb;
    String GLsql;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // Create a database if it does not exist

            //open the database
            db = SQLiteDatabase.openDatabase("/data/data/com.example.mathematicgame/eBidDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);

            //create QuestionsLog table
            QLsql = "CREATE TABLE  IF NOT EXISTS QuestionsLog ('questionID' INTEGER  PRIMARY KEY AUTOINCREMENT, 'question' text, 'answer' text, 'yourAnswer' text);";

            //run the sql above
            db.execSQL(QLsql);

            //create GamesLog table
            GLsql = "CREATE TABLE IF NOT EXISTS GamesLog ('gameID' INTEGER  PRIMARY KEY AUTOINCREMENT, 'playDate' text, 'playTime' text, 'duration' INTEGER ,'correctCount' INTEGER , 'wrongCount' INTEGER );";

            //run the sql above
            db.execSQL(GLsql);

            //close connection to database
            db.close();



        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    public void start(View view){

        //play sound effect
        final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.start);
        mediaPlayer.start();

        //start  Game Activity
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);


    }

    //clear Log data
    public void btnClearData(View view){

        //create alert dialog
        new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("You really want to delete all Log data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    //if player clicked yes
                    public void onClick(DialogInterface dialog, int which) {

                        //show a message on log deleted
                        Toast.makeText(getApplicationContext(),"Game Data Are Deleted.",Toast.LENGTH_SHORT).show();

                        //delete all game data
                        //open the database
                        db = SQLiteDatabase.openDatabase("/data/data/com.example.mathematicgame/eBidDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);

                        //create the sql of drop QuestionsLog table
                        QLsql = "DROP TABLE IF EXISTS 'QuestionsLog';";

                        //run the sql
                        db.execSQL(QLsql);

                        //create the sql of drop GamesLog table
                        QLsql = "DROP TABLE IF EXISTS 'GamesLog';";

                        //run the sql
                        db.execSQL(QLsql);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    //if player clicked no
                    public void onClick(DialogInterface dialog, int which) {
                        //show a message of nothing deleted
                        Toast.makeText(getApplicationContext(),"No Game Data Are Deleted.",Toast.LENGTH_SHORT).show();
                    }
                }).show();




    }

    public void btnQL(View view){
        //open question log Activity
        Intent intent = new Intent(this, QuestionsLog.class);
        startActivity(intent);

    }
    public void btnGL(View view){

        //open question game Activity
        Intent intent = new Intent(this, GamesLog.class);
        startActivity(intent);
    }

}
