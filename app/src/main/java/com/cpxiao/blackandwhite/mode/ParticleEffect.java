package com.cpxiao.blackandwhite.mode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.cpxiao.gamelib.mode.common.Sprite;

/**
 * @author cpxiao on 2017/09/22.
 */

public class ParticleEffect extends Sprite {

    protected ParticleEffect(Build build) {
        super(build);
    }

    @Override
    protected void beforeDraw(Canvas canvas, Paint paint) {
        setSpeedX((float) (Math.random() - 0.5F));
        setSpeedY((float) (10 + Math.random()));
        super.beforeDraw(canvas, paint);
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        super.onDraw(canvas, paint);
        paint.setColor(Color.RED);
//        paint.setAlpha((int) (255 - 200 * getFrame() / (getFrame() + 1)));
        canvas.drawCircle(getCenterX(), getCenterY(), 0.5F * getWidth() * (1 + getFrame() / (getFrame() + 1)), paint);
    }

    public static class Build extends Sprite.Build {
        @Override
        public ParticleEffect build() {
            return new ParticleEffect(this);
        }
    }
}
