package jp.dev.juny.android.uca.common;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jun on 2014/06/25.
 */
public class UcaConstants {

    public static final String PREFERENCES_NAME = "UCA_PREF";

    public static final String PREFERENCES_ITEM_DATE = "DATE";

    public static final String PREFERENCES_ITEM_MAYO_SCORE = "MAYO_SCORE";

    public static final String PREFERENCES_ITEM_TOILET_COUNT = "TOILET_COUNT";

    public static final String PREFERENCES_ITEM_TOILET_BLOOD = "TOILET_BLOOD";

    public static final String PREFERENCES_ITEM_DOCTOR_OPINION = "DOCTOR_OPINION";

    public static final String PREFERENCES_ITEM_BEFORE_UC_TOILET_COUNT = "BEFORE_UC_TOILET_COUNT";



    public static final SimpleDateFormat FORMATTER_DISP = new SimpleDateFormat("yyyy/MM/dd");

    public static final SimpleDateFormat FORMATTER_DB = new SimpleDateFormat("yyyyMMdd");


    private static final Integer BLOOD_NONE = 0;

    private static final Integer BLOOD_FEW = 1;

    private static final Integer BLOOD_HALF = 2;

    private static final Integer BLOOD_ALLMOST = 3;

    private static final Map<Integer, String> BLOOD_CODE_MAP= new HashMap<Integer, String>();
    static{
        BLOOD_CODE_MAP.put(BLOOD_NONE, "出血は無し");
        BLOOD_CODE_MAP.put(BLOOD_FEW, "たまに少量出血");
        BLOOD_CODE_MAP.put(BLOOD_HALF, "ほぼ毎回の出血");
        BLOOD_CODE_MAP.put(BLOOD_ALLMOST, "毎回、ほぼ血液");
    }

    private static final Map<String, Integer> BLOOD_STAT_MAP= new HashMap<String, Integer>();
    static{
        BLOOD_STAT_MAP.put("出血は無し", BLOOD_NONE);
        BLOOD_STAT_MAP.put("たまに少量出血", BLOOD_FEW);
        BLOOD_STAT_MAP.put("ほぼ毎回の出血", BLOOD_HALF);
        BLOOD_STAT_MAP.put("毎回、ほぼ血液", BLOOD_ALLMOST);
    }

    public static Integer getBloodCode(final String bloodStat){
        return BLOOD_STAT_MAP.get(bloodStat);
    }

    public static String getBloodStat(final Integer bloodCode){
        return BLOOD_CODE_MAP.get(bloodCode);
    }
    private static final Integer OPINION_NONE = 0;

    private static final Integer OPINION_FEW = 1;

    private static final Integer OPINION_HALF = 2;

    private static final Integer OPINION_ALLMOST = 3;

    private static final Map<Integer, String> OPINION_CODE_MAP= new HashMap<Integer, String>();
    static{
        OPINION_CODE_MAP.put(OPINION_NONE, "完全な寛解");
        OPINION_CODE_MAP.put(OPINION_FEW, "活動が軽度");
        OPINION_CODE_MAP.put(OPINION_HALF, "活動が中等度");
        OPINION_CODE_MAP.put(OPINION_ALLMOST, "活動が高度");
    }

    private static final Map<String, Integer> OPINION_STAT_MAP= new HashMap<String, Integer>();
    static{
        OPINION_STAT_MAP.put("完全な寛解", OPINION_NONE);
        OPINION_STAT_MAP.put("活動が軽度", OPINION_FEW);
        OPINION_STAT_MAP.put("活動が中等度", OPINION_HALF);
        OPINION_STAT_MAP.put("活動が高度", OPINION_ALLMOST);
    }

    public static Integer getOpinionCode(final String opinionStat){
        return OPINION_STAT_MAP.get(opinionStat);
    }

    public static String getOpinionStat(final Integer opinionCode){
        return OPINION_CODE_MAP.get(opinionCode);
    }
}
