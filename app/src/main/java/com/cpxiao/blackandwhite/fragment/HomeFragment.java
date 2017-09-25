package com.cpxiao.blackandwhite.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.cpxiao.R;
import com.cpxiao.androidutils.library.utils.PreferencesUtils;
import com.cpxiao.blackandwhite.mode.extra.Extra;
import com.cpxiao.gamelib.fragment.BaseFragment;

/**
 * @author cpxiao on 2017/09/20.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    public static HomeFragment newInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        Button play = (Button) view.findViewById(R.id.play);
        Button bestScore = (Button) view.findViewById(R.id.best_score);
        Button settings = (Button) view.findViewById(R.id.settings);
        Button quit = (Button) view.findViewById(R.id.quit);

        play.setOnClickListener(this);
        bestScore.setOnClickListener(this);
        settings.setOnClickListener(this);
        quit.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Context context = getHoldingActivity();
        if (id == R.id.play) {
            addFragment(GameFragment.newInstance(null));
        } else if (id == R.id.best_score) {
            int bestScore = PreferencesUtils.getInt(context, Extra.Key.BEST_SCORE, 0);
            showBestScoreDialog(context, bestScore);
        } else if (id == R.id.settings) {

        } else if (id == R.id.quit) {
            removeFragment();
        }
    }

    private void showBestScoreDialog(Context context, int bestScore) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.best_score)
                .setMessage("" + bestScore)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
