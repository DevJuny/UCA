package jp.dev.juny.android.uca.common;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * UcaUtils
 *
 * UCAの共通ユーティリティクラス
 *
 * Created by jun on 2014/06/25.
 */
public class UcaUtils {
    // TODO 定数クラスに定義
    private static SimpleDateFormat formatterDb = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat formatterDisp = new SimpleDateFormat("yyyy/MM/dd");

    public static String formatDateDisp(String dbDate) {
        // TODO ここら辺のロジックは要検討
        Date date = formatterDb.parse(dbDate, new ParsePosition(0));

        return formatterDisp.format(date);
    }

    public static String formatDateDb(String dispDate) {
        // TODO ここら辺のロジックは要検討
        Date date = formatterDisp.parse(dispDate, new ParsePosition(0));

        return formatterDb.format(date);
    }


}
