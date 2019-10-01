package com.example.myapplie;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class TtsStt extends AppCompatActivity implements TextToSpeech.OnInitListener {
    TextView tv;
    EditText edit;
    Button button;
    TextToSpeech tts;
    Button btPast;
    Button btFuture;
    Button btReal;
    Button btDay;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar=Calendar.getInstance();
        setContentView(R.layout.activity_tts_stt);
        tv = findViewById(R.id.tv);
        edit = findViewById(R.id.edit);
        button = findViewById(R.id.button);
        btDay = findViewById(R.id.btDay);
        btFuture = findViewById(R.id.btFuture);
        btPast = findViewById(R.id.btPast);
        btReal = findViewById(R.id.btReal);
        tts = new TextToSpeech(this, this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = edit.getText().toString();
                if (word.length() > 0) {
                    if(tts.isSpeaking())
                        tts.stop();
                    tts.setSpeechRate(1.0f);
                    tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        btReal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int hour=cal.get(Calendar.HOUR);
                if(hour>12)
                    hour-=12;
                int min=cal.get(Calendar.MINUTE);
                String word=hour+"시"+min+"분 입니다.";
                edit.setText(hour+":"+min);
                if (word.length() > 0) {
                    if(tts.isSpeaking())
                        tts.stop();
                    tts.setSpeechRate(1.0f);
                    tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        btDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                calendar=cal;
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH)+1;
                int date = cal.get(Calendar.DATE);
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                String korday="";
                switch (dayOfWeek){
                    case 1:
                        korday ="일";
                        break;
                    case 2:
                        korday ="월";
                        break;
                    case 3:
                        korday ="화";
                        break;
                    case 4:
                        korday ="수";
                        break;
                    case 5:
                        korday ="목";
                        break;
                    case 6:
                        korday ="금";
                        break;
                    case 7:
                        korday ="토";
                        break;
                }
                String word=year+"년"+month+"월"+date+"일"+korday+"요일 입니다.";
                edit.setText(year+"/"+month+"/"+date+"  "+korday+"요일");
                if (word.length() > 0) {
                    if(tts.isSpeaking())
                        tts.stop();
                    tts.setSpeechRate(1.0f);
                    tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                }

            }
        });
        btPast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(calendar.DATE,-1);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int date = calendar.get(Calendar.DATE);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String korday="";
                switch (dayOfWeek){
                    case 1:
                        korday ="일";
                        break;
                    case 2:
                        korday ="월";
                        break;
                    case 3:
                        korday ="화";
                        break;
                    case 4:
                        korday ="수";
                        break;
                    case 5:
                        korday ="목";
                        break;
                    case 6:
                        korday ="금";
                        break;
                    case 7:
                        korday ="토";
                        break;
                }
                String word=year+"년"+month+"월"+date+"일"+korday+"요일 입니다.";
                edit.setText(year+"/"+month+"/"+date+"  "+korday+"요일");
                if (word.length() > 0) {
                    if(tts.isSpeaking())
                        tts.stop();
                    tts.setSpeechRate(1.0f);
                    tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        btFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(calendar.DATE,1);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int date = calendar.get(Calendar.DATE);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                String korday="";
                switch (dayOfWeek){
                    case 1:
                        korday ="일";
                        break;
                    case 2:
                        korday ="월";
                        break;
                    case 3:
                        korday ="화";
                        break;
                    case 4:
                        korday ="수";
                        break;
                    case 5:
                        korday ="목";
                        break;
                    case 6:
                        korday ="금";
                        break;
                    case 7:
                        korday ="토";
                        break;
                }
                String word=year+"년"+month+"월"+date+"일"+korday+"요일 입니다.";
                edit.setText(year+"/"+month+"/"+date+"  "+korday+"요일");
                if (word.length() > 0) {
                    if(tts.isSpeaking())
                        tts.stop();
                    tts.setSpeechRate(1.0f);
                    tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale locale = Locale.getDefault();
            if (tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE)
                tts.setLanguage(locale);
            else
                Toast.makeText(this, "지원하지 않는 언어 오류", Toast.LENGTH_SHORT).show();
        } else if (status == ERROR) {
            Toast.makeText(this, "음성 합성 초기화 오류", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) tts.shutdown();
    }
}