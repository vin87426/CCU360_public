package ccu.ccu360;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class info {
    private Activity activity;
    private TextView title1, title2, title3, title4;
    private TextView text1, text2, text3, text4;
    private boolean flag1 = false;
    private boolean flag2 = false;
    private boolean flag3 = false;
    private boolean flag4 = false;
    //private boolean flag5 = false;
    public info (Activity _activity, View view) {
        this.activity = _activity;
        title1 = (TextView)view.findViewById(R.id.info_subtitle1);
        text1 = (TextView)view.findViewById(R.id.info_subtext1);
        title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag1){
                    text1.setVisibility(View.GONE);
                    flag1 = false;
                }else{
                    text1.setVisibility(View.VISIBLE);
                    flag1 = true;
                }
            }
        });

        title2 = (TextView)view.findViewById(R.id.info_subtitle2);
        text2 = (TextView)view.findViewById(R.id.info_subtext2);
        title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag2){
                    text2.setVisibility(View.GONE);
                    flag2 = false;
                }else{
                    text2.setVisibility(View.VISIBLE);
                    flag2 = true;
                }
            }
        });

        title3 = (TextView)view.findViewById(R.id.info_subtitle3);
        text3 = (TextView)view.findViewById(R.id.info_subtext3);
        title3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag3){
                    text3.setVisibility(View.GONE);
                    flag3 = false;
                }else{
                    text3.setVisibility(View.VISIBLE);
                    flag3 = true;
                }
            }
        });

        title4 = (TextView)view.findViewById(R.id.info_subtitle4);
        text4 = (TextView)view.findViewById(R.id.info_subtext4);
        title4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag4){
                    text4.setVisibility(View.GONE);
                    flag4 = false;
                }else{
                    text4.setVisibility(View.VISIBLE);
                    flag4 = true;
                }
            }
        });
    }

}
