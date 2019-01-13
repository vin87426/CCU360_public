package ccu.ccu360;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity  extends AppCompatActivity {
    private List<PageViewer.PageView> pageList;


    private ImageView fab;
    private Animation loading_animation0;
    private Animation loading_animation1;
    private  View main_view;
    private PageViewer pageviewer;

    private String[] str = {"基本資料(簡訊使用)","自訂聯絡人","通報對象設定","推播通知","關於CCU360"};
    private ListView set_listview;
    private TextView info_subtitle;
    private TextView info_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ItemDAO itemdao = new ItemDAO(getApplicationContext());
        itemdao.qaq();

        pageviewer = new PageViewer(MainActivity.this);
        pageList = pageviewer.get_pagelist();

        fab = (ImageView) findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view)
                    {
                        main_view = view;
                        loading_animation0 = AnimationUtils.loadAnimation(MainActivity.this,R.anim.circular_slide_out1);
                        loading_animation1 = AnimationUtils.loadAnimation(MainActivity.this,R.anim.circular_slide_out2);
                        fab.startAnimation(loading_animation0);
                        new Handler().postDelayed(change_to_warnning1,200);
                    }

                }
        );



    }
    private Runnable change_to_warnning1 = new Runnable(){
        @Override
        public void run() {
            fab.startAnimation(loading_animation1);
            new Handler().postDelayed(change_to_warnning2,400);

        }
    };
    private Runnable change_to_warnning2 = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //deal with UI
            switch (msg.what){
                case 1:
                    FrameLayout main_frame = (FrameLayout) findViewById(R.id.main_frame);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(MainActivity.this, main_view, "transition");
                    int revealX = (int) (main_frame.getX() + main_frame.getWidth() / 2);
                    int revealY = (int) (main_frame.getY() + main_frame.getHeight() / 2);

                    Intent intent = new Intent(MainActivity.this, warning.class);
                    intent.putExtra(warning.EXTRA_CIRCULAR_REVEAL_X, revealX);
                    intent.putExtra(warning.EXTRA_CIRCULAR_REVEAL_Y, revealY);

                    ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
                    new Handler().postDelayed(fin_activity,370);
                    break;

            }
        }
    };
    private Runnable fin_activity = new Runnable(){
        @Override
        public void run() {
            finish();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK ) { // 攔截返回鍵
            if(pageviewer.mViewPager.getCurrentItem() == 3){
                if(pageviewer.setting_return()){
                    return false;
                }
            }

            new AlertDialog.Builder(this)
                    .setMessage("是否要退出？")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();

        }

        return false;
    }

}
