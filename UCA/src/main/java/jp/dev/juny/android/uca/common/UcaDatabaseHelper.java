package jp.dev.juny.android.uca.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * UcaDatabaseHelper
 *
 * UCAのデータベースアクセスクラス
 *
 * Created by jun on 2014/06/25.
 */
public class UcaDatabaseHelper extends SQLiteOpenHelper {

    // データベース名
    private static final String DATABASE_NAME = "ucadatabase.db";

    // データベースバージョン
    private static final int DATABASE_VERSION = 1;

    // 基本テーブル名
    private static final String TABLE_UCA_BASE = "UCA_BASE";

    /* カラム名 */
    // 日付（PK）
    private static final String COL_DATE = "date";
    // Mayoスコア
    private static final String COL_MAYO_SCORE = "mayo_score";
    // メモ
    private static final String COL_MEMO = "memo";
    // トイレ回数
    private static final String COL_TOILET_COUNT = "toilet_count";
    // 出血状態
    private static final String COL_BLOOD_STATUS = "blood_status";
    // 医師所見
    private static final String COL_DOCTOR_OPINION = "doctor_opinion";

    /* SQL文 */
    // テーブル作成
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

    // テーブル削除
    private static final String SQL_DROP_TABLE = ""
            + "drop table if exists " + TABLE_UCA_BASE + ";";

    /**
     * コンストラクタ
     *
     * 基底クラスに処理はお任せ
     *
     * @param context
     */
    public UcaDatabaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * onCreate
     *
     * 基底クラスからの呼び出し <br/>
     * @param db
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * onUpgrade
     *
     * 基底クラスからの呼び出し <br/>
     * @param db
     */
    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    /**
     * データ登録処理
     *
     * DeleteInsertにより既存レコードも上書きで登録する
     *
     * @param date
     * @param mayoScore
     * @param memo
     * @param toiletCount
     * @param bloodStatus
     * @param doctorOpinion
     * @return
     */
    // TODO 引数はBean化して、ビルダーパターン生成にしたい
    public Long insert(final String date,
                       final Integer mayoScore,
                       final String memo,
                       final Integer toiletCount,
                       final Integer bloodStatus,
                       final Integer doctorOpinion) {
        // 書き込み可でDBインスタンス取得
        final SQLiteDatabase db = getWritableDatabase();
        final ContentValues values = new ContentValues();
        values.put(COL_DATE, date);
        values.put(COL_MAYO_SCORE, mayoScore);
        values.put(COL_MEMO, memo);
        values.put(COL_TOILET_COUNT, toiletCount);
        values.put(COL_BLOOD_STATUS, bloodStatus);
        values.put(COL_DOCTOR_OPINION, doctorOpinion);
        Long result = -1l;
        if (db != null) {
            // Deleteしてから登録を行う
            delete(date);
            // 登録
            result = db.insert(TABLE_UCA_BASE, null, values);
        }
        return result;
    }

    /**
     * プライマリキーをキーにレコード削除を行う
     * @param date
     */
    public void delete(final String date) {
        // 書き込み可でDBインスタンス取得
        final SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_UCA_BASE, COL_DATE + " = " + date, null);
    }

    /**
     * プライマリキーをキーにレコード取得を行う
     * @param date
     * @return
     */
    // TODO 戻り値はBean化したい
    public List<Object> findByDate(final String date) {
        // ReadOnlyでDBインスタンス取得
        final SQLiteDatabase db = getReadableDatabase();

        // select文
        final String query = ""
                + "select * from " + TABLE_UCA_BASE
                + " where " + COL_DATE + " = " + date + ";";

        // データ取得
        final Cursor c = db.rawQuery(query, null);

        // カーソルから各値を取得
        final List<Object> list = new ArrayList<Object>();
        if (c.moveToFirst()) {
            // TODO マジックナンバーやめる
            list.add(c.getString(0));
            list.add(c.getInt(1));
            list.add(c.getString(2));
            list.add(c.getInt(3));
            list.add(c.getInt(4));
            list.add(c.getInt(5));
        }

        return list;
    }

    /**
     * 全レコード取得を行う
     * @return
     */
    // TODO 戻り値はBean化したい
    public List<List<Object>> findAll() {
        // ReadOnlyでDBインスタンス取得
        final SQLiteDatabase db = getReadableDatabase();

        // select文
        final String query = ""
                + "select " +
                COL_DATE + ", " + COL_MAYO_SCORE + " from " + TABLE_UCA_BASE
                + " order by " + COL_DATE + ";";
        // データ取得
        final Cursor c = db.rawQuery(query, null);

        // カーソルから各値を取得
        final List<List<Object>> retList = new ArrayList<List<Object>>();
        while (c.moveToNext()) {
            // TODO マジックナンバーやめる
            final List<Object> innerList = new ArrayList<Object>();
            innerList.add(c.getString(0));
            innerList.add(c.getInt(1));
            retList.add(innerList);
        }

        return retList;
    }
}
