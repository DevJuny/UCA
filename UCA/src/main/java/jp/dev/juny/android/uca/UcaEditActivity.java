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
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import jp.dev.juny.android.uca.common.UcaConstants;
import jp.dev.juny.android.uca.common.UcaUtils;


public class UcaEditActivity extends AbstractUcaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uca_edit);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.uca_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_calendar) {
            Intent intent = new Intent(this, UcaCalendarActivity.class);
            startActivity(intent);
        }
        // R.id.action_settingsは親クラスで処理
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

        AbstractUcaActivity activity;
        View fragmentView;
        Typeface typeface;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            // アクティビティとViewを保持
            activity = (AbstractUcaActivity)getActivity();
            fragmentView = inflater.inflate(R.layout.frag_uca_edit, container, false);

            // 利用するフォントの生成
            typeface = Typeface.createFromAsset(activity.getAssets(), "mplus-1m-light.ttf");

            // 各ボタンにイベントリスナー設定
            // トイレ回数カウントアップ
            final Button btnToiletCountUp = (Button)fragmentView.findViewById(R.id.btn_edit_toilet_count_up);
            btnToiletCountUp.setOnClickListener(this);

            // トイレ回数カウントダウン
            final Button btnToiletCountDown = (Button)fragmentView.findViewById(R.id.btn_edit_toilet_count_down);
            btnToiletCountDown.setOnClickListener(this);

            // 各スピナカスタマイズ
            // 出血スピナ
            final Spinner spnBlood = (Spinner)fragmentView.findViewById(R.id.spinner_edit_blood);
            spnBlood.setOnItemSelectedListener(this);
            ArrayAdapter<String> adapterBlood = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.array_toilet_blood)){
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView v = (TextView) super.getView(position, convertView, parent);
                    v.setTypeface(typeface);
                    return v;
                }
            };
            spnBlood.setAdapter(adapterBlood);
            adapterBlood.setDropDownViewResource(R.layout.spinner_dropdown);

            // 医師所見スピナ
            final Spinner spnDoctorOpinion = (Spinner)fragmentView.findViewById(R.id.spinner_edit_doctor_opinion);
            spnDoctorOpinion.setOnItemSelectedListener(this);
            ArrayAdapter<String> adapterDoctorOpinion = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.array_doctor_opinion)){
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView v = (TextView) super.getView(position, convertView, parent);
                    v.setTypeface(typeface);
                    return v;
                }
            };
            spnDoctorOpinion.setAdapter(adapterDoctorOpinion);
            adapterDoctorOpinion.setDropDownViewResource(R.layout.spinner_dropdown);

            // フォント設定
            final TextView tvDate = (TextView)fragmentView.findViewById(R.id.tv_edit_date);
            tvDate.setTypeface(typeface);

            final TextView tvConditionLabel = (TextView)fragmentView.findViewById(R.id.tv_edit_condition_label);
            tvConditionLabel.setTypeface(typeface);

            final TextView tvConditionValue = (TextView)fragmentView.findViewById(R.id.tv_edit_condition_value);
            tvConditionValue.setTypeface(typeface);

            final TextView tvMayoScoreLabel = (TextView)fragmentView.findViewById(R.id.tv_edit_mayo_score_label);
            tvMayoScoreLabel.setTypeface(typeface);

            final TextView tvMayoScore = (TextView)fragmentView.findViewById(R.id.tv_edit_mayo_score);
            tvMayoScore.setTypeface(typeface);

            final TextView tvBlood = (TextView)fragmentView.findViewById(R.id.tv_edit_toilet_blood);
            tvBlood.setTypeface(typeface);

            final TextView tvDoctor = (TextView)fragmentView.findViewById(R.id.tv_edit_doctor_opinion);
            tvDoctor.setTypeface(typeface);

            final TextView tvToiletCountLabel = (TextView)fragmentView.findViewById(R.id.tv_edit_toilet_count_label);
            tvToiletCountLabel.setTypeface(typeface);

            final TextView tvToiletCount = (TextView)fragmentView.findViewById(R.id.tv_edit_toilet_count);
            tvToiletCount.setTypeface(typeface);

            return fragmentView;
        }

        @Override
        public void onResume() {
            super.onResume();

            // プリファレンス取得
            final SharedPreferences pref = getActivity().getSharedPreferences(UcaConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);

            // 日付設定
            final TextView tvRegData = (TextView)fragmentView.findViewById(R.id.tv_edit_date);
            tvRegData.setText(UcaUtils.formatDateDisp(pref.getString(UcaConstants.PREFERENCES_ITEM_DATE, UcaConstants.FORMATTER_DB.format(Calendar.getInstance().getTime()))));

            // トイレ回数設定
            final int toiletCount = pref.getInt(UcaConstants.PREFERENCES_ITEM_TOILET_COUNT, 0);
            final TextView tvToiletCount = (TextView)fragmentView.findViewById(R.id.tv_edit_toilet_count);
            tvToiletCount.setText(String.valueOf(toiletCount));

            // 出血状態
            final int bloodCode = pref.getInt(UcaConstants.PREFERENCES_ITEM_TOILET_BLOOD, 0);
            final Spinner spBlood = (Spinner)fragmentView.findViewById(R.id.spinner_edit_blood);
            spBlood.setSelection(bloodCode);

            // 医師所見
            final int doctorOpinionCode = pref.getInt(UcaConstants.PREFERENCES_ITEM_DOCTOR_OPINION, 0);
            final Spinner spOpinion = (Spinner)fragmentView.findViewById(R.id.spinner_edit_doctor_opinion);
            spOpinion.setSelection(doctorOpinionCode);

            // Mayoスコア設定
            final int mayoScore = pref.getInt(UcaConstants.PREFERENCES_ITEM_MAYO_SCORE, 0);
            //            final float mayoScore = activity.computeMayoScore(toiletCount,bloodCode, doctorOpinionCode);
            final TextView tvMayoScore = (TextView)fragmentView.findViewById(R.id.tv_edit_mayo_score);
            tvMayoScore.setText(String.valueOf(mayoScore));
            setMayoScoreView(mayoScore);
        }

        @Override
        public void onPause() {
            super.onPause();

            // 画面内の値をプリファレンスに仮登録する
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(UcaConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // 日付
            TextView tvRegDate = (TextView)fragmentView.findViewById(R.id.tv_edit_date);
            editor.putString(UcaConstants.PREFERENCES_ITEM_DATE, UcaUtils.formatDateDb(tvRegDate.getText().toString()));

            // Mayo Score
            TextView tvMayoScore = (TextView)fragmentView.findViewById(R.id.tv_edit_mayo_score);
            editor.putInt(UcaConstants.PREFERENCES_ITEM_MAYO_SCORE, Integer.parseInt(tvMayoScore.getText().toString()));

            // トイレ回数
            TextView tvToiletCount = (TextView)fragmentView.findViewById(R.id.tv_edit_toilet_count);
            editor.putInt(UcaConstants.PREFERENCES_ITEM_TOILET_COUNT, Integer.valueOf(tvToiletCount.getText().toString()));

            // 出血状態
            Spinner spBlood = (Spinner)fragmentView.findViewById(R.id.spinner_edit_blood);
            editor.putInt(UcaConstants.PREFERENCES_ITEM_TOILET_BLOOD, UcaConstants.getBloodCode((String) spBlood.getSelectedItem()));

            // 医師所見
            Spinner spOpinion = (Spinner)fragmentView.findViewById(R.id.spinner_edit_doctor_opinion);
            editor.putInt(UcaConstants.PREFERENCES_ITEM_DOCTOR_OPINION, UcaConstants.getOpinionCode((String) spOpinion.getSelectedItem()));

            // 確定
            editor.commit();
        }


        @Override
        public void onClick(View v) {
            final int btnId = v.getId();

            final TextView tvToiletCount = (TextView)fragmentView.findViewById(R.id.tv_edit_toilet_count);
            if(btnId == R.id.btn_edit_toilet_count_up){
                final int count = Integer.valueOf(tvToiletCount.getText().toString()) + 1;
                tvToiletCount.setText(Integer.toString(count));
            } else if(btnId == R.id.btn_edit_toilet_count_down){
                final Integer toiletCount = Integer.valueOf(tvToiletCount.getText().toString());
                if(toiletCount >= 1 ){
                    tvToiletCount.setText(Integer.toString(toiletCount - 1));
                }
            }

            final Spinner spBlood = (Spinner)fragmentView.findViewById(R.id.spinner_edit_blood);
            final Spinner spOpinion = (Spinner)fragmentView.findViewById(R.id.spinner_edit_doctor_opinion);

            // Mayoスコア設定
            final int mayoScore = activity.computeMayoScore(tvToiletCount,spBlood, spOpinion);
            final TextView tvMayoScore = (TextView)fragmentView.findViewById(R.id.tv_edit_mayo_score);
            tvMayoScore.setText(String.valueOf(mayoScore));
            setMayoScoreView(mayoScore);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final TextView tvToiletCount = (TextView)fragmentView.findViewById(R.id.tv_edit_toilet_count);
            final Spinner spBlood = (Spinner)fragmentView.findViewById(R.id.spinner_edit_blood);
            final Spinner spOpinion = (Spinner)fragmentView.findViewById(R.id.spinner_edit_doctor_opinion);

            // Mayoスコア設定
            final int mayoScore = activity.computeMayoScore(tvToiletCount,spBlood, spOpinion);
            final TextView tvMayoScore = (TextView)fragmentView.findViewById(R.id.tv_edit_mayo_score);
            tvMayoScore.setText(String.valueOf(mayoScore));
            setMayoScoreView(mayoScore);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // 何もしない
        }

        private void setMayoScoreView(final int mayoScore){
            final int iconId;
            final int wordId;
            switch (mayoScore){
                case 0:
                case 1:
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

            final ImageView ivMayoScoreIcon = (ImageView)fragmentView.findViewById(R.id.ic_edit_mayo_score);
            ivMayoScoreIcon.setImageResource(iconId);
            final TextView tvConditionValue = (TextView)fragmentView.findViewById(R.id.tv_edit_condition_value);
            tvConditionValue.setText(wordId);
        }
    }
}
