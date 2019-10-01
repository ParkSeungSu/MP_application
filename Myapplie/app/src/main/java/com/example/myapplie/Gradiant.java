package com.example.myapplie;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Gradiant extends AppCompatActivity {
    float savedScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences d=getSharedPreferences("FILE",MODE_PRIVATE);
        savedScore=d.getFloat("SCORE",0);
        TextView textView=findViewById(R.id.textView);
        textView.setText("최고온도: "+savedScore+"℃");
        Button save=findViewById(R.id.btSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences s=getSharedPreferences("FILE",MODE_PRIVATE);
                SharedPreferences.Editor e = s.edit();
                EditText editText=findViewById(R.id.editText);
                float currentScore=Float.parseFloat(editText.getText().toString());
                if(currentScore>savedScore) {
                    e.putFloat("SCORE",currentScore);
                    e.commit();
                    Toast.makeText(Gradiant.this, currentScore+" 저장 완료", Toast.LENGTH_SHORT).show();
                    TextView textView=findViewById(R.id.textView);
                    savedScore=s.getFloat("SCORE",0);
                    textView.setText("최고온도: "+savedScore+"℃");
                }else{
                    Toast.makeText(Gradiant.this, "낮은 온도는 저장 X", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}


