package com.mobile.oxi.xscan;

/**
 * Created by aabdel on 20/09/2015.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();

    public float valx=0;
    public float valy=0;
    public float ancho=0;
    public float alto=0;

    public DrawView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        //paint.setARGB(255,255,0,0);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(valx, valy, ancho, alto, paint);
      //  paint.setStrokeWidth(0);
       // paint.setColor(Color.CYAN);
      //  canvas.drawRect(33, 60, 77, 77, paint );
       // paint.setColor(Color.YELLOW);
       // canvas.drawRect(33, 33, 77, 60, paint );

    }


    public void rectVisor (float x,float y, float anc, float alt)
    {
        valx=x;
        valy=y;
        ancho=anc;
        alto=alt;
    }

}