package com.mobile.oxi.xscan;

/**
 * Created by aabdel on 20/09/2015.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();

    public DrawView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        canvas.drawRect(100, 100, 150, 170, paint);
      //  paint.setStrokeWidth(0);
       // paint.setColor(Color.CYAN);
      //  canvas.drawRect(33, 60, 77, 77, paint );
       // paint.setColor(Color.YELLOW);
       // canvas.drawRect(33, 33, 77, 60, paint );

    }

}