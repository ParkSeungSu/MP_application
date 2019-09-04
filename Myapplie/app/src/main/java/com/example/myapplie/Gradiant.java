package com.example.myapplie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Gradiant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }
}
class MyView extends View {
    final  int[] colors={ Color.rgb(255,0,0),
            Color.rgb(255,127,0),
            Color.rgb(255,255,0),
            Color.rgb(0,255,0),
            Color.rgb(0,0,255),
            Color.rgb(75,0,130),
            Color.rgb(143,0,255),};
    public MyView(Context context) {
        super(context);
    }
    public void onDraw(Canvas canvas){
        Paint paint = new Paint();
        paint.setShader(new RadialGradient(getWidth()/4,getHeight()/4,getWidth()/4,Color.RED, Color.BLACK, Shader.TileMode.CLAMP));
        canvas.drawCircle(getWidth()/4,getHeight()/4,getWidth()/4,paint);
        paint.setShader(new RadialGradient(3*(getWidth()/4),getHeight()/4,getWidth()/4,Color.YELLOW, Color.BLACK, Shader.TileMode.CLAMP));
        canvas.drawCircle(3*(getWidth()/4),getHeight()/4,getWidth()/4,paint);
        paint.setShader(new RadialGradient(getWidth()/4,3*(getHeight()/4),getWidth()/4,Color.GREEN, Color.BLACK, Shader.TileMode.CLAMP));
        canvas.drawCircle(getWidth()/4,3*(getHeight()/4),getWidth()/4,paint);
         paint.setShader(new RadialGradient(3*(getWidth()/4),3*(getHeight()/4),getWidth()/4,Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP));
        canvas.drawCircle(3*(getWidth()/4),3*(getHeight()/4),getWidth()/4,paint);

    }
}
