package com.cpxiao.blackandwhite.mode;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.cpxiao.gamelib.mode.common.Sprite;

/**
 * @author cpxiao on 2017/09/21.
 */

public class Player extends Sprite {

    private int mColor;

    private float mDeltaSpeed;

    protected Player(Build build) {
        super(build);
        this.mColor = build.color;
        this.mDeltaSpeed = build.deltaSpeed;
    }

    public boolean isTouchLeft() {
        return getX() - 0.1F <= getMovingRangeRectF().left;
    }

    public boolean isTouchRight() {
        return getX() + getWidth() + 0.1F >= getMovingRangeRectF().right;
    }

    @Override
    protected void beforeDraw(Canvas canvas, Paint paint) {
        super.beforeDraw(canvas, paint);
        setSpeedX(getSpeedX() + mDeltaSpeed);
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        paint.setColor(mColor);
        super.onDraw(canvas, paint);
        paint.setAlpha(100);
        canvas.drawCircle(getCenterX(), getCenterY(), 0.5F * getWidth(), paint);
        paint.setAlpha(255);
        canvas.drawCircle(getCenterX(), getCenterY(), 0.315F * getWidth(), paint);
    }


    public static class Build extends Sprite.Build {

        private int color;
        private float deltaSpeed;

        public Build setColor(int color) {
            this.color = color;
            return this;
        }

        public Build setDeltaSpeedX(float deltaSpeed) {
            this.deltaSpeed = deltaSpeed;
            return this;
        }

        @Override
        public Player build() {
            return new Player(this);
        }
    }
}
