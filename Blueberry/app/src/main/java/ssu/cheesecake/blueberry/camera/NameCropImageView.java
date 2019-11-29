package ssu.cheesecake.blueberry.camera;

import android.content.Context;
import android.graphics.Bitmap;
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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class NameCropImageView extends AppCompatImageView implements View.OnTouchListener
{
    private Paint paint;

    private boolean flagPathDraw = true;
    private boolean bFirstPoint = false;

    private Point mFirstPoint = null;
    private Point mLastPoint = null;

    private Bitmap bitmap;

    /**
     *
     */
    private List<Point> points;
    private Point leftTop;
    private Point rightBottom;
    private boolean hasName = false;

    public NameCropImageView(Context context) {
        super(context);
    }

    public void init(Bitmap bitmap) {
        this.bitmap = bitmap;
        super.setImageBitmap(bitmap);
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

        bFirstPoint = false;
        invalidate();
    }

    public NameCropImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NameCropImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Point getLeftTop(){
        if(hasName){
            return leftTop;
        }else {
            return new Point(0,0);
        }
    }

    public Point getRightBottom(){
        if(hasName){
            return rightBottom;
        }else {
            return new Point(1,1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

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
                mLastPoint = points.get(i);
                path.lineTo(point.x, point.y);
            }
        }
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("ocr", points.size() + "");
        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();

        if (flagPathDraw) {

            if (bFirstPoint) {
                if (point.x < leftTop.x)
                    leftTop.x = point.x;
                if (point.x > rightBottom.x)
                    rightBottom.x = point.x;
                if (point.y < leftTop.y)
                    leftTop.y = point.y;
                if (point.y > rightBottom.y)
                    rightBottom.y = point.y;
                if (comparepoint(mFirstPoint, point)) {
                    // points.add(point);
                    points.add(mFirstPoint);
                    flagPathDraw = false;
                } else {
                    points.add(point);
                }
            } else {
                points.add(point);
            }

            if (!(bFirstPoint)) {

                mFirstPoint = point;
                bFirstPoint = true;
                leftTop = new Point(point.x, point.y);
                rightBottom = new Point(point.x, point.y);
            }
        }

        invalidate();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            mLastPoint = point;
            flagPathDraw = false;
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

            hasName = true;
        }

        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
