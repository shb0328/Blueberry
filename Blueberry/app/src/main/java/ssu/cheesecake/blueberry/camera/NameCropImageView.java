package ssu.cheesecake.blueberry.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class NameCropImageView extends AppCompatImageView implements View.OnTouchListener {

    private Bitmap bitmap;
    private Rect bitmapRegion;

    private Paint paint;

    private int myWidth;
    private int myHeight;

    private List<Point> points;
    private Point leftTop;
    private Point rightBottom;

    private boolean isFirstTouch = true;
    private boolean isTouched = false;

    private boolean hasName = false;

    public NameCropImageView(Context context) {
        super(context);
    }

    public NameCropImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NameCropImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Bitmap bitmap) {
        this.bitmap = bitmap;
        super.setImageBitmap(bitmap);
        initBitmapRegion(getWidth(), getHeight());

        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        paint.setStrokeWidth(10);
        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        this.setOnTouchListener(this);

        invalidate();
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(points == null){
            return;
        }

        Rect rect = new Rect(leftTop.x,leftTop.y,rightBottom.x,rightBottom.y);
        canvas.drawRect(rect,paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(isFirstTouch){
            isFirstTouch = false;
            initBitmapRegion(v.getWidth(),v.getHeight());
        }

        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();

        //BitmapRegion 안에서 TouchEvent가 발생했을 때에만
        if (inBitmapRegion(point)) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){

                v.getParent().requestDisallowInterceptTouchEvent(true);

                points = new ArrayList<Point>();
                leftTop = new Point(point.x, point.y);
                rightBottom = new Point(point.x, point.y);
                points.add(point);
                isTouched = true;

            }else if(isTouched && event.getAction() == MotionEvent.ACTION_MOVE){

                if (point.x < leftTop.x)
                    leftTop.x = point.x;
                if (point.x > rightBottom.x)
                    rightBottom.x = point.x;
                if (point.y < leftTop.y)
                    leftTop.y = point.y;
                if (point.y > rightBottom.y)
                    rightBottom.y = point.y;

                points.add(point);

            }else if(isTouched && event.getAction() == MotionEvent.ACTION_UP){
                hasName = true;
                isTouched = false;
            }
        }
        //BitmapRegion 밖으로 벗어났을 경우
        else{
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                isTouched = false;
            }else if(isTouched && event.getAction() == MotionEvent.ACTION_MOVE){
                isTouched = false;
                points.removeAll(points);
                Toast.makeText(getContext(),"영역을 벗어났습니다. 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
            }else if(isTouched && event.getAction() == MotionEvent.ACTION_UP) {
                isTouched = false;
                points.removeAll(points);
                Toast.makeText(getContext(),"영역을 벗어났습니다. 다시 시도해주세요.",Toast.LENGTH_SHORT).show();
            }
        }

        invalidate();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void initBitmapRegion(int width, int height) {
        myWidth = width;
        myHeight = height;
        if (bitmap.getWidth() > this.getWidth()) {
            //view에서의 좌표를 image에서의 좌표로 재설정
            float ratio = ((float) myWidth / bitmap.getWidth());
            width = (int) (bitmap.getWidth() * ratio);
            height = (int) (bitmap.getHeight() * ratio);
        }
        bitmapRegion = new Rect(0, 0, width, height);
        return;
    }

    private boolean inBitmapRegion(Point point) {
        return (point.x >= bitmapRegion.left) && (point.x <= bitmapRegion.right) && (point.y >= bitmapRegion.top) && (point.y <= bitmapRegion.bottom);
    }
}