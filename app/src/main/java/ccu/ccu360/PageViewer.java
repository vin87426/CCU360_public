package ccu.ccu360;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


class PageViewer extends  MainActivity{
    private List<PageView> pageList;
    public ViewPager mViewPager;
    private Activity activity;
    private Setting setting;

    public PageViewer(Activity _activity) {
        activity = _activity;

        pageList = new ArrayList<>();
        pageList.add(new PageOneView(activity));
        pageList.add(new PageTwoView(activity));
        pageList.add(new PageThreeView(activity));
        pageList.add(new PageFourView(activity));


        mViewPager = (ViewPager) activity.findViewById(R.id.view_pager);
        mViewPager.setAdapter(new SamplePagerAdapter());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                               @Override
                                               public void onPageScrolled(int i, float v, int i1) {
                                                   int cur_page = mViewPager.getCurrentItem();
                                                   ImageView img = (ImageView) activity.findViewById(R.id.bar_1);
                                                   img.setImageResource(R.drawable.ic_bar_1);
                                                   img = (ImageView) activity.findViewById(R.id.bar_2);
                                                   img.setImageResource(R.drawable.ic_bar_2);
                                                   img = (ImageView) activity.findViewById(R.id.bar_3);
                                                   img.setImageResource(R.drawable.ic_bar_3);
                                                   img = (ImageView) activity.findViewById(R.id.bar_4);
                                                   img.setImageResource(R.drawable.ic_bar_4);
                                                   switch (cur_page) {
                                                       case 0:
                                                           img = (ImageView) activity.findViewById(R.id.bar_1);
                                                           img.setImageResource(R.drawable.ic_bar_1_1);
                                                           break;
                                                       case 1:
                                                           img = (ImageView) activity.findViewById(R.id.bar_2);
                                                           img.setImageResource(R.drawable.ic_bar_2_1);
                                                           break;
                                                       case 2:
                                                           img = (ImageView) activity.findViewById(R.id.bar_3);
                                                           img.setImageResource(R.drawable.ic_bar_3_1);
                                                           break;
                                                       case 3:
                                                           img = (ImageView) activity.findViewById(R.id.bar_4);
                                                           img.setImageResource(R.drawable.ic_bar_4_1);
                                                           break;
                                                   }
                                               }

                                               @Override
                                               public void onPageSelected(int i) {

                                               }

                                               @Override
                                               public void onPageScrollStateChanged(int i) {

                                               }
                                           }
        );
        mViewPager.setCurrentItem(2);
        tool_bar_btn_change();
    }

    public void tool_bar_btn_change() {
        ImageButton info_btn = (ImageButton) activity.findViewById(R.id.bar_1);
        ImageButton news_btn = (ImageButton) activity.findViewById(R.id.bar_2);
        ImageButton map_btn = (ImageButton) activity.findViewById(R.id.bar_3);
        ImageButton setting_btn = (ImageButton) activity.findViewById(R.id.bar_4);
        info_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        news_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });
        map_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mViewPager.setCurrentItem(3);
            }
        });
    }

    public List<PageViewer.PageView> get_pagelist() {
        return pageList;
    }

    private class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            container.addView(pageList.get(position));
            return pageList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    public class PageView extends FrameLayout {
        public PageView(Context context) {
            super(context);
        }
    }

    public class PageOneView extends PageView {
        public PageOneView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.activity_info, null);
            new info(activity,view);
            addView(view);
        }
    }

    public class PageTwoView extends PageView {
        public PageTwoView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.activity_news, null);
            new NewsCrawler(activity,view);
            addView(view);
        }
    }

    public class PageThreeView extends PageView {
        public PageThreeView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.activity_map, null);
            new map(activity,view);
            addView(view);


        }
    }

    public class PageFourView extends PageView {
        public PageFourView(Context context) {
            super(context);
            View view = LayoutInflater.from(context).inflate(R.layout.activity_setting, null);
            setting = new Setting(activity,view);
            addView(view);
        }
    }

    public boolean setting_return(){
        if(setting.set_page == 5){
            return false;
        }else{
            setting.return_set_index();
            return true;
        }
    }



}
