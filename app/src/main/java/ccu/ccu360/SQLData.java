package ccu.ccu360;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLData extends SQLiteOpenHelper {
    private final static String DATABASE_NAME="CCU360.db"; //Database
    private final static String TABLE_NAME_CONTACT = "contact";    //DataTable
    private final static String TABLE_NAME_SETTING = "setting";    //DataTable
    private static final int VERSION = 40;
    private static SQLiteDatabase database;

    public SQLData(Context context, String name, SQLiteDatabase.CursorFactory factory,
                   int version) {
        super(context, name, factory, version);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItemDAO.T1);
        db.execSQL(ItemDAO.T2);
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = ft.parse("1998-04-26");
        } catch (ParseException e) {
            System.out.println("Unparseable using " + ft);
        }



        cv.put(ItemDAO.T1_NAME, "NULL");
        cv.put(ItemDAO.T1_CALLTO, -1);
        cv.put(ItemDAO.T1_STUDENT_ID, "NULL");
        cv.put(ItemDAO.T1_DEPARTMENT, "NULL");
        cv.put(ItemDAO.T1_CELLPHONE, "NULL");
        cv.put(ItemDAO.T1_CONTACT, -1);
        cv.put(ItemDAO.T1_BLOOD, -1);
        cv.put(ItemDAO.T1_GENDER, -1);
        cv.put(ItemDAO.T1_BROADCAST, -1);
        cv.put(ItemDAO.T1_BIRTH, ft.format(date));

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME_CONTACT, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL1 = "DROP TABLE IF EXISTS " + TABLE_NAME_CONTACT;
        String SQL2 = "DROP TABLE IF EXISTS " + TABLE_NAME_SETTING;
        db.execSQL(SQL1);
        db.execSQL(SQL2);
        db.execSQL(ItemDAO.T1);
        db.execSQL(ItemDAO.T2);

    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new SQLData(context, DATABASE_NAME,
                    null, VERSION).getWritableDatabase();
        }

        return database;
    }
}
