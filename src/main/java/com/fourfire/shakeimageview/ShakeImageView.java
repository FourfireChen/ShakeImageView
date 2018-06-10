package com.fourfire.shakeimageview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 陈钊燚 on 2018/6/10.
 * QQ 1215638092
 * Github FourfireChen
 */
public class ShakeImageView extends AppCompatImageView {
    public static final int shakeX = 1;
    public static final int shakeY = 2;
    public static final int rotate = 3;
    public static final int zoom = 4;

    private AnimatorSet set;
    private int mode;
    private boolean shakable;
    private float shakeRange;
    private int shakeDuration;


    public ShakeImageView(Context context) {
        super(context);
    }

    public ShakeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShakeImageView);
        shakable = typedArray.getBoolean(R.styleable.ShakeImageView_shakable, true);
        shakeRange = typedArray.getFloat(R.styleable.ShakeImageView_shake_range, 10);
        shakeDuration = typedArray.getInt(R.styleable.ShakeImageView_shake_duration, 500);
        mode = typedArray.getInt(R.styleable.ShakeImageView_shake_mode, shakeX);
        typedArray.recycle();
        if (shakable) {
            setClickable(true);
            setAnimator();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                shake();
                break;
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setShakable(boolean shakable) {
        if (this.shakable != shakable) {
            this.shakable = shakable;
            if (shakable) {
                setClickable(true);
                setAnimator();
            }
        }
    }

    public boolean isShakable() {
        return shakable;
    }

    public float getShakeRange() {
        return shakeRange;
    }

    public void setShakeRange(float shakeRange) {
        this.shakeRange = shakeRange;
    }

    public int getShakeDuration() {
        return shakeDuration;
    }

    public void setShakeDuration(int shakeDuration) {
        this.shakeDuration = shakeDuration;
    }

    private void setAnimator() {
        set = new AnimatorSet();
        float[] shake = {0, -shakeRange, 0, shakeRange, 0, -shakeRange, 0, shakeRange, 0, -shakeRange, 0};
        //这里缩放的模式需要同时缩放x、y所以需要做一个组合动画
        if (mode == zoom) {
            shake = new float[]{1, 1 + shakeRange / 100, 1, 1 + shakeRange / 100, 1, 1 + shakeRange / 100, 1, 1 + shakeRange / 100, 1, 1 + shakeRange / 100, 1};
            ObjectAnimator xAnimator = ObjectAnimator.ofFloat(this, "scaleX", shake);
            ObjectAnimator yAnimator = ObjectAnimator.ofFloat(this, "scaleY", shake);
            set.play(xAnimator).with(yAnimator);
            set.setDuration(shakeDuration);
        } else {
            String propertyName = "translationX";
            switch (mode) {
                case shakeX:
                    propertyName = "translationX";
                    break;
                case shakeY:
                    propertyName = "translationY";
                    break;
                case rotate:
                    propertyName = "rotation";
                    break;
            }
            ObjectAnimator shakeAnimator = ObjectAnimator.ofFloat(this, propertyName, shake);
            set.play(shakeAnimator);
            set.setDuration(shakeDuration);
        }
    }

    private void shake() {
        if (set != null && shakable) {
            set.start();
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
