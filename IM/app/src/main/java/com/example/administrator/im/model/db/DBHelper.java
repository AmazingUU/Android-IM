package com.example.administrator.im.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.im.model.dao.ContactTable;
import com.example.administrator.im.model.dao.InviteTable;

/**
 * Created by Administrator on 2017/1/27.
 */

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建联系人的表
        sqLiteDatabase.execSQL(ContactTable.CREATE_TAB);

        //创建邀请信息的表
        sqLiteDatabase.execSQL(InviteTable.CREATE_TAB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
