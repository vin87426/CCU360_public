package ccu.ccu360;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.graphics.*;
import android.widget.Toast;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class warning extends AppCompatActivity implements View.OnTouchListener {

    private int progress_bar_status;
    private ProgressBar progress_bar;
    private boolean progress_bar_check;
    private GestureDetector mGestureDetector;
    private ConstraintLayout warning_layout;
    private Animation slideout_animation;
    private ImageView call_start;
    private Button btn_cancel;
    private Button btn_other;

    private int revealX;
    private int revealY;
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    private ImageView imageview;
    private ImageView imageview1;
    private ImageView imageview2;
    private Animation arrow_animation0;
    private Animation arrow_animation1;
    private Animation arrow_animation2;
    private Animation arrow_animation3;
    private Animation arrow_animation4;
    private Animation arrow_animation5;
    private int sendkey = 0;

    private int change_activity_state = 0;
    private SecurityAlert securityAlert;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);

        securityAlert = new SecurityAlert(this);
        securityAlert.check_perrmission();
        btn_cancel = (Button) findViewById(R.id.cancel_btn);

        final Intent intent = getIntent();

        warning_layout = findViewById(R.id.warning_layout);
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            warning_layout.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);


            ViewTreeObserver viewTreeObserver = warning_layout .getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        warning_layout .getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            warning_layout .setVisibility(View.VISIBLE);
        }

        call_start= (ImageView) findViewById(R.id.call_start);
        progress_bar = (ProgressBar) findViewById(R.id.warning_progressbar);
        progress_bar.setProgress(0);
        call_start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {//当前状态
                    case MotionEvent.ACTION_DOWN:
                        ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.ic_warning_1));
                        progress_bar_status = 0;
                        progress_bar_check = true;
                        new Handler().postDelayed(animation_progressbar,10);
                        break;
                    case MotionEvent.ACTION_UP:
                        ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.ic_warning));
                        progress_bar_check = false;
                        progress_bar_status = 0;
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        mGestureDetector = new GestureDetector(new gesturelistener());
        FrameLayout warning_frameLayout = (FrameLayout) findViewById(R.id.warning_frameLayout);
        warning_frameLayout.setOnTouchListener(this);
        warning_frameLayout.setFocusable(true);
        warning_frameLayout.setClickable(true);
        warning_frameLayout.setLongClickable(true);

        slideout_animation = AnimationUtils.loadAnimation(this,R.anim.slide_out);

        btn_other = (Button) findViewById(R.id.btn_other);
        btn_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warning_layout.startAnimation(slideout_animation);
                final Locker locker = new Locker();
                locker.run();
            }
        });

        new Handler().postDelayed(animation_runnable,50);
        imageview = (ImageView) findViewById(R.id.arrowAnimation);
        imageview1 = (ImageView) findViewById(R.id.arrowAnimation1);
        imageview2 = (ImageView) findViewById(R.id.arrowAnimation2);

        arrow_animation0 = AnimationUtils.loadAnimation(this,R.anim.arrow0);
        arrow_animation1 = AnimationUtils.loadAnimation(this,R.anim.arrow0);
        arrow_animation2 = AnimationUtils.loadAnimation(this,R.anim.arrow0);
        arrow_animation3 = AnimationUtils.loadAnimation(this,R.anim.arrow1);
        arrow_animation4 = AnimationUtils.loadAnimation(this,R.anim.arrow1);
        arrow_animation5 = AnimationUtils.loadAnimation(this,R.anim.arrow1);

        arrow_animation0.setFillAfter(true);
        arrow_animation1.setFillAfter(true);
        arrow_animation2.setFillAfter(true);
        arrow_animation3.setFillAfter(true);
        arrow_animation4.setFillAfter(true);
        arrow_animation5.setFillAfter(true);

    }

    private Runnable animation_runnable = new Runnable(){
        @Override
        public void run() {
            sendkey++;
            if(sendkey == 7)sendkey = 1;
            Message msg = new Message();
            msg.what = sendkey;
            aHandler.sendMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler aHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    imageview.startAnimation(arrow_animation3);
                    new Handler().postDelayed(animation_runnable,200);
                    break;
                case 2:
                    imageview1.startAnimation(arrow_animation4);
                    new Handler().postDelayed(animation_runnable,200);
                    break;
                case 3:
                    imageview2.startAnimation(arrow_animation5);
                    new Handler().postDelayed(animation_runnable,1000);
                    break;
                case 4:
                    imageview.startAnimation(arrow_animation0);
                    new Handler().postDelayed(animation_runnable,200);
                    break;
                case 5:
                    imageview1.startAnimation(arrow_animation1);
                    new Handler().postDelayed(animation_runnable,200);
                    break;
                case 6:
                    imageview2.startAnimation(arrow_animation2);
                    new Handler().postDelayed(animation_runnable,400);
                    break;
                default:
                    break;
            }
        }
    };

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(warning_layout.getWidth(), warning_layout.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(warning_layout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            warning_layout.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            finish();
        }
    }

    public boolean onTouch(View v, MotionEvent event) {

        return mGestureDetector.onTouchEvent(event);
    }

    private Runnable animation_progressbar = new Runnable(){
        @Override
        public void run() {
            if(progress_bar_check) progress_bar_status+=1;
            else {
                progress_bar_status = 0;
                return;
            }
            if(progress_bar_status >= 100) {
                progress_bar_status = 101;
                Message msg = new Message();
                msg.what = 2;
                mHandler.sendMessage(msg);
            }else{
                new Handler().postDelayed(animation_progressbar,10);
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        }
    };

    protected void show_notification() {
        securityAlert.check_perrmission();
        String message = securityAlert.calling();
        TextView warn_info = (TextView) findViewById(R.id.warning_information);
        warn_info.setVisibility(View.GONE);
        TextView end_info = (TextView) findViewById(R.id.end_information);
        ImageView end_text = (ImageView) findViewById(R.id.end_text);
        TextView local_info = (TextView) findViewById(R.id.local_info);
        LinearLayout layout_msg = (LinearLayout) findViewById(R.id.layout_msg);

        local_info.setText(message);
        btn_cancel.setVisibility(View.GONE);
        call_start.setVisibility(View.GONE);
        layout_msg.setVisibility(View.VISIBLE);
        end_info.setVisibility(View.VISIBLE);
        end_text.setVisibility(View.VISIBLE);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if(progress_bar_check){
                        progress_bar.setProgress(progress_bar_status);
                    }
                    else
                        progress_bar.setProgress(0);
                    break;

                //讀條成功
                case 2:
                    progress_bar.setProgress(progress_bar_status);
                    show_notification();
                    call_start.setOnTouchListener(null);
                    progress_bar.setProgress(0);
                    call_start.setImageResource(R.drawable.ic_warning_2);
                    call_start.setScaleType(ImageButton.ScaleType.CENTER_INSIDE);

                    break;
                default:
                    break;
            }
        }
    };

    private class gesturelistener implements GestureDetector.OnGestureListener{

        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
        }

        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            return false;
        }

        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // TODO Auto-generated method stub
            if(e2.getY() - e1.getY() > 100 && velocityY > 100){
                warning_layout.startAnimation(slideout_animation);
                final Locker locker = new Locker();
                locker.run();
            }
            return false;
        }

    }

    private Runnable change_to_main = new Runnable(){
        @Override
        public void run() {
            startActivity(new Intent(warning.this,MainActivity.class));
            overridePendingTransition( R.anim.slide_in, R.anim.slide_in );
            finish();
        }
    };

    private class Locker {
        private Lock lock = new ReentrantLock();// 锁对象
        public void run() {
            // TODO 线程输出方法
            lock.lock();// 得到锁
            try {
                if(change_activity_state == 0){
                    new Handler().postDelayed(change_to_main,300);
                }
                change_activity_state = 1;
            } finally {
                lock.unlock();// 释放锁
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵

                warning_layout.startAnimation(slideout_animation);
                final Locker locker = new Locker();
                locker.run();

            return false;
        }


        return false;
    }
}
