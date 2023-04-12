package com.example.mathematicgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GamesLogChart extends AppCompatActivity {


    private LinearLayout layout;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //create canvas drawing place
        setContentView(R.layout.activity_games_log_chart);

        layout=findViewById(R.id.llayout);
        btnBack=findViewById(R.id.btnBack);
        Panel panel = new Panel(this);


        //set background color
        layout.setBackgroundColor(Color.argb(255, 240, 163, 10));

        layout.addView(panel);



    }

    public void btnBack(View view){
        Intent intent = new Intent(this, GamesLog.class);
        startActivity(intent);
    }

    class Panel extends View {

        public  Panel(Context context){
            super(context);
        }

        //onDraw method
        public void onDraw(Canvas c){
            SQLiteDatabase db;
            Cursor cursor = null;

            //connect database
            db = SQLiteDatabase.openDatabase("/data/data/com.example.mathematicgame/eBidDB", null, SQLiteDatabase.OPEN_READONLY);

            //find total correct question count and total wrong question count
            cursor = db.rawQuery("select SUM(correctCount), SUM(wrongCount) from GamesLog", null);

            int count[]= new int[2];

            while (cursor.moveToNext()) {

                //get total correct question count
                int correctCount = cursor.getInt(cursor.getColumnIndex("SUM(correctCount)"));

                //get total wrong question count
                int wrongCount = cursor.getInt(cursor.getColumnIndex("SUM(wrongCount)"));

                //store both count in array
                count[0]=correctCount;
                count[1]=wrongCount;
            }

            //calculate percentage of each section of the pie chart
            double fullCount = count[0]+count[1];
            float correctPer = (float)count[0]/(float)fullCount*100;
            float wrongPer = (float)count[1]/(float)fullCount*100   ;

            //variable
            String title="All game result Pie chart: ";
            String items [] = {"Correct Answer : "+count[0],"Wrong Answer : "+count[1]};
            float data[] = {correctPer,wrongPer};
            int rColor[] = {0xff32cd32,0xffff0000 };
            float cDegree = 0;

            // see if phone is landscape or  portrait
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // In landscape //horizontal

                super.onDraw(c);
                Paint paint = new Paint();

                //draw the pie chart
                for(int i = 0; i<data.length;i++){
                    float drawDegree = data[i]*360/100;

                    paint.setColor(rColor[i]);
                    RectF rec = new RectF(100,150,1150,1150);
                    c.drawArc(rec,cDegree,drawDegree,true,paint);
                    cDegree+=drawDegree;

                }

                //set text color and size
                paint.setColor(Color.rgb(0,80,239));
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(100);
                paint.setTypeface(Typeface.DEFAULT_BOLD);   //set bold
                c.drawText(title,70,100,paint);

                //draw text, show how many questions answered
                c.drawText("Answered questions : "+(count[0]+count[1]),1250,250,paint);

                int vSpace = getHeight()-100;

                //draw text, show what is in the pie chart
                for(int i = 0; i<data.length;i++){
                    paint.setTextSize(100);
                    paint.setColor(rColor[i]);
                    c.drawRect(getWidth()/2,vSpace,getWidth()/2+50,vSpace+50,paint);
                    paint.setColor(Color.rgb(0,80,239));
                    c.drawText(items[i],getWidth()/2+100,vSpace+50,paint);

                    vSpace-=300;
                }


            } else {
                // In portrait //vertical


                super.onDraw(c);
                Paint paint = new Paint();

                //draw the pie chart
                for(int i = 0; i<data.length;i++){
                    float drawDegree = data[i]*360/100;

                    paint.setColor(rColor[i]);
                    

                    RectF rec = new RectF(getLeft()+(getRight()-getLeft())/10,
                            getTop()+(getBottom()-getTop())/4,
                            getRight()-(getRight()-getLeft())/10,
                            getBottom()-(getBottom()-getTop())/4    );
                    c.drawArc(rec,cDegree,drawDegree,true,paint);
                    cDegree+=drawDegree;

                }

                //set text color and size
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(70);
                paint.setTypeface(Typeface.DEFAULT_BOLD);       //set bold
                paint.setColor(Color.rgb(0,80,239));
                c.drawText(title,70,100,paint);

                //draw text, show how many questions answered
                c.drawText("Answered questions : "+(count[0]+count[1]),70,300,paint);

                int vSpace = getHeight()-100;


                //draw text, show what is in the pie chart
                for(int i = 0; i<data.length;i++){
                    paint.setColor(rColor[i]);
                    c.drawRect(getWidth()/2-300,vSpace,getWidth()/2-250,vSpace+50,paint);
                    paint.setColor(Color.rgb(0,80,239));
                    paint.setTextSize(80);
                    c.drawText(items[i],getWidth()/2-215,vSpace+50,paint);
                    vSpace-=100;
                }

            }


        }
    }
}