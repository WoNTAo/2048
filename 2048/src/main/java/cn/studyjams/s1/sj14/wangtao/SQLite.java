package cn.studyjams.s1.sj14.wangtao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 27357 on 2015/11/19.
 */
public class SQLite extends SQLiteOpenHelper {

    public SQLite(Context context){
        super(context,"Grade.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE grade(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Score VARCHAR(20)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
