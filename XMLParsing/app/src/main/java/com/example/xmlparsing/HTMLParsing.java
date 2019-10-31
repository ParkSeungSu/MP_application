package com.example.xmlparsing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class HTMLParsing extends AppCompatActivity {
    private TextView tv;
    private String strHtml="";
    String day="";
    private Button bt1;
    private Button bt2;
    private Button bt3;
    private Button bt4;
    private Button bt5;
    ArrayList<Button> btList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_htmlparsing);
        btList=new ArrayList<Button>();
        tv=findViewById(R.id.textView);
        bt1=findViewById(R.id.button);
        bt2=findViewById(R.id.button2);
        bt3=findViewById(R.id.button3);
        bt4=findViewById(R.id.button4);
        bt5=findViewById(R.id.button5);
        btList.add(bt1);
        btList.add(bt2);
        btList.add(bt3);
        btList.add(bt4);
        btList.add(bt5);
        new WorkerThresd().start();
        for(int i= 0;i<btList.size();i++){
            final int finalI = i;
            btList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    day=btList.get(finalI).getText().toString();
                    HTMLParsing();
                }
            });
        }
    }


    class WorkerThresd extends Thread{
        String strLine;

        @Override
        public void run() {
            try {
                URL url = new URL("http://www.halla.ac.kr/mbs/kr/jsp/restaurant/restaurant.jsp?configIdx=23342&id=kr_050301000000");
                BufferedReader in =new BufferedReader(new InputStreamReader(url.openStream()));
                while((strLine=in.readLine())!=null){
                    strHtml+=strLine;
                }
                in.close();
            }catch (Exception e){
                tv.setText("네트워크 오류");
            }
        }
    }
    void HTMLParsing(){
        String strMenu=day+" 식단표\n\n";
        try {
            String index=day+" 식단표";
            int Start=strHtml.indexOf(index);
            int BreakfastStart=strHtml.indexOf("-&nbsp",Start);
            int BreakfastEnd=strHtml.indexOf("</p>",BreakfastStart);
            String BreakfastFood=strHtml.substring(BreakfastStart+7,BreakfastEnd);
            strMenu+="조식: "+BreakfastFood+"\n\n";

            int LunchStandardStart=strHtml.indexOf("-&nbsp",BreakfastEnd);
            int LunchStandardEnd=strHtml.indexOf("</p>",LunchStandardStart);
            String LunchStandardFood=strHtml.substring(LunchStandardStart+7,LunchStandardEnd);
            strMenu+="중식(일반): "+LunchStandardFood+"\n\n";

            int LunchSpecialStart=strHtml.indexOf("-&nbsp",LunchStandardEnd);
            int LunchSpecialdEnd=strHtml.indexOf("</p>",LunchSpecialStart);
            String LunchSpecialFood=strHtml.substring(LunchSpecialStart+7,LunchSpecialdEnd);
            strMenu+="중식(특식): "+LunchSpecialFood+"\n\n";

            int LunchFastStart=strHtml.indexOf("-&nbsp",LunchSpecialdEnd);
            int LunchFastdEnd=strHtml.indexOf("</p>",LunchFastStart);
            String LunchFastFood=strHtml.substring(LunchFastStart+7,LunchFastdEnd);
            strMenu+="중식(즉석): "+LunchFastFood+"\n\n";

            int DinnerStart=strHtml.indexOf("-&nbsp",LunchFastdEnd);
            int DinnerEnd=strHtml.indexOf("</p>",DinnerStart);
            String DinnerFood=strHtml.substring(DinnerStart+7,DinnerEnd);
            strMenu+="석식: "+DinnerFood+"\n\n";

            tv.setText(strMenu);
        }catch (Exception e){
            tv.setText("파싱 오류");
        }

    }
}
