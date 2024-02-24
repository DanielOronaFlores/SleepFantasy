package ChartPainter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

@SuppressLint("ViewConstructor")
public class BarChartPainter extends View {
    private final Paint paint;
    private final int[] colors;
    private final float[] values;

    public BarChartPainter(Context context, int[] colors, float[] values) {
        super(context);
        this.colors = colors;
        this.values = values;

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        float barWidth = 50;
        float barSpace = 0;

        for (int i = 0; i < values.length; i++) {
            float barHeight = (values[i] / getMaxValue()) * getHeight();

            paint.setStyle(Paint.Style.FILL);

            if (i % 2 == 0) paint.setColor(ContextCompat.getColor(this.getContext(), colors[0]));
            else paint.setColor(ContextCompat.getColor(this.getContext(), colors[1]));

            float left = i * (barSpace + barWidth);
            float top = getHeight() - barHeight;
            float right = left + barWidth;
            float bottom = getHeight();

            canvas.drawRect(left, top, right, bottom, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setColor(Color.BLACK);

            canvas.drawRect(left, top, right, bottom, paint);
        }
    }

    private float getMaxValue() {
        float max = 0;
        for (float value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
