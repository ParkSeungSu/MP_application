package com.example.myapplie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class dbPractice2 extends AppCompatActivity {
    String DB_NAME = "toeic.db";
    Button randBt;
    TextView explain;
    TextView answer;
    LinearLayout layout;
    Context context;
    ArrayList<RadioButton> radioButtons;

    private void copyDatabase(File dbFile) {
        try {
            String folderPath = "/data/data/" + getPackageName() + "/database";
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            InputStream is = getAssets().open(DB_NAME);
            OutputStream os = new FileOutputStream(dbFile);
            byte[] buffer = new byte[1024];
            while (is.read(buffer) > 0) {
                os.write(buffer);
            }
            os.flush();
            is.close();
            os.close();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_practice2);
        File dbFile = getDatabasePath(DB_NAME);
        explain = findViewById(R.id.Explain);
        answer = findViewById(R.id.Answer);
        randBt = findViewById(R.id.RandomBt);
        layout = findViewById(R.id.lina);
        context = this;
        if (!dbFile.exists()) {
            copyDatabase(dbFile);
        }
        randBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeAllViews();
                layout.addView(randBt);
                layout.addView(explain);
                layout.addView(answer);
                radioButtons = new ArrayList<>();
                ArrayList<String> words = new ArrayList();

                SQLiteDatabase db;
                Cursor cursor;
                db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
                cursor = db.rawQuery("SELECT 단어,해석 FROM 토익;", null);
                Random random = new Random();
                int count = cursor.getCount();
                int x = random.nextInt(count);
                cursor.moveToPosition(x);
                String Word = "";
                String Mean = "";
                Word = cursor.getString(0);
                Mean = cursor.getString(1);
                answer.setText(Word);
                explain.setText(Mean);

                int radioNum = random.nextInt(9);
                //radio button생성
                words.add(Word);
                for (int i = 0; ; i++) {
                    int y = random.nextInt(count);
                    cursor.moveToPosition(y);
                    String a = cursor.getString(0);
                    if (!words.contains(a))
                        words.add(a);
                    if (words.size() == (radioNum + 2))
                        break;
                }
                Collections.shuffle(words);
                for (int i = 0; i < (2 + radioNum); i++) {
                    final RadioButton r = new RadioButton(context);
                    r.setText(words.get(i));
                    final String finalWord = Word;
                    if (Word.contains(r.getText()))
                        r.setTextColor(Color.RED);
                    else
                        r.setTextColor(Color.BLUE);
                    r.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (finalWord.contains(r.getText())){
                                Toast.makeText(dbPractice2.this, "정답입니다!", Toast.LENGTH_SHORT).show();
                                randBt.callOnClick();
                            }
                            else
                                Toast.makeText(dbPractice2.this, "오답입니다!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    radioButtons.add(r);
                }
                for (int i = 0; i < radioButtons.size(); i++) {
                    layout.addView(radioButtons.get(i));
                }
                words.clear();
                radioButtons.clear();
            }
        });
    }
}
