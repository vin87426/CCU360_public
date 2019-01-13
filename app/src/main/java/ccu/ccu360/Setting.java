package ccu.ccu360;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import static android.R.layout.simple_expandable_list_item_1;
import static android.R.layout.simple_list_item_single_choice;


class Setting {
    private Activity activity;
    public int set_page=5;
    private String[] str = {"基本資料(簡訊使用)","自訂聯絡人","通報對象設定","關於CCU360"};
    private String[] str2 = {"學校","警消"};
    private ArrayList<String> Basedata;
    private ArrayList<ContactData> contactDataList;
    private ListView set_listview;
    private LinearLayout list0;
    private LinearLayout list1_a;
    private ListView list1;
    private ListView list2;
    private LinearLayout list3;
    private ScrollView list4;
    private TextView set_subtitle;
    private TextView set_title;
    private CheckBox news_check;
    private Button btn_newcontact;
    private Button btn_base;
    private ContactAdapter adapter1;

    private TextView in0,in1,in2,in3,in4,in5,in6;
    private String name;
    private String student_id;
    private String department;
    private String cellphone;
    private String blood;
    private String gender;
    private String birth;

    private Map<String,String> user_data;
    @SuppressLint("WrongViewCast")
    public Setting(Activity _activity, final View view){
        this.activity = _activity;

        final ItemDAO itemdao = new ItemDAO(activity.getApplicationContext());
        user_data = itemdao.getBaseData();

        list0 = (LinearLayout) view.findViewById(R.id.set_list0);
        list1_a = (LinearLayout) view.findViewById(R.id.set_list1_a);
        list1 = (ListView) view.findViewById(R.id.set_list1);
        list2 = (ListView) view.findViewById(R.id.set_list2);
        list3 = (LinearLayout) view.findViewById(R.id.set_list3);
        list4 = (ScrollView) view.findViewById(R.id.set_list4);



        set_subtitle = (TextView) view.findViewById(R.id.set_subtitle);
        set_title = (TextView) view.findViewById(R.id.set_title);
        set_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(set_page<5&&set_page>=0) {
                    set_page = 5;
                    set_listview.setVisibility(View.VISIBLE);
                    set_subtitle.setVisibility(View.GONE);
                    list0.setVisibility(View.GONE);
                    list1.setVisibility(View.GONE);
                    list1_a.setVisibility(View.GONE);
                    list2.setVisibility(View.GONE);
                    list3.setVisibility(View.GONE);
                    list4.setVisibility(View.GONE);
                }
            }
        });

        in0 = (TextView) view.findViewById(R.id.R0);
        in1 = (TextView) view.findViewById(R.id.R1);
        in2 = (TextView) view.findViewById(R.id.R2);
        in3 = (TextView) view.findViewById(R.id.R3);
        in4 = (TextView) view.findViewById(R.id.R4);
        in5 = (TextView) view.findViewById(R.id.R5);
        in6 = (TextView) view.findViewById(R.id.R6);

        /*Basedata = itemdao.getBaseData();
        if(Basedata.size()<=0) {

        } else{
            in0.setText(Basedata.get(0));
            in1.setText(Basedata.get(1));
            in2.setText(Basedata.get(2));
            in3.setText(Basedata.get(3));
            in4.setText(Basedata.get(4));
            in5.setText(Basedata.get(5));
            in6.setText(Basedata.get(6));
        //}*/


        btn_base = (Button) view.findViewById(R.id.btn_base);
        if( !user_data.get("Name").equals("NO")){
            in0.setText(user_data.get("Name"));
        }
        if( !user_data.get("Gender").equals("-1")){
            in1.setText(user_data.get("Gender"));
        }
        if( !user_data.get("Cellphone").equals("NO")){
            in2.setText(user_data.get("Cellphone"));
        }
        if( !user_data.get("Department").equals("NO")){
            in3.setText(user_data.get("Department"));
        }
        if( !user_data.get("Student_ID").equals("NO")){
            in4.setText(user_data.get("Student_ID"));
        }
        if( !user_data.get("Birth").equals("1000-01-01")){
            in5.setText(user_data.get("Birth"));
        }

        if( !user_data.get("Blood").equals("-1")){
            in6.setText(user_data.get("Blood"));
        }


        btn_base.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                name = in0.getText().toString();
                gender = in1.getText().toString();
                cellphone = in2.getText().toString();
                department = in3.getText().toString();
                student_id = in4.getText().toString();
                birth = in5.getText().toString();
                blood = in6.getText().toString();

                itemdao.setting_base_update(name, student_id, department, cellphone, blood, gender, birth);
                Toast.makeText(activity, "保存成功!", Toast.LENGTH_SHORT).show();
            }
        });

        set_listview = (ListView) view.findViewById(R.id.listview_setting);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, simple_expandable_list_item_1, str);
        set_listview.setAdapter(adapter);
        set_listview.setOnItemClickListener(onClickListView);       //指定事件 Method

        contactDataList = itemdao.getAll_contact();
        adapter1 = new ContactAdapter(activity, contactDataList);
        list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list1.setAdapter(adapter1);
        if(user_data.get("Contact") != "-1")
            adapter1.temp = Integer.valueOf(user_data.get("Contact"));
        btn_newcontact = (Button)view.findViewById(R.id.btn_newcontact);
        btn_newcontact.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final View item = LayoutInflater.from(activity).inflate(R.layout.dialog_style1, null);
                new AlertDialog.Builder(activity)
                        .setTitle("新增聯絡人")
                        .setView(item)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editName = (EditText) item.findViewById(R.id.contact_name);
                                EditText editPhone = (EditText) item.findViewById(R.id.contact_phone);
                                String name = editName.getText().toString();
                                String phone = editPhone.getText().toString();
                                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(phone)) {
                                    Toast.makeText(activity, "請輸入完整資料", Toast.LENGTH_LONG).show();
                                }else {
                                    itemdao.contact_insert(name,phone);
                                    contactDataList = itemdao.getAll_contact();
                                    adapter1 = new ContactAdapter(activity, contactDataList);
                                    list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                    list1.setAdapter(adapter1);
                                    int lastid = contactDataList.get(contactDataList.size()-1).getId();
                                    Toast.makeText(activity, name + "新增成功", Toast.LENGTH_SHORT).show();
                                    if(user_data.get("Contact").equals("-1")){
                                        //Log.d("AAAAA","BBBB");
                                        itemdao.setting_calling_update(adapter1.getItem(0).getId());
                                        adapter1.temp = adapter1.getItem(0).getId();
                                        adapter1.temp_id = list1.getAdapter().getView(0,null,list1).getId();
                                    }
                                    //RadioButton rbtn = (RadioButton) list1.getAdapter().getView(0,null, (ViewGroup) list1).findViewById(R.id.radio);
                                    //Log.d("AAAAA",rbtn.toString());
                                    //rbtn.setChecked(true);

                                    user_data = itemdao.getBaseData();
                                    if(user_data.get("Contact") != "-1")
                                        adapter1.temp = Integer.valueOf(user_data.get("Contact"));
                                        for (int i = 0; i < list1.getAdapter().getCount(); i++){
                                            if(adapter1.getItem(0).getId() ==  adapter1.temp){
                                                adapter1.temp_id = list1.getAdapter().getView(i,null,list1).getId();
                                                break;
                                            }

                                        }
                                }

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });



        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(activity, simple_list_item_single_choice, str2);
        list2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list2.setAdapter(adapter2);
        if(user_data.get("Callto").equals("0"))
            list2.setItemChecked(0,true);
        else
            list2.setItemChecked(1,true);
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                if(position==1) {
                    itemdao.setting_calling_update(1);
                }else {
                    itemdao.setting_calling_update(0);
                }
                Toast.makeText(activity,  "設定通報對象為"+list2.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                user_data = itemdao.getBaseData();
            }
        });

        news_check = (CheckBox)view.findViewById(R.id.news_check);
        if( user_data.get("Broadcast").equals("1"))
            news_check.setChecked(true);
        else
            news_check.setChecked(false);
        news_check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //設定CheckBox勾選狀態
                if (news_check.isChecked()) {//如果是选中状态
                    news_check.setChecked(true);
                    itemdao.setting_broadcast_update(1);
                    Toast.makeText(activity, "接收推播", Toast.LENGTH_SHORT).show();
                }else {
                    news_check.setChecked(false);
                    itemdao.setting_broadcast_update(0);
                    Toast.makeText(activity, "取消推播", Toast.LENGTH_SHORT).show();
                }
                user_data = itemdao.getBaseData();
            }
        });



    }
    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(position){
                case 0:
                    set_page=0;
                    set_subtitle.setText(">"+str[position]);
                    set_subtitle.setVisibility(View.VISIBLE);
                    list0.setVisibility(View.VISIBLE);
                    set_listview.setVisibility(View.GONE);
                    break;
                case 1:
                    set_page=1;
                    set_subtitle.setText(">"+str[position]);
                    set_subtitle.setVisibility(View.VISIBLE);
                    list1.setVisibility(View.VISIBLE);
                    list1_a.setVisibility(View.VISIBLE);
                    set_listview.setVisibility(View.GONE);
                    break;
                case 2:
                    set_page=2;
                    set_subtitle.setText(">"+str[position]);
                    set_subtitle.setVisibility(View.VISIBLE);
                    list2.setVisibility(View.VISIBLE);
                    set_listview.setVisibility(View.GONE);
                    break;
                /*case 3:
                    set_page=3;
                    set_subtitle.setText(">"+str[position]);
                    set_subtitle.setVisibility(View.VISIBLE);
                    list3.setVisibility(View.VISIBLE);
                    set_listview.setVisibility(View.GONE);
                    break;*/
                case 3:
                    set_page=4;
                    set_subtitle.setText(">"+str[position]);
                    set_subtitle.setVisibility(View.VISIBLE);
                    list4.setVisibility(View.VISIBLE);
                    set_listview.setVisibility(View.GONE);
                    break;
                default:
                    set_listview.setVisibility(View.VISIBLE);
                    set_subtitle.setVisibility(View.GONE);
                    list0.setVisibility(View.GONE);
                    list1.setVisibility(View.GONE);
                    list1_a.setVisibility(View.GONE);
                    list2.setVisibility(View.GONE);
                    list3.setVisibility(View.GONE);
                    list4.setVisibility(View.GONE);
                    break;
            }

        }
    };

    public void return_set_index(){
        if(set_page<5&&set_page>=0) {
            set_page = 5;
            set_listview.setVisibility(View.VISIBLE);
            set_subtitle.setVisibility(View.GONE);
            list0.setVisibility(View.GONE);
            list1.setVisibility(View.GONE);
            list1_a.setVisibility(View.GONE);
            list2.setVisibility(View.GONE);
            list3.setVisibility(View.GONE);
            list4.setVisibility(View.GONE);
        }
    }


}
