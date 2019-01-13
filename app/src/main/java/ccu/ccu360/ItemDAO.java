package ccu.ccu360;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 資料功能類別
public class ItemDAO {

    // 表格名稱
    private final static String TABLE_NAME_CONTACT = "contact";    //DataTable
    private final static String TABLE_NAME_SETTING = "setting";    //DataTable

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";

    // 其它表格欄位名稱
    public static final String T1_NAME = "Name";
    public static final String T1_STUDENT_ID = "Student_ID";
    public static final String T1_DEPARTMENT = "Department";
    public static final String T1_CELLPHONE = "Cellphone";
    public static final String T1_BLOOD = "Blood";
    public static final String T1_GENDER = "Gender";
    public static final String T1_BIRTH = "Birth";
    public static final String T1_CONTACT = "Contact";
    public static final String T1_CALLTO = "Callto";
    public static final String T1_BROADCAST = "Broadcast";

    private static final String T2_NAME = "Name";
    private static final String T2_CELLPHONE = "Cellphone";


    // 使用上面宣告的變數建立表格的SQL指令
    public static final String T1 =
            "CREATE TABLE " + TABLE_NAME_SETTING + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    T1_NAME + " TEXT NOT NULL, " +
                    T1_STUDENT_ID + " TEXT NOT NULL, " +
                    T1_DEPARTMENT + " TEXT, " +
                    T1_CELLPHONE + " TEXT NOT NULL, " +
                    T1_BLOOD + " TEXT NOT NULL, " +
                    T1_GENDER + " TEXT NOT NULL, " +
                    T1_BIRTH + " TEXT NOT NULL,"+
                    T1_CONTACT + " INTEGER, " +
                    T1_CALLTO + " INTEGER NOT NULL, " +
                    T1_BROADCAST + " INTEGER NOT NULL)" ;

    public static final String T2 =
            "CREATE TABLE " + TABLE_NAME_CONTACT + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    T2_NAME + " TEXT NOT NULL, " +
                    T2_CELLPHONE + " TEXT NOT NULL)";

    // 資料庫物件
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public ItemDAO(Context context) {
        db = SQLData.getDatabase(context);
        check_data_isset();
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }


    public boolean setting_base_update(String name, String student_id, String department, String cellphone, String blood, String gender,  String birth){

        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");

        try {
            if(!birth.equals(""))
                date = ft.parse(birth);
        } catch (ParseException e) {
            System.out.println("Unparseable using " + ft);
        }

        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        String where = KEY_ID + "=" + "1";

        cv.put(ItemDAO.T1_NAME, name);
        cv.put(ItemDAO.T1_STUDENT_ID, student_id);
        cv.put(ItemDAO.T1_DEPARTMENT, department);
        cv.put(ItemDAO.T1_CELLPHONE, cellphone);
        cv.put(ItemDAO.T1_BLOOD, blood);
        cv.put(ItemDAO.T1_GENDER, gender);
        cv.put(ItemDAO.T1_BIRTH, ft.format(date));
        //SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        //cv.put(ItemDAO.T1_BIRTH,  ft.format(birth));
        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.update(TABLE_NAME_SETTING, cv, where , null);

        // 設定編號
        // 回傳結果
        return true;
    }

    // 讀取所有記事資料
    public Map<String,String> getBaseData () {
        Map<String,String> result = new HashMap<>();
        Cursor cursor = db.query(TABLE_NAME_SETTING, null, null, null, null, null, null, null);
        cursor.moveToNext();
        //result.add(cursor.getString(0));
        result.put(cursor.getColumnName(0),String.valueOf(cursor.getString(0)));
        result.put(cursor.getColumnName(1),String.valueOf(cursor.getString(1)));
        result.put(cursor.getColumnName(2),String.valueOf(cursor.getString(2)));
        result.put(cursor.getColumnName(3),String.valueOf(cursor.getString(3)));
        result.put(cursor.getColumnName(4),String.valueOf(cursor.getString(4)));
        result.put(cursor.getColumnName(5),String.valueOf(cursor.getString(5)));
        result.put(cursor.getColumnName(6),String.valueOf(cursor.getString(6)));
        result.put(cursor.getColumnName(7),String.valueOf(cursor.getString(7)));
        result.put(cursor.getColumnName(8),String.valueOf(cursor.getString(8)));
        result.put(cursor.getColumnName(9),String.valueOf(cursor.getString(9)));
        result.put(cursor.getColumnName(10),String.valueOf(cursor.getString(10)));
        cursor.close();
        return result;
    }

    public boolean setting_contact_update(Integer contact){
        ContentValues cv = new ContentValues();
        String where = KEY_ID + "=" + "1";
        cv.put(ItemDAO.T1_CONTACT, contact);
        long id = db.update(TABLE_NAME_SETTING, cv, where , null);
        return true;
    }

    public boolean setting_calling_update(Integer calling){
        ContentValues cv = new ContentValues();
        String where = KEY_ID + "=" + "1";
        cv.put(ItemDAO.T1_CALLTO, calling);
        long id = db.update(TABLE_NAME_SETTING, cv, where , null);
        return true;
    }

    public boolean setting_broadcast_update(Integer calling){
        ContentValues cv = new ContentValues();
        String where = KEY_ID + "=" + "1";
        cv.put(ItemDAO.T1_BROADCAST, calling);
        long id = db.update(TABLE_NAME_SETTING, cv, where , null);
        return true;
    }

    // 新增參數指定的物件
    public boolean contact_insert(String name, String phone){
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(T2_NAME, name);
        cv.put(T2_CELLPHONE, phone);

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME_CONTACT, null, cv);

        // 設定編號
        // 回傳結果
        return true;
    }

    // 修改參數指定的物件
    public boolean contact_update(int id, String name, String phone) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
//        cv.put(LONGITUDE_COLUMN, item.getLongitude());
 //       cv.put(LASTMODIFY_COLUMN, item.getLastModify());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + id;

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME_CONTACT, cv, where, null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean contact_delete(int id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME_CONTACT, where , null) > 0;
    }
    // 刪除參數指定編號的資料
    public boolean contact_delete_all(){
        return db.delete(TABLE_NAME_CONTACT, null , null) > 0;
    }

    // 讀取所有記事資料
    public ArrayList<ContactData> getAll_contact () {
        ArrayList<ContactData> result = new ArrayList<ContactData>();
        Cursor cursor = db.query(TABLE_NAME_CONTACT, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(new ContactData(Integer.valueOf((int) cursor.getLong(0)),
                    cursor.getString(1),cursor.getString(2)));
            //temp.add(String.valueOf(cursor.getLong(0)));
            //temp.add(cursor.getString(1));
            //temp.add(cursor.getString(2));
            //Log.d(cursor.getString(1),cursor.getString(2));
            //result.add(temp);
        }
        cursor.close();
        return result;
    }
    public String getContact_Phone (String id) {
        String phpne = "";
        ArrayList<ContactData> result = new ArrayList<ContactData>();
        String where = KEY_ID + "=" + id;
        Cursor cursor = db.query(TABLE_NAME_CONTACT, null, where, null, null, null, null, null);
        while (cursor.moveToNext()) {
            phpne = cursor.getString(2);
        }
        cursor.close();
        return  phpne;
    }
    // 建立範例資料


    public void qaq() {
        check_data_isset();
        //setting_base_update("胡竣賓","405410043","資工系","0985334105","1", "1", "1998-04-26");
        //setting_calling_update(1);
        //setting_broadcast_update(1);
        Map<String,String> temp = getBaseData();

        Log.d("AAAA",temp.get("_id"));
        Log.d("AAAA",temp.get("Name"));
        Log.d("AAAA",temp.get("Student_ID"));
        Log.d("AAAA",temp.get("Department"));
        Log.d("AAAA",temp.get("Cellphone"));
        Log.d("AAAA",temp.get("Blood"));
        Log.d("AAAA",temp.get("Gender"));
        Log.d("AAAA",temp.get("Birth"));
        Log.d("AAAA",temp.get("Contact"));
        Log.d("AAAA",temp.get("Callto"));
        Log.d("AAAA",temp.get("Broadcast"));



       /* Log.d("AA","in");

        contact_insert("AAA","BBB");
        contact_insert("AAA","BBB");
        contact_insert("AAA","BBB");
        ArrayList<ContactData> temp = getAll_contact();

        Log.d("AA",String.valueOf(temp.size()));

        for (int i = 0; i < temp.size(); i++ ){
            boolean x = contact_delete(temp.get(i).getId());
            Log.d("AA",String.valueOf(x));
            Log.d("AA","CC");
        }
        temp = getAll_contact();
        Log.d("AAAAA",String.valueOf(temp.size()));
        //getBaseData();
        */
    }

    public void check_data_isset(){
        Cursor cursor = db.query(TABLE_NAME_SETTING, null, null, null, null, null, null, null);
        if(cursor.getCount() == 1) {
            return;
        }

        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");


        try {
            date = ft.parse("1000-01-01");
        } catch (ParseException e) {
            System.out.println("Unparseable using " + ft);
        }

        cv.put(ItemDAO.T1_NAME, "NO");
        cv.put(ItemDAO.T1_STUDENT_ID, "NO");
        cv.put(ItemDAO.T1_DEPARTMENT, "NO");
        cv.put(ItemDAO.T1_CELLPHONE, "NO");
        cv.put(ItemDAO.T1_BLOOD, -1);
        cv.put(ItemDAO.T1_GENDER, -1);
        cv.put(ItemDAO.T1_BIRTH, ft.format(date));
        cv.put(ItemDAO.T1_CONTACT, -1);
        cv.put(ItemDAO.T1_CALLTO, 0);
        cv.put(ItemDAO.T1_BROADCAST, 1);


        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        db.insert(TABLE_NAME_SETTING, null, cv);
    }

}