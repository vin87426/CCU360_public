package ccu.ccu360;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.util.*;
class NewsCrawler {
    private List<Map<String, String>> listmaps = new ArrayList<>();
    private String url = "http://studaffbh.ccu.edu.tw/files/40-1002-38-1.php";
    private ListView listview;
    private String get_url;
    private String get_context;
    private String get_title;
    private Activity activity;
    private View view;

    public NewsCrawler( Activity _activity, View _view){
        this.activity = _activity;
        this.view = _view;

        //run
        if(isNetworkAvailable(activity)){
            Runnable crawling_runnable = new Runnable() {
                @Override
                public void run() {
                    //run
                    crawling();
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            };
            new Thread(crawling_runnable).start();
        }else{
            Log.d("Network_state","No Network!");
        }
    }

    private Runnable get_news_context_runnable = new Runnable(){
        @Override
        public void run(){
            //run
            get_context = getcontext(get_url);
            Message msg = new Message();
            msg.what = 2;
            handler.sendMessage(msg);
        }
    };



    public void crawling() {
        //<title,context(url)>;
        //new tread for http request
        // if (isNetworkAvailable(NewsCrawler.this))
        run();
        //page2
        url = "http://studaffbh.ccu.edu.tw/files/40-1002-38-2.php";
        run();
        //  else
        // System.out.println("No network QQ");

    }

    public String getcontext(String url2) {
        String context = "";
        try {
            Document doc2 = Jsoup.connect(url2).get();
            context = doc2.getElementsByClass("ptcontent clearfix floatholder").text();
            //output
            System.out.println("context: " + url2);
            //finish

        } catch (IOException io) {
            System.out.println("nothing");
        }

        return context;
    }

    //new tread for http request

        public void run() {
            //run
            // String url = "http://studaffbh.ccu.edu.tw/files/40-1002-38-1.php";

            try {
                Document doc = Jsoup.connect(url).get();
                Elements elements = doc.select("table[class=baseTB list_TIDY]>tbody>tr");
                for (Element element : elements) {
                    String time = element.getElementsByClass("date").text();
                    if (time.equals("")) continue;
                    String title = element.getElementsByClass("ptname").text();
                    Elements links = element.select("a[href]");
                    String link = links.attr("abs:href");

                    //output
                    /*System.out.println("date: " + time);
                    System.out.println("title: " + title);
                    System.out.println("link: " + link);*/

                    Map<String, String> tmpnews = new HashMap<>();

                    tmpnews.put("title", title);
                    tmpnews.put("url", link);
                    listmaps.add(tmpnews);
                }
                //finish
            } catch (IOException io) {
                System.out.println("nothing");
            }
        }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //deal with UI
            switch (msg.what) {
                case 0:
                    listview = (ListView) view.findViewById(R.id.listview_news);
                    SimpleAdapter simpleAdapter = new SimpleAdapter(activity, listmaps, android.R.layout.simple_list_item_activated_1, new String[]{"title"}, new int[]{android.R.id.text1});
                    listview.setAdapter(simpleAdapter);
                    simpleAdapter.notifyDataSetChanged();
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            get_url = listmaps.get((int) id).get("url");
                            get_title = listmaps.get((int) id).get("title");
                            new Thread(get_news_context_runnable).start();
                        }
                    });
                    break;
                case 2:
                    new AlertDialog.Builder(activity)
                        .setTitle(get_title)
                        .setMessage(get_context)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                    break;
            }
        }
    };

    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null)
            return false;
        else{
            //get all NetworkInfo
            NetworkInfo[] networkInfo = cm.getAllNetworkInfo();
            if(networkInfo !=null && networkInfo.length>0)
            {
                for(int i=0;i<networkInfo.length;i++)
                    if(networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                        return true; //available connect
            }
        }
        return false;
    }

}