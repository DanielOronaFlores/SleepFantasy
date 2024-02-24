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
public class PieChartPainter extends View {
    private final Paint paint;
    private final int[] colors;
    private final float[] values;

    public PieChartPainter(Context context, int[] colors, float[] values) {
        super(context);
        this.colors = colors;
        this.values = values;

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        float total = 0;
        for (float value : values) {
            total += value;
        }

        float startAngle = 0;
        float sweepAngle;
        for (int i = 0; i < values.length; i++) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(ContextCompat.getColor(this.getContext(), colors[i]));

            float left = 0;
            float top = 0;
            float right = 300;
            float bottom = 300;

            sweepAngle = (values[i] / total) * 360;
            canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, true, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.BLACK);

            canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, true, paint);
            startAngle += sweepAngle;
        }
    }
}