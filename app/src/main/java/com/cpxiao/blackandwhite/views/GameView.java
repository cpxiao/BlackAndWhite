package com.cpxiao.blackandwhite.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cpxiao.blackandwhite.mode.Background;
import com.cpxiao.blackandwhite.mode.Player;
import com.cpxiao.blackandwhite.mode.extra.ColorExtra;
import com.cpxiao.gamelib.views.BaseSurfaceViewFPS;

/**
 * @author cpxiao on 2017/09/20.
 */

public class GameView extends BaseSurfaceViewFPS {
    private Background mBackground;
    private Player mPlayerLeft, mPlayerRight;
    private float mSpeedY;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initWidget() {
        mBackground = (Background) new Background.Build()
                .setW(mViewWidth)
                .setH(mViewHeight)
                .build();

        float mPlayerWH = 0.085F * mViewWidth;
        float mDeltaSpeed = 0.005F * mViewWidth;
        mPlayerLeft = (Player) new Player.Build()
                .setColor(Color.parseColor(ColorExtra.colorPlayerLeft))
                .setDeltaSpeed(mDeltaSpeed)
                .setW(mPlayerWH)
                .setH(mPlayerWH)
                .centerTo(0.5F * mViewWidth, 0.6F * mViewHeight)
                .setMovingRangeRectF(new RectF(0, 0, 0.5F * mViewWidth, mViewHeight))
                .build();


        mPlayerRight = (Player) new Player.Build()
                .setColor(Color.parseColor(ColorExtra.colorPlayerRight))
                .setDeltaSpeed(-mDeltaSpeed)
                .setW(mPlayerWH)
                .setH(mPlayerWH)
                .centerTo(0.5F * mViewWidth, 0.6F * mViewHeight)
                .setMovingRangeRectF(new RectF(0.5F * mViewWidth, 0, mViewWidth, mViewHeight))
                .build();
    }

    @Override
    public void drawCache() {
        mBackground.onDraw(mCanvasCache, mPaint);

        mPaint.setColor(Color.RED);
        mPlayerLeft.draw(mCanvasCache, mPaint);
        mPlayerRight.draw(mCanvasCache, mPaint);
    }

    @Override
    protected void timingLogic() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Context context = getContext();
        if (action == MotionEvent.ACTION_DOWN) {
            float speed = 3F * mViewWidth / mFPS;
            if (event.getX() <= 0.5F * mViewWidth) {
                //click left
                mPlayerLeft.setSpeedX(-speed);
            } else {
                //click right
                mPlayerRight.setSpeedX(speed);
            }
        }
        return true;
    }
}
