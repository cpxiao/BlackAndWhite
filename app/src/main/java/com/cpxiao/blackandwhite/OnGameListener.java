package com.cpxiao.blackandwhite;

/**
 * @author cpxiao on 2017/09/23.
 */

public interface OnGameListener {
    void onScoreChange(int score);

    void onGameOver(int score);
}
