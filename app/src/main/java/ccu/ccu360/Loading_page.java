package ccu.ccu360;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

    public class Loading_page extends AppCompatActivity {
        private ImageView imageview;

        private Animation loading_animation0;
        private Animation loading_animation1;
        private Animation loading_animation2;
        private Animation loading_animation3;
        private Animation loading_animation4;


    private int sendkey = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);
        new Handler().postDelayed(loading_runnable,3000);
        new Handler().postDelayed(animation_runnable,400);
        imageview = (ImageView) findViewById(R.id.loadingAnimation);
        loading_animation0 = AnimationUtils.loadAnimation(this,R.anim.loading0);
        loading_animation1 = AnimationUtils.loadAnimation(this,R.anim.loading1);
        loading_animation2 = AnimationUtils.loadAnimation(this,R.anim.loading2);
        loading_animation3 = AnimationUtils.loadAnimation(this,R.anim.loading3);
        loading_animation4 = AnimationUtils.loadAnimation(this,R.anim.loading4);

        loading_animation0.setFillAfter(true);
        loading_animation1.setFillAfter(true);
        loading_animation2.setFillAfter(true);
        loading_animation3.setFillAfter(true);
        loading_animation4.setFillAfter(true);

        imageview.startAnimation(loading_animation0);


    }

    private Runnable loading_runnable = new Runnable(){
        @Override
        public void run() {
            startActivity(new Intent(Loading_page.this,warning.class));
            finish();
        }
    };
    private Runnable animation_runnable = new Runnable(){
        @Override
        public void run() {
            sendkey++;
            Message msg = new Message();
            msg.what = sendkey;
            mHandler.sendMessage(msg);

        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    imageview.startAnimation(loading_animation1);
                    new Handler().postDelayed(animation_runnable,350);
                    break;
                case 2:
                    imageview.startAnimation(loading_animation2);
                    new Handler().postDelayed(animation_runnable,300);
                    break;
                case 3:
                    imageview.startAnimation(loading_animation3);
                    new Handler().postDelayed(animation_runnable,250);
                    break;
                case 4:
                    imageview.startAnimation(loading_animation4);
                    sendkey-=2;
                    new Handler().postDelayed(animation_runnable,250);
                    break;
                default:

                    break;
            }
        }
    };


}
