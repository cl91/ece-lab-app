package com.uoa.ece.p4p.ecelabmanager.utility;

import android.os.Handler;

import com.dd.processbutton.ProcessButton;

import java.util.Random;

public class ProgressGenerator {

    public interface OnCompleteListener {
        public void onComplete();
    }

    private OnCompleteListener mListener;
    private int mProgress;

    public ProgressGenerator(OnCompleteListener listener) {
        mListener = listener;
    }

    public void start(final ProcessButton button) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgress = button.getProgress();
                if (mProgress == -1) {
                    handler.removeCallbacksAndMessages(null);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    button.setProgress(0);
                    mListener.onComplete();
                } else if (mProgress < 100) {
                    button.setProgress(mProgress + 10);
                    handler.postDelayed(this, generateDelay());
                } else {
                    mListener.onComplete();
                }
            }
        }, generateDelay());
    }

    private Random random = new Random();

    private int generateDelay() {
        return random.nextInt(1000);
    }
}