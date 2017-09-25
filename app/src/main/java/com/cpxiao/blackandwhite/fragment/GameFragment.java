package com.cpxiao.blackandwhite.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.blackandwhite.OnGameListener;
import com.cpxiao.blackandwhite.mode.extra.Extra;
import com.cpxiao.blackandwhite.views.GameView;
import com.cpxiao.gamelib.fragment.BaseFragment;
import com.cpxiao.zads.utils.ThreadUtils;

/**
 * @author cpxiao on 2017/09/20.
 */

public class GameFragment extends BaseFragment {
    private GameView mGameView;

    public static GameFragment newInstance(Bundle bundle) {
        GameFragment fragment = new GameFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mGameView = (GameView) view.findViewById(R.id.game_view);
        mGameView.setOnGameListener(new OnGameListener() {
            @Override
            public void onScoreChange(int score) {
                Context context = getHoldingActivity();
                updateBestScore(context, score);
            }

            @Override
            public void onGameOver(final int score) {
                ThreadUtils.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        showDialog(getHoldingActivity(), score);
                    }
                });

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_game;
    }

    private int getBestScore(Context context) {
        return PreferencesUtils.getInt(context, Extra.Key.BEST_SCORE, 0);
    }

    private void updateBestScore(Context context, int score) {
        int bestScore = getBestScore(context);
        if (score > bestScore) {
            bestScore = score;
            PreferencesUtils.putInt(context, Extra.Key.BEST_SCORE, bestScore);
        }
    }

    private void showDialog(Context context, int score) {
        int bestScore = getBestScore(context);
        updateBestScore(context, score);

        String msg = getString(R.string.score) + ": " + score + "\n"
                + getString(R.string.best_score) + ": " + bestScore;
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.game_over)
                .setMessage(msg)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mGameView.reset();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        removeFragment();
                    }
                })
                .create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
