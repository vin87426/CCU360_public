package ccu.ccu360;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class ContactAdapter extends BaseAdapter {
    private LayoutInflater myInflater;
    private ArrayList<ContactData> list_contact;
    public int temp = -1;
    public int temp_id = -1;
    private Context context;


    public ContactAdapter(Context context, ArrayList<ContactData> list_contact){
        myInflater = LayoutInflater.from(context);
        this.context = context;
        this.list_contact = list_contact;
    }

    final class ViewHolder {
        public ImageButton btn_del;
        public TextView txtName;
        public RadioButton txtPhone;
    }

    @Override
    public int getCount() {
        return list_contact.size();
    }

    @Override
    public ContactData getItem(int arg0) {
        return list_contact.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return list_contact.indexOf(getItem(position));
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
            convertView = myInflater.inflate(R.layout.listview_style1, null);
            holder = new ViewHolder();
            holder.btn_del = (ImageButton) convertView.findViewById(R.id.del_icon);
            holder.txtName = (TextView) convertView.findViewById(R.id.name);
            holder.txtPhone =  (RadioButton) convertView.findViewById(R.id.radio);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ContactData contact = (ContactData)getItem(position);
        holder.txtName.setText(contact.getName());
        holder.txtPhone.setText(contact.getPhone());
        holder.txtPhone.setId(position);

        final ItemDAO itemdao = new ItemDAO(context.getApplicationContext());

        holder.btn_del.setTag(position);
        holder.btn_del.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //TODO Auto-generated method stub

                Map<String,String> data = itemdao.getBaseData();
                if(data.get("Contact").equals(String.valueOf(getItem(position).getId()))){
                    Toast.makeText(context, "不能刪除選取中的聯絡人", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(context, holder.txtName.getText().toString()+"已刪除", Toast.LENGTH_SHORT).show();
                itemdao.contact_delete(getItem(position).getId());
                list_contact.remove(position);
                //list_contact = itemdao.getAll_contact();
                //ContactAdapter adapter1 = new ContactAdapter(context, list_contact);
                notifyDataSetChanged();
                //Toast.makeText(context, getItem(position).getId()+","+holder.txtName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.txtPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//如果是选中状态
                    if (temp != -1) {//temp不为-1，说明已经进行过点击事件
                        RadioButton tempButton = (RadioButton) parent.findViewById(temp_id);
                        if(tempButton != null)
                            tempButton.setChecked(false);//取到上一次点击的RadioButton，并设置为未选中状态
                    }
                    temp = list_contact.get(position).getId();//将temp重新赋值，记录下本次点击的RadioButton
                    temp_id = buttonView.getId();
                    //Log.d("AAAAID",String.valueOf(temp));
                    itemdao.setting_contact_update(temp);
                    holder.txtPhone.setChecked(true);

                }
            }
        });

        if (list_contact.get(position).getId() == temp) {
            holder.txtPhone.setChecked(true);//将本次点击的RadioButton设置为选中状态
        } else {
            holder.txtPhone.setChecked(false);
        }
        return convertView;
    }

}
