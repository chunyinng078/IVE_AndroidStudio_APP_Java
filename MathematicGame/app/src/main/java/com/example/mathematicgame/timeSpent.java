package com.example.mathematicgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class timeSpent extends AppCompatActivity {

    //variables
    public String playTime_;    //store play time
    public String playCount_;   //store play count (questions)

    public TextView tvPlayTime;
    public TextView tvAvgTime;
    public TextView tvGrade;
    public TextView tvAnsCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_spent);

        //find view by id stuff
        tvPlayTime=findViewById(R.id.tvPlayTime);
        tvAvgTime=findViewById(R.id.tvAvgTime);
        tvGrade=findViewById(R.id.tvGrade);
        tvAnsCount=findViewById(R.id.tvAnsCount);

        //get intent
        Intent intent = getIntent();

        //getExtras (String variable) from last activity (duration and questions answered)
        this.playTime_ = intent.getExtras().getString("playTime").toString();
        this.playCount_ = intent.getExtras().getString("playCount").toString();

        //calculate average time, see time per question is how many
        double avgTime_= Double.parseDouble(playTime_)/Double.parseDouble(playCount_);

        //set questions answered count and duration and avg time of last game
        tvAnsCount.setText(playCount_+" questions");        //question count
        tvPlayTime.setText(playTime_+" seconds");           //duration
        tvAvgTime.setText(String.format("%.2f",avgTime_)+" seconds");       //avg time

        //set game grade
        if (avgTime_ <=5){
            tvGrade.setText("YOUR SPEED IS FAST");  //<= 5 very good
        }else if(avgTime_ <=10){
            tvGrade.setText("YOUR SPEED ARE NORMAL");   //<= 10 normal
        }else{
            tvGrade.setText("YOU NEED BE FASTER");  // >10 bad

        }

    }

    //a button to go back to menu
    public void btnMenu(View view){

        //start menu Activity (main Activity)
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}