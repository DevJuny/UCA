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
 * Created by jun on 2014/06/25.
 */
public abstract class AbstractUcaActivity  extends Activity {

    @Override
    protected void onResume() {
        super.onResume();

        // プリファレンスに仮保存中のデータを取得
        final SharedPreferences pref = getSharedPreferences(UcaConstants.PREFERENCES_NAME, MODE_PRIVATE);
        final String preferDateStr = pref.getString(UcaConstants.PREFERENCES_ITEM_DATE, null);

        // 仮保存中のプリファレンスがない場合
        if (preferDateStr == null) {
            // 初回のツール起動とみなし、病気以前のトイレ回数を設定させる
            createNumberPickerDialog();
            return;
        }

        // 現在日時取得(yyyyMMdd)
        final String sysDateStr = UcaConstants.FORMATTER_DB.format(Calendar.getInstance().getTime());

        // 現在日付が仮保存中の日付と異なった場合、仮保存中のデータを登録する
        if (!preferDateStr.equals(sysDateStr)) {
            UcaDatabaseHelper dbHelper = new UcaDatabaseHelper(this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            createNumberPickerDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    protected AlertDialog createNumberPickerDialog(){

        // プリファレンスよりUC以前のトイレ回数を取得
        final SharedPreferences pref = getSharedPreferences(UcaConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        // 初回起動時のプリファレンス未設定時は初期値として0を取得
        final int beforeUcToiletCount = pref.getInt(UcaConstants.PREFERENCES_ITEM_BEFORE_UC_TOILET_COUNT, 0);

        // NumberPickerを生成
        final LayoutInflater inflater = (LayoutInflater)
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final NumberPicker npView = (NumberPicker)inflater.inflate(R.layout.number_picker_dialog_layout, null);
        npView.setMinValue(0);
        npView.setMaxValue(10);
        npView.setValue(beforeUcToiletCount);
        npView.setBackgroundColor(getResources().getColor(R.color.back));

        // ダイアログ生成
        return new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_toilet_count_title)
                .setMessage(R.string.dialog_toilet_count_message)
                .setView(npView)
                .setPositiveButton(R.string.dialog_toilet_count_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // 画面内の値をプリファレンスに仮登録する
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt(UcaConstants.PREFERENCES_ITEM_BEFORE_UC_TOILET_COUNT,  npView.getValue());
                                editor.commit();
                            }
                        })
                .show();
    }


    protected int computeMayoScore(final TextView tvToiletCount, final Spinner spBlood, final Spinner spDoctorOpinion){
        return computeMayoScore(Integer.parseInt(tvToiletCount.getText().toString()), spBlood.getSelectedItemPosition(), spDoctorOpinion.getSelectedItemPosition());
    }

    protected int computeMayoScore(final int toiletCount, final int bloodCode, final int doctorOpinionCode) {

        int mayoScore = 0;

        /* トイレの回数から算出 */
        // プリファレンスよりUC以前のトイレ回数を取得
        final SharedPreferences pref = getSharedPreferences(UcaConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        // 初回起動時のプリファレンス未設定時は初期値として0を取得
        final int beforeUcToiletCount = pref.getInt(UcaConstants.PREFERENCES_ITEM_BEFORE_UC_TOILET_COUNT, 0);

        // UC後のトイレ回数の差を取得
        final int toiletCountGap = toiletCount - beforeUcToiletCount;

        // UC後、トイレの回数が増えている場合
        if (toiletCountGap > 0) {
            switch (toiletCountGap) {
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
