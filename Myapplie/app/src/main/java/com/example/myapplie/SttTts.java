package com.example.myapplie;

import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class SttTts extends AppCompatActivity implements TextToSpeech.OnInitListener {
EditText recogedit;
Button recogBt;
TextToSpeech tts;
SeekBar seekBar;
TextView speedView;
Button calBt;
Button speakBt;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stt_tts);
        recogBt=findViewById(R.id.recogBt);
        recogedit=findViewById(R.id.textRecog);
        tts = new TextToSpeech(this,this);
        speedView=findViewById(R.id.speedtext);
        seekBar=findViewById(R.id.seekBar);
        seekBar.setMax(5);
        seekBar.setProgress(1);
        calBt=findViewById(R.id.calBt);
        speakBt=findViewById(R.id.speakBt);
        recogBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    startActivityForResult(intent,0);
                }catch (Exception e){
                    Toast.makeText(SttTts.this, "구글 앱이 설치되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        calBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    startActivityForResult(intent,1);
                }catch (Exception e){
                    Toast.makeText(SttTts.this, "구글 앱이 설치되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress<0) progress=0;
                if(progress>5) progress=5;
                String speed="";
                if(progress==0){
                    speed="0.5배속";
                    tts.setSpeechRate(0.5f);
                }else if(progress==1) {
                    speed = "1배속";
                    tts.setSpeechRate(1.0f);
                }else if(progress==2){
                    speed="1.5배속";
                    tts.setSpeechRate(1.5f);
                }else if(progress==3) {
                    speed = "2.0배속";
                    tts.setSpeechRate(2.0f);
                }else if(progress==4){
                    speed ="2.5배속";
                    tts.setSpeechRate(2.5f);
                }else if(progress==5) {
                    speed = "3.0배속";
                    tts.setSpeechRate(3.0f);
                }else{
                    speed="0.5배속";
                    tts.setSpeechRate(0.5f);
                }
                speedView.setText(speed);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        speakBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=recogedit.getText().toString();
                if (str.length() > 0) {
                    if(tts.isSpeaking())
                        tts.stop();
                    tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==0 && resultCode== RESULT_OK){
            ArrayList<String> results=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String str = results.get(0);
            recogedit.setText(str);
            if (str.length() > 0) {
                if(tts.isSpeaking())
                    tts.stop();
                tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
        else if(requestCode ==1 && resultCode==RESULT_OK){
            ArrayList<String> results=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String str = results.get(0);
            String result="";
            // Create an Expression (A class from exp4j library)
            try {
                Expression expression = new ExpressionBuilder(str).build();
                // Calculate the result and display
                if(expression.evaluate()-(int)expression.evaluate()==0)
                {
                    String resu = String.valueOf((int)expression.evaluate());
                    recogedit.setText(str+"="+resu);
                    result=resu;
                } else{
                    String resu = String.valueOf(expression.evaluate());
                    recogedit.setText(str+"="+resu);
                    result=resu;
                }
            } catch (ArithmeticException e) {
                // Display an error message
               recogedit.setText("Error");
            }catch (RuntimeException e){
                Toast.makeText(this, "수식만 말해주세요 ", Toast.LENGTH_SHORT).show();
            }
            if (result.length() > 0) {
                if(tts.isSpeaking())
                    tts.stop();
                tts.speak("정답은 "+result+"입니다.", TextToSpeech.QUEUE_FLUSH, null);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

