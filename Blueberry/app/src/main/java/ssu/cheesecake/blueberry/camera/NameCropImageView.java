package ssu.cheesecake.blueberry.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class NameCropImageView extends AppCompatImageView implements View.OnTouchListener {
    private Paint paint;

    private boolean isTouched = false;

    private boolean isFirstTouch = true;

    private Rect bitmapRegion;
    private int myWidth;
    private int myHeight;

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

        invalidate();
    }

    public void initBitmapRegion(int width, int height) {
        myWidth = width;
        myHeight = height;
        if (bitmap.getWidth() > this.getWidth()) {
            Log.d("DEBUG!", "this.width: " + this.getWidth());
            Log.d("DEBUG!", "this.height: " + this.getHeight());
            Log.d("DEBUG!", "bitmap.width: " + bitmap.getWidth());
            Log.d("DEBUG!", "bitmap.height: " + bitmap.getHeight());
            Log.d("DEBUG!", "myWidth: " + myWidth);
            Log.d("DEBUG!", "myHeight: " + myHeight);
            //view에서의 좌표를 image에서의 좌표로 재설정
            width = (int) ((bitmap.getWidth() * ((float) myWidth / bitmap.getWidth())));
            height = (int) ((bitmap.getHeight() * ((float) myHeight / bitmap.getWidth())));
            Log.d("DEBUG!", "width: " + width);
            Log.d("DEBUG!", "height: " + height);
        }
        bitmapRegion = new Rect(0, 0, width, height);
        Log.d("DEBUG!", bitmapRegion.toString());
        return;
    }

    public NameCropImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NameCropImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Point getLeftTop() {
        if (hasName) {
            return leftTop;
        } else {
            return new Point(0, 0);
        }
    }

    public Point getRightBottom() {
        if (hasName) {
            return rightBottom;
        } else {
            return new Point(1, 1);
        }
    }

    private boolean inBitmapRegion(Point point) {
        return (point.x >= bitmapRegion.left) && (point.x <= bitmapRegion.right) && (point.y >= bitmapRegion.top) && (point.y <= bitmapRegion.bottom);
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
                path.lineTo(point.x, point.y);
            }
        }
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //첫 터치 이벤트 때에만 BitmapRegion 초기화
        if (isFirstTouch) {
            isFirstTouch = false;
            initBitmapRegion(v.getWidth(), v.getHeight());
        }
        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();
        //BitmapRegion 안에서 TouchEvent가 발생했을 때에만
        if (inBitmapRegion(point)) {
            //Touch Move, Up Event
            if (isTouched) {
                if (point.x < leftTop.x)
                    leftTop.x = point.x;
                if (point.x > rightBottom.x)
                    rightBottom.x = point.x;
                if (point.y < leftTop.y)
                    leftTop.y = point.y;
                if (point.y > rightBottom.y)
                    rightBottom.y = point.y;
                points.add(point);
            }
            //Touch Down Event
            else {
                points = new ArrayList<Point>();
                isTouched = true;
                leftTop = new Point(point.x, point.y);
                rightBottom = new Point(point.x, point.y);
                points.add(point);
            }
            //Touch Up Event
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (isTouched) {
                    isTouched = false;
                    //margin 25
                    leftTop.x = leftTop.x - 25;
                    leftTop.y = leftTop.y - 25;
                    rightBottom.x = rightBottom.x + 25;
                    rightBottom.y = rightBottom.y + 25;

                    hasName = true;
                }
            }
        }
        //BitmapRegion 밖으로 벗어났을 경우
        else{
            if(isTouched) {
                isTouched = false;
                //margin 25
                leftTop.x = leftTop.x - 25;
                leftTop.y = leftTop.y - 25;
                rightBottom.x = rightBottom.x + 25;
                rightBottom.y = rightBottom.y + 25;
                hasName = true;
            }
        }

        invalidate();

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
