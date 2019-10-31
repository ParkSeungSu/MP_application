package com.example.xmlparsing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class NAVERParsing extends AppCompatActivity {
    String strHtml="";
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naverparsing);
        tv=findViewById(R.id.naverText);
        tv.setText("검색중...");
        Handler h=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                HTMLParse();
            }
        };
        new WorkersThread(h).start();

    }
    class WorkersThread extends Thread{
        Handler h;
        String strLine;
        WorkersThread(Handler h){this.h=h;}

        @Override
        public void run() {
            try{
                URL aURL=new URL("https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query=%EB%84%A4%EC%9D%B4%EB%B2%84+%EB%AF%B8%EC%84%B8%EB%A8%BC%EC%A7%80&oquery=%EC%A0%84%EA%B5%AD%EB%AF%B8%EC%84%B8%EB%A8%BC%EC%A7%80&tqi=UkQNgwp0JywssFdNOXhsssssttN-313606");
                BufferedReader in =new BufferedReader(new InputStreamReader(aURL.openStream()));
                while((strLine=in.readLine())!=null){
                        strHtml+=strLine;
                }
                in.close();
                h.sendMessage(new Message());
            }catch (Exception e){
                tv.setText("네트워크 에러"+e.toString());
            }
        }
    }
    void HTMLParse(){

        try {
            String strContent="";
            int start = strHtml.indexOf("지역별 미세먼지 정보");
            int end=0;
            for(int i=1;i<=10;i++){
                start=strHtml.indexOf("scope=\"row\"",start);
                end=strHtml.indexOf("</span>",start);
                strContent+="실시간 검색어 "+i+"위 : "+strHtml.substring(start+6,end)+"\n";
            }
            tv.setText(strContent);
        }catch (Exception e){
            tv.setText("파싱 에러"+e.toString());
        }
    }
}
