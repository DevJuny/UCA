package jp.dev.juny.android.uca;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import jp.dev.juny.android.uca.common.UcaConstants;
import jp.dev.juny.android.uca.common.UcaDatabaseHelper;

/**
 * AbstractUcaActivity
 * <p/>
 * UCAのActivityが継承する基底クラス
 * <p/>
 * Created by jun on 2014/06/25.
 */
public abstract class AbstractUcaActivity extends Activity {

    /**
     * Activityが有効になる直前に以下の処理を実行する <br/>
     * <p/>
     * ・プリファレンスに仮保存中の入力データを取得し、<br/>
     * 　入力データの日付が現在時刻と異なる場合（入力時から日付が変わった場合）、<br/>
     * 　仮保存中のデータをデータベースへ登録する。 <br/>
     * 　また、プリファレンスは日付と便の回数を初期化し、それ以外は前日を引き継ぎ保存する。 </br>
     * ・仮保存中のプリファレンスが存在しない場合は、</br>
     * 　初回のツール起動とみなしUC以前の排便回数を入力するダイアログを起動する。
     */
    @Override
    protected void onResume() {
        super.onResume();

        // プリファレンスに仮保存中のデータを取得
        final SharedPreferences pref = getSharedPreferences(UcaConstants.PREFERENCES_NAME, MODE_PRIVATE);
        final String preferDateStr = pref.getString(UcaConstants.PREFERENCES_ITEM_DATE, null);

        // 仮保存中のプリファレンスがない場合
        if (preferDateStr == null) {
            // 初回のツール起動とみなし、病気以前のトイレ回数を設定させる
            createBeforeUcToiletCountDialog();
            return;
        }

        // 現在日時取得(yyyyMMdd)
        final String sysDateStr = UcaConstants.FORMATTER_DB.format(Calendar.getInstance().getTime());

        // 現在日付が仮保存中の日付と異なった場合、仮保存中のデータを登録する
        if (!preferDateStr.equals(sysDateStr)) {
            final UcaDatabaseHelper dbHelper = new UcaDatabaseHelper(this);
            dbHelper.insert(preferDateStr,
                    pref.getInt(UcaConstants.PREFERENCES_ITEM_MAYO_SCORE, 0),
                    null,
                    pref.getInt(UcaConstants.PREFERENCES_ITEM_TOILET_COUNT, 0),
                    pref.getInt(UcaConstants.PREFERENCES_ITEM_TOILET_BLOOD, 0),
                    pref.getInt(UcaConstants.PREFERENCES_ITEM_DOCTOR_OPINION, 0));

            // プリファレンスを初期化する
            SharedPreferences.Editor editor = pref.edit();
            // 日付
            editor.putString(UcaConstants.PREFERENCES_ITEM_DATE, sysDateStr);
            // トイレ回数を初期化
            editor.putInt(UcaConstants.PREFERENCES_ITEM_TOILET_COUNT, 0);
            // その他は前日を引き継いでコミット
            editor.commit();
        }
    }

    /**
     * オプションメニューの共通処理
     * <p/>
     * オプションメニューで「設定」が選択された場合、</br>
     * UC以前の排便回数を記録するダイアログを呼び出す
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        // 「設定」選択時
        if (id == R.id.action_settings) {
            // UC以前の排便回数を設定するダイアログを呼び出す
            createBeforeUcToiletCountDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * UC以前の排便回数を登録するダイアログを生成する <br/>
     * 入力データはプリファレンスに登録する
     *
     * @return
     */
    protected AlertDialog createBeforeUcToiletCountDialog() {

        // プリファレンスよりUC以前のトイレ回数を取得
        final SharedPreferences pref =
                getSharedPreferences(UcaConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        // 初回起動時のプリファレンス未設定時は初期値として0を取得
        final int beforeUcToiletCount =
                pref.getInt(UcaConstants.PREFERENCES_ITEM_BEFORE_UC_TOILET_COUNT, 0);

        // NumberPickerを生成
        final LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final NumberPicker npView = (NumberPicker) inflater.inflate(R.layout.number_picker_dialog_layout, null);
        // 選択肢の最小・最大値を設定
        npView.setMinValue(UcaConstants.BEFORE_UC_TOILET_COUNT_MIN);
        npView.setMaxValue(UcaConstants.BEFORE_UC_TOILET_COUNT_MAX);
        // 初期値を設定
        npView.setValue(beforeUcToiletCount);
        // システムの背景色を設定
        npView.setBackgroundColor(getResources().getColor(R.color.back));

        // ダイアログ生成
        return new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_toilet_count_title)
                .setMessage(R.string.dialog_toilet_count_message)
                .setView(npView)
                .setPositiveButton(R.string.dialog_toilet_count_ok,
                        new DialogInterface.OnClickListener() {
                            /**
                             * OKボタン押下時、選択された値をプリファレンスに登録する
                             *
                             * @param dialog
                             * @param whichButton
                             */
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // 画面内の値をプリファレンスに登録する
                                final SharedPreferences.Editor editor = pref.edit();
                                editor.putInt(UcaConstants.PREFERENCES_ITEM_BEFORE_UC_TOILET_COUNT, npView.getValue());
                                editor.commit();
                            }
                        }
                )
                .show();
    }

    /**
     * Mayoスコアの算出処理
     * <p/>
     * 以下を保持するViewから設定値を取り出し、算出したMayoスコアを返す </br>
     * ・排便回数 </br>
     * ・出血状況 </br>
     * ・医師所見 </br>
     *
     * @param tvToiletCount
     * @param spBlood
     * @param spDoctorOpinion
     * @return Mayoスコア
     */
    protected int computeMayoScore(final TextView tvToiletCount, final Spinner spBlood, final Spinner spDoctorOpinion) {
        return computeMayoScore(
                Integer.parseInt(tvToiletCount.getText().toString()),
                spBlood.getSelectedItemPosition(),
                spDoctorOpinion.getSelectedItemPosition());
    }

    /**
     * Mayoスコアの算出処理
     * <p/>
     * 以下の値から算出したMayoスコアを返す </br>
     * ・排便回数 </br>
     * ・出血状況 </br>
     * ・医師所見 </br>
     *
     * @param toiletCount
     * @param bloodCode
     * @param doctorOpinionCode
     * @return
     */
    protected int computeMayoScore(final int toiletCount, final int bloodCode, final int doctorOpinionCode) {

        // Mayoスコア 初期値0
        int mayoScore = 0;

        /* トイレの回数から算出 */
        // プリファレンスよりUC以前のトイレ回数を取得
        final SharedPreferences pref = getSharedPreferences(UcaConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        final int beforeUcToiletCount = pref.getInt(UcaConstants.PREFERENCES_ITEM_BEFORE_UC_TOILET_COUNT, 0);

        // UC後のトイレ回数の差を取得
        final int gapToiletCount = toiletCount - beforeUcToiletCount;

        // UC後、トイレの回数が増えている場合
        if (gapToiletCount > 0) {
            switch (gapToiletCount) {
                case 1:
                case 2:
                    // 1, 2回増えた場合+1
                    mayoScore++;
                    break;
                case 3:
                case 4:
                    // 3, 4回増えた場合+2
                    mayoScore += 2;
                    break;
                default:
                    // 5回以上増えた場合+3
                    mayoScore += 3;
                    break;
            }
        }

        /* 出血量から算出 */
        mayoScore += bloodCode;

        /* 医師による評価から算出 */
        mayoScore += doctorOpinionCode;

        return mayoScore;
    }
}
