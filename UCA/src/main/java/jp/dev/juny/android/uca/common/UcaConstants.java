package jp.dev.juny.android.uca.common;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * UcaConstants
 *
 * UCAの定数クラス
 *
 * Created by jun on 2014/06/25.
 */
public class UcaConstants {

    /**
     * プリファレンスの名称
     */
    public static final String PREFERENCES_NAME = "UCA_PREF";

    /**
     * プリファレンスキーの日付
     */
    public static final String PREFERENCES_ITEM_DATE = "DATE";

    /**
     * プリファレンスキーのMayoスコア
     */
    public static final String PREFERENCES_ITEM_MAYO_SCORE = "MAYO_SCORE";

    /**
     * プリファレンスキーのトイレ回数
     */
    public static final String PREFERENCES_ITEM_TOILET_COUNT = "TOILET_COUNT";

    /**
     * プリファレンスキーの出血状態
     */
    public static final String PREFERENCES_ITEM_TOILET_BLOOD = "TOILET_BLOOD";

    /**
     * プリファレンスキーの医師所見
     */
    public static final String PREFERENCES_ITEM_DOCTOR_OPINION = "DOCTOR_OPINION";

    /**
     * プリファレンスキーのUC以前の排便回数
     */
    public static final String PREFERENCES_ITEM_BEFORE_UC_TOILET_COUNT = "BEFORE_UC_TOILET_COUNT";

    /**
     * メインフォントのフォントファイル名
     */
    public static final String FONT_FILE_NAME_MAIN = "mplus-1m-light.ttf";

    /**
     * UC以前の排便回数設定ダイアログ用の最小値
     */
    public static final int BEFORE_UC_TOILET_COUNT_MIN  = 0;

    /**
     * UC以前の排便回数設定ダイアログ用の最大値
     */
    public static final int BEFORE_UC_TOILET_COUNT_MAX  = 10;

    /**
     * CalendarからViewへ渡す選択日付のIntentキーワード
     */
    public static final String INTENT_KEY_CAL_SELECT_DATE = "CAL_SELECT_DATE";

    /**
     * 画面表示用の日付フォーマット
     */
    public static final SimpleDateFormat FORMATTER_DISP = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * データ保持用の日付フォーマット
     */
    public static final SimpleDateFormat FORMATTER_DB = new SimpleDateFormat("yyyyMMdd");

    /**
     * 出血ステータス：なし
     */
    private static final Integer BLOOD_NONE = 0;

    /**
     * 出血ステータス：ちょっと
     */
    private static final Integer BLOOD_FEW = 1;

    /**
     * 出血ステータス：よくある
     */
    private static final Integer BLOOD_HALF = 2;

    /**
     * 出血ステータス：いつも
     */
    private static final Integer BLOOD_ALLMOST = 3;

    /**
     * 出血ステータスと文言の紐付け用Map1
     */
    private static final Map<Integer, String> BLOOD_CODE_MAP = new HashMap<Integer, String>();
    static {
        BLOOD_CODE_MAP.put(BLOOD_NONE, "出血は無し");
        BLOOD_CODE_MAP.put(BLOOD_FEW, "たまに少量出血");
        BLOOD_CODE_MAP.put(BLOOD_HALF, "ほぼ毎回の出血");
        BLOOD_CODE_MAP.put(BLOOD_ALLMOST, "毎回、ほぼ血液");
    }

    /**
     * 出血ステータスと文言の紐付け用Map2
     */
    private static final Map<String, Integer> BLOOD_STAT_MAP = new HashMap<String, Integer>();
    static {
        BLOOD_STAT_MAP.put("出血は無し", BLOOD_NONE);
        BLOOD_STAT_MAP.put("たまに少量出血", BLOOD_FEW);
        BLOOD_STAT_MAP.put("ほぼ毎回の出血", BLOOD_HALF);
        BLOOD_STAT_MAP.put("毎回、ほぼ血液", BLOOD_ALLMOST);
    }

    // TODO NONEとかFEWとかおかしいので変えたい
    /**
     * 医師所見：寛解
     */
    private static final Integer OPINION_NONE = 0;

    /**
     * 医師所見：軽度
     */
    private static final Integer OPINION_FEW = 1;

    /**
     * 医師所見：中度
     */
    private static final Integer OPINION_HALF = 2;

    /**
     * 医師所見：重度
     */
    private static final Integer OPINION_ALLMOST = 3;

    /**
     * 医師所見と文言の紐付け用Map1
     */
    private static final Map<Integer, String> OPINION_CODE_MAP = new HashMap<Integer, String>();
    static {
        OPINION_CODE_MAP.put(OPINION_NONE, "完全な寛解");
        OPINION_CODE_MAP.put(OPINION_FEW, "活動が軽度");
        OPINION_CODE_MAP.put(OPINION_HALF, "活動が中等度");
        OPINION_CODE_MAP.put(OPINION_ALLMOST, "活動が高度");
    }

    /**
     * 医師所見と文言の紐付け用Map2
     */
    private static final Map<String, Integer> OPINION_STAT_MAP = new HashMap<String, Integer>();
    static {
        OPINION_STAT_MAP.put("完全な寛解", OPINION_NONE);
        OPINION_STAT_MAP.put("活動が軽度", OPINION_FEW);
        OPINION_STAT_MAP.put("活動が中等度", OPINION_HALF);
        OPINION_STAT_MAP.put("活動が高度", OPINION_ALLMOST);
    }

    /**
     * 出血状態の文言からコード値を取得
     * @param bloodStat
     * @return
     */
    public static Integer getBloodCode(final String bloodStat) {
        return BLOOD_STAT_MAP.get(bloodStat);
    }

    /**
     * 出血状態のコード値から文言を取得
     * @param bloodCode
     * @return
     */
    public static String getBloodStat(final Integer bloodCode) {
        return BLOOD_CODE_MAP.get(bloodCode);
    }

    /**
     * 医師所見の文言からコード値を取得
     * @param opinionStat
     * @return
     */
    public static Integer getOpinionCode(final String opinionStat) {
        return OPINION_STAT_MAP.get(opinionStat);
    }

    /**
     * 医師所見のコード値から文言を取得
     * @param opinionCode
     * @return
     */
    public static String getOpinionStat(final Integer opinionCode) {
        return OPINION_CODE_MAP.get(opinionCode);
    }
}
