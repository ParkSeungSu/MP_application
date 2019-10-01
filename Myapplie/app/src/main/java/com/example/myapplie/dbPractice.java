package com.example.myapplie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class dbPractice extends AppCompatActivity {
    Button addBt;
    Button delBt;
    String DB_NAME = "toeic.db";
    Button[] bts = new Button[12];
    StringBuilder builder;
    TextView Score;
    String SCORE = "SCORE: ";
    int score = 0;
    EditText editNum;
    EditText editWord;
    EditText WordCount;

    public void setScore(int i) {
        score += i;
    }

    public int getScore() {
        return score;
    }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_practice);
        addBt = findViewById(R.id.btSave);
        delBt = findViewById(R.id.btDelete);
        File dbFile = getDatabasePath(DB_NAME);
        Score = findViewById(R.id.score);
        editNum = findViewById(R.id.editNum);
        editWord = findViewById(R.id.editWord);
        WordCount = findViewById(R.id.WordCount);
        Score.setText(SCORE + getScore());
        if (!dbFile.exists()) {
            copyDatabase(dbFile);
        }
        bts[0] = findViewById(R.id.b1);
        bts[1] = findViewById(R.id.b2);
        bts[2] = findViewById(R.id.b3);
        bts[3] = findViewById(R.id.b4);
        bts[4] = findViewById(R.id.b5);
        bts[5] = findViewById(R.id.b6);
        bts[6] = findViewById(R.id.b7);
        bts[7] = findViewById(R.id.b8);
        bts[8] = findViewById(R.id.b9);
        bts[9] = findViewById(R.id.b10);
        bts[10] = findViewById(R.id.b11);
        bts[11] = findViewById(R.id.b12);
        addBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.editText);
                String eng = editText.getText().toString();
                SQLiteDatabase db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
                Cursor cursor = db.rawQuery(
                        "SELECT 단어,해석 FROM 토익 WHERE 단어 like '" + eng + "%';", null);
                TextView textView = findViewById(R.id.textView);
                if (cursor.getCount() > 0) {
                    String result = "";
                    while (cursor.moveToNext()) {
                        String strWord = cursor.getString(0);
                        String strMean = cursor.getString(1);
                        result += (strWord + " : " + strMean + "\n");
                    }
                    textView.setText(result);
                } else textView.setText("DB에 없는 단어입니다.");
            }
        });
        delBt.setOnClickListener(new View.OnClickListener() {
            int limit;
            int[] y = new int[3];
            char wordPeace;
            int wordcount;
            ArrayList<String> anser = new ArrayList<>();
            ArrayList<String> apla1 = new ArrayList<>();//어레이 리스트를 통해서 해결해볼려고 시도중
            ArrayList<String> apla2 = new ArrayList<>();

            @Override
            public void onClick(View v) {
                SQLiteDatabase db;
                Cursor cursor;
                try {
                    wordcount = Integer.parseInt(WordCount.getText().toString());
                    limit = Integer.parseInt(editNum.getText().toString());
                    wordPeace = editWord.getText().charAt(0);
                } catch (NumberFormatException e) {
                    Toast.makeText(dbPractice.this, "모든 부분을 입력해주세요\n지금은 그냥 넘어가지만;;", Toast. LENGTH_LONG).show();
                }
                if (limit > 7) {
                    db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
                    cursor = db.rawQuery("SELECT 단어,해석 FROM 토익 WHERE length(단어) == " + limit + " AND 단어 LIKE '%" + wordPeace + "%' AND (length(단어)-length(replace(단어,'" + wordPeace + "',''))) = " + wordcount + ";", null);
                } else {
                    db = openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
                    cursor = db.rawQuery("SELECT 단어,해석 FROM 토익 WHERE length(단어)>=7;", null);
                }
                Random random = new Random();
                int count = cursor.getCount();
                int x = random.nextInt(count);
                cursor.moveToPosition(x);
                TextView textView = findViewById(R.id.textView);
                final TextView textView1 = findViewById(R.id.textView1);
                TextView resultView = findViewById(R.id.answer);
                TextView resultView2 = findViewById(R.id.answer2);
                String Word = "";
                String Mean = "";
                Word = cursor.getString(0);
                Mean = cursor.getString(1);
                for (int i = 0; i < bts.length; i++) {
                    final int finalI = i;
                    final String finalWord = Word;
                    bts[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean state = false;
                            if (bts[finalI].getCurrentTextColor() != Color.BLUE) {
                                state = true;
                            }
                            for (int i = 0; i < 3; i++) {
                                if (state) {
                                    if (finalWord.charAt(y[i]) == bts[finalI].getText().charAt(0)) {
                                        builder.setCharAt(y[i], finalWord.charAt(y[i]));
                                        setScore(100);
                                        textView1.setText(builder.toString());
                                    }
                                }
                            }
                            if (state) {
                                bts[finalI].setTextColor(Color.BLUE);
                            } else {
                                setScore(-50);
                            }
                            Score.setText(SCORE + getScore());
                            String word = builder.toString();
                            if (word.equals(finalWord)) {
                                Toast.makeText(dbPractice.this, "정답입니다~", Toast.LENGTH_SHORT).show();
                                delBt.callOnClick();
                            }
                        }
                    });
                }
                if (Word.length() > 9) {
                    y[0] = random.nextInt(4);
                    for (int b = 1; b < y.length; b++) {
                        y[b] = y[b - 1] + random.nextInt(2) + 2;
                    }
                } else {
                    y[0] = random.nextInt(2);
                    for (int b = 1; b < y.length; b++) {
                        y[b] = y[b - 1] + 2;
                    }
                }
                String answer = "";
                builder = new StringBuilder(Word);
                for (int i = 0; i < y.length; i++) {
                    builder.setCharAt(y[i], '_');
                    answer += Word.charAt(y[i]) + " ";
                    anser.add(String.valueOf(Word.charAt(y[i])));
                }
                for (int i = 0; i < 26; i++) { //12
                    apla1.add(String.valueOf((char) (i + 97)));
                }
                for (int i = 0; i < 3; i++) {
                    if (!apla2.contains(anser.get(i)))
                        apla2.add(anser.get(i));
                }
                for (int i = 0; i < apla1.size(); i++) {
                    if (!apla2.contains(apla1.get(i)))
                        apla2.add(apla1.get(i));
                    if (apla2.size() == 12)
                        break;
                }
                Collections.shuffle(apla2);
                for (int i = 0; i < 12; i++) {
                    bts[i].setText((String.valueOf(apla2.get(i))));
                    bts[i].setTextColor(Color.BLUE);
                    if (anser.contains(bts[i].getText())) {
                        bts[i].setTextColor(Color.RED);
                    }
                }
                textView.setText(Mean);
                textView1.setText(builder.toString());
                resultView.setText("답: " + Word);
                resultView2.setText("빈칸 순서대로: " + answer);
                cursor.moveToFirst();
                apla2.clear();
                apla1.clear();
                anser.clear();
            }
        });
    }
}
