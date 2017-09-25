package com.cpxiao.blackandwhite.mode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.cpxiao.blackandwhite.mode.extra.ColorExtra;
import com.cpxiao.gamelib.mode.common.Sprite;

/**
 * @author cpxiao on 2017/09/23.
 */

public class EnemyRight extends Sprite {
    private int mColor = Color.parseColor(ColorExtra.colorEnemyRight);

    protected EnemyRight(Build build) {
        super(build);
        setCollideRectFPercent(0.6F, 1);
    }

    private Path mPath = new Path();

    private Path getPath() {
        mPath.reset();
        mPath.moveTo(getX(), getY());
        mPath.lineTo(getX() + getWidth(), getY() + 0.5F * getHeight());
        mPath.lineTo(getX(), getY() + getHeight());
        return mPath;
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        super.onDraw(canvas, paint);
        paint.setColor(mColor);
        canvas.drawPath(getPath(), paint);
    }

    public static class Build extends Sprite.Build {

        public EnemyRight build() {
            return new EnemyRight(this);
        }
    }
}
