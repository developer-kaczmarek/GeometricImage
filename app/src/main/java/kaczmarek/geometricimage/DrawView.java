package kaczmarek.geometricimage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {
    private Paint p;
    private float detail=0, radius=250;
    double alterl=0;
    double a = 0, x1 = 0, y1 = 0, x2 = 0, y2 = 0;
    private int stroke = 1;
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        p.setColor(getResources().getColor(R.color.colorAccent));

    }

    float X=0;
    float Y=0;
    public void updateCanvas(int valueAltPiSeekBar, int valueCountDetaisSeekBar, int valueStrokeWidthSeekBar){
        this.alterl=valueAltPiSeekBar;
        this.detail=valueCountDetaisSeekBar;
        this.stroke = valueStrokeWidthSeekBar;
        invalidate();
    }
    public void updateLineColor(int R, int G, int B){
        p.setColor(Color.rgb(R,G,B));
        invalidate();
    }
    public void updateBackgroundColor(int R, int G, int B){
        setBackgroundColor(Color.rgb(R,G,B));
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {

        p.setStrokeWidth(stroke);
        a = 0; x1 = 0; y1 = 0; x2 = 0; y2 = 0;
        setX();
        setY();
        while (a < 1)
        {
            x1 = radius * Math.cos(2 * Math.PI * a);
            y1 = radius * Math.sin(2 * Math.PI * a);
            x2 = radius * Math.cos(alterl * Math.PI * a);
            y2 = radius * Math.sin(alterl * Math.PI * a);
            a += 2 / detail;
            canvas.drawLine((float)x1 + X, (float)y1 + Y, (float)x2 + X, (float)y2 + Y, p);
        }
    }

    private void setX(){
        float widthView = getWidth();
        this.X = widthView/2;
    }

    private void setY(){
        float heightView = getHeight();
        this.Y = heightView/2;
    }



}