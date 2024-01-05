package com.woooapp.meeting.impl.utils;

import android.os.Handler;

public class IncrementingTimer {

    private Handler handler;
    private Runnable runnable;
    private int elapsedTimeInSeconds;
    private TimerCallback timerCallback;

    public IncrementingTimer(TimerCallback timerCallback) {
        this.timerCallback = timerCallback;
        this.elapsedTimeInSeconds = 0;

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                elapsedTimeInSeconds++;
                updateUI();
                handler.postDelayed(this, 1000); // Repeat every 1000 milliseconds (1 second)
            }
        };
    }

    public void startTimer() {
        handler.post(runnable);
    }

    public void stopTimer() {
        handler.removeCallbacks(runnable);
    }

    private void updateUI() {
        int hours = elapsedTimeInSeconds / 3600;
        int minutes = (elapsedTimeInSeconds % 3600) / 60;
        int seconds = elapsedTimeInSeconds % 60;

        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerCallback.onTimerTick(formattedTime);
    }

    public interface TimerCallback {
        void onTimerTick(String formattedTime);
    }
}

