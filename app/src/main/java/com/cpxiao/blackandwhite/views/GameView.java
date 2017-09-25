package com.cpxiao.blackandwhite.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.cpxiao.R;
import com.cpxiao.blackandwhite.OnGameListener;
import com.cpxiao.blackandwhite.mode.Background;
import com.cpxiao.blackandwhite.mode.EnemyLeft;
import com.cpxiao.blackandwhite.mode.EnemyRight;
import com.cpxiao.blackandwhite.mode.ParticleEffect;
import com.cpxiao.blackandwhite.mode.Player;
import com.cpxiao.blackandwhite.mode.extra.ColorExtra;
import com.cpxiao.gamelib.mode.common.Sprite;
import com.cpxiao.gamelib.mode.common.SpriteControl;
import com.cpxiao.gamelib.views.BaseSurfaceViewFPS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author cpxiao on 2017/09/20.
 */

public class GameView extends BaseSurfaceViewFPS {
    private float mScore, mBestScore;
    private Background mBackground;
    private Player mPlayerLeft, mPlayerRight;
    private boolean mPlayerLeftClicked = false, mPlayerRightClicked = false;
    private float mSpeedY;

    private ConcurrentLinkedQueue<Sprite> mSpriteQueue = new ConcurrentLinkedQueue<>();
    private boolean isGameOver = false;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void reset() {
        mScore = 0;
        mBestScore = 0;
        isGameOver = false;
        mFrame = 0;
        mSpriteQueue.clear();
    }

    @Override
    protected void initWidget() {
        mBackground = (Background) new Background.Build()
                .setW(mViewWidth)
                .setH(mViewHeight)
                .build();

        float mPlayerWH = 0.085F * mViewWidth;
        float mDeltaSpeedX = 0.003F * mViewWidth;
        mPlayerLeft = (Player) new Player.Build()
                .setColor(Color.parseColor(ColorExtra.colorPlayerLeft))
                .setDeltaSpeedX(mDeltaSpeedX)
                .setW(mPlayerWH)
                .setH(mPlayerWH)
                .centerTo(0.5F * mViewWidth, 0.6F * mViewHeight)
                .setMovingRangeRectF(new RectF(0, 0, (0.5F - Background.centerPercent) * mViewWidth, mViewHeight))
                .build();


        mPlayerRight = (Player) new Player.Build()
                .setColor(Color.parseColor(ColorExtra.colorPlayerRight))
                .setDeltaSpeedX(-mDeltaSpeedX)
                .setW(mPlayerWH)
                .setH(mPlayerWH)
                .centerTo(0.5F * mViewWidth, 0.6F * mViewHeight)
                .setMovingRangeRectF(new RectF((0.5F + Background.centerPercent) * mViewWidth, 0, mViewWidth, mViewHeight))
                .build();
    }

    @Override
    public void drawCache() {
        mBackground.onDraw(mCanvasCache, mPaint);

        for (Sprite sprite : mSpriteQueue) {
            sprite.draw(mCanvasCache, mPaint);
        }

        mPlayerLeft.draw(mCanvasCache, mPaint);
        mPlayerRight.draw(mCanvasCache, mPaint);

        String text = getResources().getString(R.string.score) + ": " + (int) mScore;
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(0.05F * mViewWidth);
        mCanvasCache.drawText(text, 0.75F * mViewWidth, 0.05F * mViewHeight, mPaint);
    }

    @Override
    protected void timingLogic() {
        if (!isGameOver) {
            mScore += 10F * mSpeedY / mViewHeight;
        }

        float speedX = 2.6F * mViewWidth / mFPS;
        if (mPlayerLeftClicked) {
            if (mPlayerLeft.isTouchRight()) {
                mPlayerLeft.setSpeedX(-speedX);
            }
        }
        if (mPlayerRightClicked) {
            if (mPlayerRight.isTouchLeft()) {
                mPlayerRight.setSpeedX(speedX);
            }
        }

        createEnemy();
        //        createParticleEffect();
        removeDestroyedSprite();
        if (DEBUG) {
            Log.d(TAG, "timingLogic: mSpriteQueue.size() = " + mSpriteQueue.size());
        }

        if (mOnGameListener != null && !isGameOver) {
            mOnGameListener.onScoreChange((int) mScore);
        }

        if (checkGameOver()) {
            if (mOnGameListener != null && !isGameOver) {
                mOnGameListener.onGameOver((int) mScore);
            }
            isGameOver = true;
        }

    }

    private boolean checkGameOver() {
        for (Sprite sprite : mSpriteQueue) {
            if (sprite.isDestroyed()) {
                continue;
            }
            if (sprite instanceof EnemyLeft && SpriteControl.isCollidedByTwoSprite(sprite, mPlayerLeft)) {
                return true;
            }
            if (sprite instanceof EnemyRight && SpriteControl.isCollidedByTwoSprite(sprite, mPlayerRight)) {
                return true;
            }
        }
        return false;
    }

    private synchronized void createEnemy() {
        float enemyW = 0.1F * mViewWidth;
        float enemyH = enemyW * 0.618F;
        mSpeedY = 0.5F * mViewHeight / mFPS + mFrame / 1000;
        createEnemyL(enemyW, enemyH, mSpeedY);
        createEnemyR(enemyW, enemyH, mSpeedY);
    }

    private float enemyL = 0;
    private float enemyLMax = 10;

    private synchronized void createEnemyL(float enemyW, float enemyH, float speedY) {
        if (enemyL < enemyLMax) {
            enemyL += mSpeedY;
            return;
        }
        enemyL = 0;
        enemyLMax = (float) (mViewHeight * (0.4F + 0.3F * Math.random()));

        EnemyLeft enemy = (EnemyLeft) new EnemyLeft.Build()
                .setW(enemyW)
                .setH(enemyH)
                .setX(0.5F * mViewWidth - enemyW)
                .setY(-enemyH)
                .setSpeedY(speedY)
                .build();
        mSpriteQueue.add(enemy);
    }

    private float enemyR = 0;
    private float enemyRMax = 0;

    private synchronized void createEnemyR(float enemyW, float enemyH, float speedY) {
        if (enemyR < enemyRMax) {
            enemyR += mSpeedY;
            return;
        }
        enemyR = 0;
        enemyRMax = (float) (mViewHeight * (0.4F + 0.3F * Math.random()));

        EnemyRight enemy = (EnemyRight) new EnemyRight.Build()
                .setW(enemyW)
                .setH(enemyH)
                .setX(0.5F * mViewWidth)
                .setY(-enemyH)
                .setSpeedY(speedY)
                .build();
        mSpriteQueue.add(enemy);
    }

    private void removeDestroyedSprite() {

        for (Sprite sprite : mSpriteQueue) {
            if (sprite.getY() >= mViewHeight) {
                sprite.destroy();
            }
            if (sprite.isDestroyed()) {
                mSpriteQueue.remove(sprite);
            }
        }
    }

    private void removeDestroyedSprite2() {
        Iterator<Sprite> iterator = mSpriteQueue.iterator();
        while (iterator.hasNext()) {
            Sprite sprite = iterator.next();
            if (sprite.getY() > mViewHeight) {
                sprite.destroy();
            }
            if (sprite.isDestroyed()) {
                iterator.remove();
            }
        }
    }

    /**
     * 创建粒子效果
     */
    private synchronized void createParticleEffect() {
        if (mPlayerLeft == null || mPlayerRight == null) {
            return;
        }
        float wh = 0.01F * mViewWidth;
        ParticleEffect particleEffectLeft = (ParticleEffect) new ParticleEffect.Build()
                .setW(wh)
                .setH(wh)
                .centerTo(mPlayerLeft.getCenterX(), mPlayerLeft.getCenterY())
                .build();

        ParticleEffect particleEffectRight = (ParticleEffect) new ParticleEffect.Build()
                .setW(wh)
                .setH(wh)
                .centerTo(mPlayerRight.getCenterX(), mPlayerRight.getCenterY())
                .build();
        mSpriteQueue.add(particleEffectLeft);
        mSpriteQueue.add(particleEffectRight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
         *涉及多点触摸
         */
        int action = event.getActionMasked();

        if (action == MotionEvent.ACTION_DOWN) {
            checkupMotionEventDown(event);
        } else if (action == MotionEvent.ACTION_POINTER_DOWN) {
            checkupMotionEventDown(event);
        } else if (action == MotionEvent.ACTION_POINTER_UP) {
            checkupMotionEventUp(event);
        } else if (action == MotionEvent.ACTION_UP) {
            checkupMotionEventUp(event);
        }
        return true;
    }

    private void checkupMotionEventDown(MotionEvent event) {
        int index = event.getActionIndex();

        if (event.getX(index) <= 0.5F * mViewWidth) {
            //click left
            mPlayerLeftClicked = true;
        } else {
            //click right
            mPlayerRightClicked = true;

        }
    }

    private void checkupMotionEventUp(MotionEvent event) {
        int index = event.getActionIndex();

        if (event.getX(index) <= 0.5F * mViewWidth) {
            //click left
            mPlayerLeftClicked = false;
        } else {
            //click right
            mPlayerRightClicked = false;

        }
    }

    private synchronized List<Sprite> getEnemyLeftList() {
        List<Sprite> list = new ArrayList<>();
        for (Sprite sprite : mSpriteQueue) {
            if (sprite instanceof EnemyLeft && !sprite.isDestroyed()) {
                list.add(sprite);
            }
        }
        return list;
    }

    private synchronized List<Sprite> getEnemyRightList() {
        List<Sprite> list = new ArrayList<>();
        for (Sprite sprite : mSpriteQueue) {
            if (sprite instanceof EnemyRight && !sprite.isDestroyed()) {
                list.add(sprite);
            }
        }
        return list;
    }

    private OnGameListener mOnGameListener;

    public void setOnGameListener(OnGameListener onGameListener) {
        mOnGameListener = onGameListener;
    }
}
