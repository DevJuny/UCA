package jp.dev.juny.android.uca.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jun on 2014/06/25.
 */
public class UcaDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ucadatabase.db";

    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_UCA_BASE = "UCA_BASE";

    private static final String COL_DATE = "date";

    private static final String COL_MAYO_SCORE = "mayo_score";

    private static final String COL_MEMO = "memo";

    private static final String COL_TOILET_COUNT = "toilet_count";

    private static final String COL_BLOOD_STATUS = "blood_status";

    private static final String COL_DOCTOR_OPINION = "doctor_opinion";

    private static final String SQL_CREATE_TABLE = ""
            + "create table " + TABLE_UCA_BASE
            + "( "
            + COL_DATE + " TEXT PRIMARY KEY,  "
            + COL_MAYO_SCORE + " INTEGER, "
            + COL_MEMO + " TEXT, "
            + COL_TOILET_COUNT + " INTEGER, "
            + COL_BLOOD_STATUS + " INTEGER, "
            + COL_DOCTOR_OPINION + " INTEGER "
            + ");";

    private static final String SQL_DROP_TABLE = ""
            + "drop table if exists " + TABLE_UCA_BASE + ";";

    public UcaDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    // TODO 引数はBean化して、ビルダーパターンで生成させる
    public Long insert(final String date, final Integer mayoScore, final String memo, final Integer toiletCount, final Integer bloodStatus, final Integer doctorOpinion){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DATE, date);
        values.put(COL_MAYO_SCORE, mayoScore);
        values.put(COL_MEMO, memo);
        values.put(COL_TOILET_COUNT, toiletCount);
        values.put(COL_BLOOD_STATUS, bloodStatus);
        values.put(COL_DOCTOR_OPINION, doctorOpinion);
        Long result = -1l;
        if(db != null){
            delete(date);
            result = db.insert(TABLE_UCA_BASE, null, values);
        }
        return result;
    }

    public void delete(final String date){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_UCA_BASE, COL_DATE + " = " + date, null);
    }

    // TODO 戻り値はBean化する
    public List<Object> findByDate(final String date){
        SQLiteDatabase db = getReadableDatabase();
        String query = ""
                + "select * from " + TABLE_UCA_BASE
                + " where " + COL_DATE + " = " + date + ";";

        Cursor c = db.rawQuery(query, null);

        final List<Object> list = new ArrayList<Object>();
        if(c.moveToFirst()){
            list.add(c.getString(0));
            list.add(c.getInt(1));
            list.add(c.getString(2));
            list.add(c.getInt(3));
            list.add(c.getInt(4));
            list.add(c.getInt(5));
        }

        return list;
    }
}
