package com.codecrafters.quizquest.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.codecrafters.quizquest.R;

public class LineChartView extends View {

    private Paint paint;
    private float[] dataPoints; // Your data points for the line chart

    public LineChartView(Context context) {
        super(context);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorPrimary)); // Set line color
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4); // Set line width
    }

    public void setDataPoints(float[] dataPoints) {
        this.dataPoints = dataPoints;
        invalidate(); // Trigger a redraw when data points change
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataPoints != null && dataPoints.length > 1) {
            float xStep = getWidth() / (dataPoints.length - 1);
            float maxY = getMaxValue(dataPoints);
            float yStep = getHeight() / maxY;

            for (int i = 0; i < dataPoints.length - 1; i++) {
                float x1 = i * xStep;
                float y1 = getHeight() - (dataPoints[i] * yStep);
                float x2 = (i + 1) * xStep;
                float y2 = getHeight() - (dataPoints[i + 1] * yStep);
                canvas.drawLine(x1, y1, x2, y2, paint);
            }
        }
    }

    private float getMaxValue(float[] values) {
        float max = Float.MIN_VALUE;
        for (float value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
