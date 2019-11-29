package ssu.cheesecake.blueberry.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NameCropImageView extends View implements View.OnTouchListener {
    private Paint paint;
    private List<Point> points;
    private Point leftTop;
    private Point rightBottom;

    boolean flgPathDraw = true;
    boolean bfirstpoint = false;

    Point mfirstpoint = null;
    Point mlastpoint = null;

    Bitmap bitmap;

    public NameCropImageView(Context context) {
        super(context);
    }

    public void init(Bitmap bitmap) {
        this.bitmap = bitmap;

        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        paint.setStrokeWidth(50);
        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        this.setOnTouchListener(this);
        points = new ArrayList<Point>();

        bfirstpoint = false;
    }

    public NameCropImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NameCropImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);

        Path path = new Path();
        boolean first = true;

        for (int i = 0; i < points.size(); i += 2) {
            Point point = points.get(i);
            if (first) {
                first = false;
                path.moveTo(point.x, point.y);
            } else if (i < points.size() - 1) {
                Point next = points.get(i + 1);
                path.quadTo(point.x, point.y, next.x, next.y);
            } else {
                mlastpoint = points.get(i);
                path.lineTo(point.x, point.y);
            }
        }
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();

        if (flgPathDraw) {

            if (bfirstpoint) {
                if (point.x < leftTop.x)
                    leftTop.x = point.x;
                if (point.x > rightBottom.x)
                    rightBottom.x = point.x;
                if (point.y < leftTop.y)
                    leftTop.y = point.y;
                if (point.y > rightBottom.y)
                    rightBottom.y = point.y;
                if (comparepoint(mfirstpoint, point)) {
                    // points.add(point);
                    points.add(mfirstpoint);
                    flgPathDraw = false;
                } else {
                    points.add(point);
                }
            } else {
                points.add(point);
            }

            if (!(bfirstpoint)) {

                mfirstpoint = point;
                bfirstpoint = true;
                leftTop = new Point(point.x, point.y);
                rightBottom = new Point(point.x, point.y);
            }
        }

        invalidate();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            mlastpoint = point;
            flgPathDraw = false;
            leftTop.x = leftTop.x - 25;
            leftTop.y = leftTop.y - 25;

            rightBottom.x = rightBottom.x + 25;
            rightBottom.y = rightBottom.y + 25;

            Log.d("DEBUG!", "onTouch: \nX: " + this.getX() + " ,Y: " + this.getY());

            if (leftTop.x < (int) Math.ceil(this.getX()))
                leftTop.x = (int) Math.ceil(this.getX());
            if (leftTop.y < (int) Math.ceil(this.getY()))
                leftTop.y = (int) Math.ceil(this.getY());
            if (rightBottom.x > (int) Math.floor(this.getX()) + this.getWidth())
                rightBottom.x = (int) Math.floor(this.getX()) + this.getWidth();
            if (rightBottom.y > (int) Math.floor(this.getY()) + this.getHeight())
                rightBottom.y = (int) Math.floor(this.getY()) + this.getHeight();
        }

        //TODO:좌표를 인텐트로 넘기기 누구한테? edit activity한테
        return false;
    }


    private boolean comparepoint(Point first, Point current) {
        int left_range_x = (int) (current.x - 3);
        int left_range_y = (int) (current.y - 3);

        int right_range_x = (int) (current.x + 3);
        int right_range_y = (int) (current.y + 3);

        if ((left_range_x < first.x && first.x < right_range_x)
                && (left_range_y < first.y && first.y < right_range_y)) {
            if (points.size() < 10) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }

}
