package com.cpxiao.blackandwhite.mode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.cpxiao.blackandwhite.mode.extra.ColorExtra;
import com.cpxiao.gamelib.mode.common.Sprite;

/**
 * @author cpxiao on 2017/09/21.
 */

public class Background extends Sprite {

    public static final float centerPercent = 0.035F;

    protected Background(Build build) {
        super(build);
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        super.onDraw(canvas, paint);
        float w = 0.5F * getWidth();
        float h = getHeight();
        paint.setColor(Color.parseColor(ColorExtra.colorLeft));
        canvas.drawRect(0, 0, w, h, paint);

        paint.setColor(Color.parseColor(ColorExtra.colorRight));
        canvas.drawRect(w, 0, 2 * w, h, paint);

        float smallRectFW = centerPercent * getWidth();
        paint.setColor(Color.parseColor(ColorExtra.colorCenterLeft));
        canvas.drawRect(w - smallRectFW, 0, w, h, paint);

        paint.setColor(Color.parseColor(ColorExtra.colorCenterRight));
        canvas.drawRect(w, 0, w + smallRectFW, h, paint);


    }

    public static class Build extends Sprite.Build {

        @Override
        public Background build() {
            return new Background(this);
        }
    }
}
