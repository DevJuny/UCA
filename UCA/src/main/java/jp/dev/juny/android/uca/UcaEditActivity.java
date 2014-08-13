package jp.dev.juny.android.uca;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import jp.dev.juny.android.uca.common.UcaConstants;
import jp.dev.juny.android.uca.common.UcaUtils;

/**
 * UcaEditActivity
 * <p/>
 * UCAの当日の症状を記録するActivity </br>
 * UCAのエントリーポイント
 * <p/>
 * Created by jun on 2014/06/25.
 */
public class UcaEditActivity extends AbstractUcaActivity {

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uca_edit);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * onCreateOptionsMenu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.uca_edit, menu);
        return true;
    }

    /**
     * onOptionsItemSelected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        // カレンダー選択時はカレンダーActivityへ
        if (id == R.id.action_calendar) {
            // TODO
            startActivity(new Intent(this, UcaCalendarActivity.class));
        }else if (id == R.id.action_chart) {
            // TODO
            startActivity(new Intent(this, UcaChartActivity.class));
        }
        // R.id.action_settingsは親クラスで処理
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
            implements View.OnClickListener, AdapterView.OnItemSelectedListener {

        AbstractUcaActivity activity;
        View fragmentView;
        Typeface typeface;

        public PlaceholderFragment() {
        }

        /**
         * 各Viewの初期化処理として下記を行う
         *
         * ・リスナー登録 </br>
         * ・フォント設定 </br>
         *
         * @param inflater
         * @param container
         * @param savedInstanceState
         * @return
         */
        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 final Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            // アクティビティとViewを保持
            activity = (AbstractUcaActivity) getActivity();
            fragmentView = inflater.inflate(R.layout.frag_uca_edit, container, false);

            // 利用するフォントの生成
            typeface = Typeface.createFromAsset(activity.getAssets(), UcaConstants.FONT_FILE_NAME_MAIN);

            // 各ボタンにイベントリスナー登録
            // トイレ回数カウントアップ
            final Button btnToiletCountUp = (Button) fragmentView.findViewById(R.id.btn_edit_toilet_count_up);
            btnToiletCountUp.setOnClickListener(this);

            // トイレ回数カウントダウン
            final Button btnToiletCountDown = (Button) fragmentView.findViewById(R.id.btn_edit_toilet_count_down);
            btnToiletCountDown.setOnClickListener(this);

            // 各スピナカスタマイズ
            // 出血スピナ
            final Spinner spnBlood = (Spinner) fragmentView.findViewById(R.id.spinner_edit_blood);
            spnBlood.setOnItemSelectedListener(this);
            ArrayAdapter<String> adapterBlood = new ArrayAdapter<String>(
                    activity, R.layout.spinner_item, getResources().getStringArray(R.array.array_toilet_blood)) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView v = (TextView) super.getView(position, convertView, parent);
                    v.setTypeface(typeface);
                    return v;
                }
            };
            spnBlood.setAdapter(adapterBlood);
            adapterBlood.setDropDownViewResource(R.layout.spinner_dropdown);

            // 医師所見スピナ
            final Spinner spnDoctorOpinion = (Spinner) fragmentView.findViewById(R.id.spinner_edit_doctor_opinion);
            spnDoctorOpinion.setOnItemSelectedListener(this);
            ArrayAdapter<String> adapterDoctorOpinion = new ArrayAdapter<String>(
                    activity, R.layout.spinner_item, getResources().getStringArray(R.array.array_doctor_opinion)) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView v = (TextView) super.getView(position, convertView, parent);
                    v.setTypeface(typeface);
                    return v;
                }
            };
            spnDoctorOpinion.setAdapter(adapterDoctorOpinion);
            adapterDoctorOpinion.setDropDownViewResource(R.layout.spinner_dropdown);

            // フォント設定
            // TODO TextViewは独自拡張してフォント設定済のものを利用するようにしたい
            final TextView tvDate = (TextView) fragmentView.findViewById(R.id.tv_edit_date);
            tvDate.setTypeface(typeface);

            final TextView tvConditionLabel = (TextView) fragmentView.findViewById(R.id.tv_edit_condition_label);
            tvConditionLabel.setTypeface(typeface);

            final TextView tvConditionValue = (TextView) fragmentView.findViewById(R.id.tv_edit_condition_value);
            tvConditionValue.setTypeface(typeface);

            final TextView tvMayoScoreLabel = (TextView) fragmentView.findViewById(R.id.tv_edit_mayo_score_label);
            tvMayoScoreLabel.setTypeface(typeface);

            final TextView tvMayoScore = (TextView) fragmentView.findViewById(R.id.tv_edit_mayo_score);
            tvMayoScore.setTypeface(typeface);

            final TextView tvBlood = (TextView) fragmentView.findViewById(R.id.tv_edit_toilet_blood);
            tvBlood.setTypeface(typeface);

            final TextView tvDoctor = (TextView) fragmentView.findViewById(R.id.tv_edit_doctor_opinion);
            tvDoctor.setTypeface(typeface);

            final TextView tvToiletCountLabel = (TextView) fragmentView.findViewById(R.id.tv_edit_toilet_count_label);
            tvToiletCountLabel.setTypeface(typeface);

            final TextView tvToiletCount = (TextView) fragmentView.findViewById(R.id.tv_edit_toilet_count);
            tvToiletCount.setTypeface(typeface);

            return fragmentView;
        }

        /**
         * Activityが有効になる直前、プリファレンスから設定を読み込み各Viewへ設定する
         */
        @Override
        public void onResume() {
            super.onResume();

            // プリファレンス取得
            final SharedPreferences pref = getActivity().getSharedPreferences(UcaConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);

            // 日付設定
            final TextView tvRegData = (TextView) fragmentView.findViewById(R.id.tv_edit_date);
            tvRegData.setText(UcaUtils.formatDateDisp(
                    pref.getString(UcaConstants.PREFERENCES_ITEM_DATE, UcaConstants.FORMATTER_DB.format(Calendar.getInstance().getTime()))));

            // トイレ回数設定
            final int toiletCount = pref.getInt(UcaConstants.PREFERENCES_ITEM_TOILET_COUNT, 0);
            final TextView tvToiletCount = (TextView) fragmentView.findViewById(R.id.tv_edit_toilet_count);
            tvToiletCount.setText(String.valueOf(toiletCount));

            // 出血状態
            final int bloodCode = pref.getInt(UcaConstants.PREFERENCES_ITEM_TOILET_BLOOD, 0);
            final Spinner spBlood = (Spinner) fragmentView.findViewById(R.id.spinner_edit_blood);
            spBlood.setSelection(bloodCode);

            // 医師所見
            final int doctorOpinionCode = pref.getInt(UcaConstants.PREFERENCES_ITEM_DOCTOR_OPINION, 0);
            final Spinner spOpinion = (Spinner) fragmentView.findViewById(R.id.spinner_edit_doctor_opinion);
            spOpinion.setSelection(doctorOpinionCode);

            // Mayoスコア設定
            final int mayoScore = pref.getInt(UcaConstants.PREFERENCES_ITEM_MAYO_SCORE, 0);
            final TextView tvMayoScore = (TextView) fragmentView.findViewById(R.id.tv_edit_mayo_score);
            tvMayoScore.setText(String.valueOf(mayoScore));
            setConditionView(mayoScore); // コンディション表示も更新
        }

        /**
         * Activityが非アクティブになったタイミングで入力値をプリファレンスに登録
         */
        @Override
        public void onPause() {
            super.onPause();

            // 画面内の値をプリファレンスに仮登録する
            final SharedPreferences.Editor editor =
                    getActivity().getSharedPreferences(UcaConstants.PREFERENCES_NAME, Context.MODE_PRIVATE).edit();

            // 日付
            TextView tvRegDate = (TextView) fragmentView.findViewById(R.id.tv_edit_date);
            editor.putString(UcaConstants.PREFERENCES_ITEM_DATE, UcaUtils.formatDateDb(tvRegDate.getText().toString()));

            // Mayo Score
            TextView tvMayoScore = (TextView) fragmentView.findViewById(R.id.tv_edit_mayo_score);
            editor.putInt(UcaConstants.PREFERENCES_ITEM_MAYO_SCORE, Integer.parseInt(tvMayoScore.getText().toString()));

            // トイレ回数
            TextView tvToiletCount = (TextView) fragmentView.findViewById(R.id.tv_edit_toilet_count);
            editor.putInt(UcaConstants.PREFERENCES_ITEM_TOILET_COUNT, Integer.valueOf(tvToiletCount.getText().toString()));

            // 出血状態
            Spinner spBlood = (Spinner) fragmentView.findViewById(R.id.spinner_edit_blood);
            editor.putInt(UcaConstants.PREFERENCES_ITEM_TOILET_BLOOD, UcaConstants.getBloodCode((String) spBlood.getSelectedItem()));

            // 医師所見
            Spinner spOpinion = (Spinner) fragmentView.findViewById(R.id.spinner_edit_doctor_opinion);
            editor.putInt(UcaConstants.PREFERENCES_ITEM_DOCTOR_OPINION, UcaConstants.getOpinionCode((String) spOpinion.getSelectedItem()));

            // 確定
            editor.commit();
        }

        /**
         * トイレ回数ボタン押下時の処理
         *
         * ・トイレ回数の増減処理 </br>
         * ・Mayoスコアの再計算とコンディション表示 </br>
         *
         * @param v
         */
        @Override
        public void onClick(final View v) {
            final int btnId = v.getId();

            // ボタンの増減にあわせてカウントアップ／ダウンを行う
            final TextView tvToiletCount = (TextView) fragmentView.findViewById(R.id.tv_edit_toilet_count);
            if (btnId == R.id.btn_edit_toilet_count_up) {
                // [+]ボタン
                final int count = Integer.valueOf(tvToiletCount.getText().toString()) + 1;
                tvToiletCount.setText(Integer.toString(count));
            } else if (btnId == R.id.btn_edit_toilet_count_down) {
                // [-]ボタン
                final Integer toiletCount = Integer.valueOf(tvToiletCount.getText().toString());
                // 負数にはしない
                if (toiletCount > 0) {
                    tvToiletCount.setText(Integer.toString(toiletCount - 1));
                }
            }

            /* Mayoスコア計算 */

            // スピナ取得
            final Spinner spBlood = (Spinner) fragmentView.findViewById(R.id.spinner_edit_blood);
            final Spinner spOpinion = (Spinner) fragmentView.findViewById(R.id.spinner_edit_doctor_opinion);

            // 計算
            final int mayoScore = activity.computeMayoScore(tvToiletCount, spBlood, spOpinion);
            final TextView tvMayoScore = (TextView) fragmentView.findViewById(R.id.tv_edit_mayo_score);
            tvMayoScore.setText(String.valueOf(mayoScore));

            // コンディション表示更新
            setConditionView(mayoScore);
        }

        /**
         * スピナ更新時、Mayoスコアの再計算とコンディション表示を行う
         * @param parent
         * @param view
         * @param position
         * @param id
         */
        @Override
        public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
            // Mayoスコア計算用に各View取得
            final TextView tvToiletCount = (TextView) fragmentView.findViewById(R.id.tv_edit_toilet_count);
            final Spinner spBlood = (Spinner) fragmentView.findViewById(R.id.spinner_edit_blood);
            final Spinner spOpinion = (Spinner) fragmentView.findViewById(R.id.spinner_edit_doctor_opinion);

            // Mayoスコア設定
            final int mayoScore = activity.computeMayoScore(tvToiletCount, spBlood, spOpinion);
            final TextView tvMayoScore = (TextView) fragmentView.findViewById(R.id.tv_edit_mayo_score);
            tvMayoScore.setText(String.valueOf(mayoScore));

            // コンディション表示更新
            setConditionView(mayoScore);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // 何もしない
        }

        /**
         * Mayoスコアを元にコンディション表示を更新する
         * @param mayoScore
         */
        private void setConditionView(final int mayoScore) {
            // アイコン表示用のID
            final int iconId;
            // 文言表示用のID
            final int wordId;

            // Mayoスコアにより設定値取得
            switch (mayoScore) {
                case 0:
                case 1:
                    // TODO IDの文言はそろえたい（very_good / excellent）
                    iconId = R.drawable.ic_mayo_score_very_good;
                    wordId = R.string.condition_excellent;
                    break;
                case 2:
                case 3:
                    iconId = R.drawable.ic_mayo_score_good;
                    wordId = R.string.condition_good;
                    break;
                case 4:
                case 5:
                    iconId = R.drawable.ic_mayo_score_normal;
                    wordId = R.string.condition_average;
                    break;
                case 6:
                case 7:
                    iconId = R.drawable.ic_mayo_score_bad;
                    wordId = R.string.condition_poor;
                    break;
                default:
                    iconId = R.drawable.ic_mayo_score_very_bad;
                    wordId = R.string.condition_bad;
                    break;
            }

            // アイコン設定
            final ImageView ivMayoScoreIcon = (ImageView) fragmentView.findViewById(R.id.ic_edit_mayo_score);
            ivMayoScoreIcon.setImageResource(iconId);

            // 文言設定
            final TextView tvConditionValue = (TextView) fragmentView.findViewById(R.id.tv_edit_condition_value);
            tvConditionValue.setText(wordId);
        }
    }
}
