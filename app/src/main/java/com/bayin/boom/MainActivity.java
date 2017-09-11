package com.bayin.boom;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

public class MainActivity extends AppCompatActivity {

    private static final int BREAK = -1;//handler 终止跑马灯tag，开大门


    private AnimalPanView animalView;
    private View imgLeftDoor;
    private View imgRightDoor;
    private LeftAnimalPanView leftPan;
    private RightAnimalPanView rightPan;
    private boolean isOpen = false;
    private int width;
    private BallTextView tvBallText;
    private long overtime = 4000;//跑马灯计时
    private MyCount myCount;
    private boolean marqueenEnd = false;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BREAK:
                    //终止跑马灯
//                    leftPan.stop();
//                    rightPan.stop();
                    marqueenEnd = true;
                    rightAnimator.end();
                    leftAnimator.end();
                    myCount.cancel();
                    myCount = null;
                    //开大门
                    openDoor();
                    break;
            }
        }
    };
    private ObjectAnimator rightAnimator;
    private ObjectAnimator leftAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        tvBallText = (BallTextView) findViewById(R.id.tv_result);

        imgLeftDoor = findViewById(R.id.img_left_door);
        leftPan = (LeftAnimalPanView) findViewById(R.id.leftView);

        imgRightDoor = findViewById(R.id.img_right_door);
        rightPan = (RightAnimalPanView) findViewById(R.id.rightView);
        rightPan.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                leftPan.startAnimation();
            }
        });
        leftPan.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                rightPan.startAnimation();
            }
        });

        initAnimation();

        findViewById(R.id.bt_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    //关门
                    closeDoor();
                } else {
                    myCount = new MyCount(overtime, 1000);
                    myCount.start();
                    //跑马灯
                    marqueenEnd = false;
                    rightAnimator.start();
                }
                isOpen = !isOpen;
            }
        });
    }

    private void initAnimation() {
        rightAnimator = ObjectAnimator.ofInt(rightPan, "index", 0, 6).setDuration(600);
        rightAnimator.setInterpolator(new LinearInterpolator());
        leftAnimator = ObjectAnimator.ofInt(leftPan, "index", 5, -1).setDuration(600);
        leftAnimator.setInterpolator(new LinearInterpolator());
        rightAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!marqueenEnd) {
                    rightPan.stop();
                    leftAnimator.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        leftAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!marqueenEnd) {
                    leftPan.stop();
                    rightAnimator.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 开门
     */
    private void openDoor() {
        imgLeftDoor.animate().translationX(-width / 2).setDuration(2000);
        leftPan.animate().translationX(-width / 2).setDuration(2000);
        imgRightDoor.animate().translationX(width / 2).setDuration(2000);
        rightPan.animate().translationX(width / 2).setDuration(2000);
        tvBallText.setBallText(RandomUtils.getRandom(100));
    }

    private void closeDoor() {
        //关门
        imgLeftDoor.animate().translationX(0).setDuration(200);
        leftPan.animate().translationX(0).setDuration(200);
        imgRightDoor.animate().translationX(0).setDuration(200);
        rightPan.animate().translationX(0).setDuration(200);
    }

    private class MyCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            mHandler.sendEmptyMessage(BREAK);
        }
    }
}