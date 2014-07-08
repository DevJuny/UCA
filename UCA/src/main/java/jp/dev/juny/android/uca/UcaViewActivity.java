package jp.dev.juny.android.uca;

import android.app.Activity;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import jp.dev.juny.android.uca.common.UcaConstants;
import jp.dev.juny.android.uca.common.UcaDatabaseHelper;
import jp.dev.juny.android.uca.common.UcaUtils;


public class UcaViewActivity extends AbstractUcaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uca_view);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.uca_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_recordings) {
            Intent intent = new Intent(this, UcaEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.action_calendar) {
            finish();
//            Intent intent = new Intent(this, UcaCalendarActivity.class);
//            startActivity(intent);
        }
        // R.id.action_settingsは親クラスで処理
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemSelectedListener{

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
            fragmentView = inflater.inflate(R.layout.frag_uca_view, container, false);

            // 利用するフォントの生成
            typeface = Typeface.createFromAsset(activity.getAssets(), "mplus-1m-light.ttf");

            // 各ボタンにイベントリスナー設定
            // トイレ回数カウントアップ
            final Button btnUp = (Button)fragmentView.findViewById(R.id.btn_view_count_up);
            btnUp.setOnClickListener(this);

            // トイレ回数カウントダウン
            final Button btnDown = (Button)fragmentView.findViewById(R.id.btn_view_count_down);
            btnDown.setOnClickListener(this);

            // 編集ON,OFFスイッチ
            final Switch swUpdate = (Switch)fragmentView.findViewById(R.id.sw_view_update);
            swUpdate.setOnCheckedChangeListener(this);
            swUpdate.setTypeface(typeface);

            // 各スピナカスタマイズ
            // 出血スピナ
            final Spinner spnBlood = (Spinner)fragmentView.findViewById(R.id.spinner_view_blood);
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
            final Spinner spnDoctorOpinion = (Spinner)fragmentView.findViewById(R.id.spinner_view_doctor_opinion);
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
            final TextView tvDate = (TextView)fragmentView.findViewById(R.id.tv_view_date);
            tvDate.setTypeface(typeface);

            final TextView tvConditionLabel = (TextView)fragmentView.findViewById(R.id.tv_view_condition_label);
            tvConditionLabel.setTypeface(typeface);

            final TextView tvConditionValue = (TextView)fragmentView.findViewById(R.id.tv_view_condition_value);
            tvConditionValue.setTypeface(typeface);

            final TextView tvMayoScoreLabel = (TextView)fragmentView.findViewById(R.id.tv_view_mayo_score_label);
            tvMayoScoreLabel.setTypeface(typeface);

            final TextView tvMayoScore = (TextView)fragmentView.findViewById(R.id.tv_view_mayo_score);
            tvMayoScore.setTypeface(typeface);

            final TextView tvBlood = (TextView)fragmentView.findViewById(R.id.tv_view_blood);
            tvBlood.setTypeface(typeface);

            final TextView tvDoctor = (TextView)fragmentView.findViewById(R.id.tv_view_doctor_opinion);
            tvDoctor.setTypeface(typeface);

            final TextView tvToiletCountLabel = (TextView)fragmentView.findViewById(R.id.tv_view_toilet_count_label);
            tvToiletCountLabel.setTypeface(typeface);

            final TextView tvToiletCount = (TextView)fragmentView.findViewById(R.id.tv_view_toilet_count);
            tvToiletCount.setTypeface(typeface);

            return fragmentView;
        }

        @Override
        public void onResume() {
            super.onResume();

            Intent intent = getActivity().getIntent();
            final String data = intent.getStringExtra("Date");
            TextView tvDate = (TextView)fragmentView.findViewById(R.id.tv_view_date);
            tvDate.setText(UcaUtils.formatDateDisp(data));

            UcaDatabaseHelper dbHelper = new UcaDatabaseHelper(getActivity());
            final List<Object> list = dbHelper.findByDate(data);
            if(!list.isEmpty()){
                final TextView tvToiletCount = (TextView)fragmentView.findViewById(R.id.tv_view_toilet_count);
                tvToiletCount.setText(Integer.toString((Integer) list.get(3))); // TODO 変換が冗長

                final Spinner spBlood = (Spinner)fragmentView.findViewById(R.id.spinner_view_blood);
                spBlood.setSelection((Integer) list.get(4));

                final TextView tvBlood = (TextView)fragmentView.findViewById(R.id.tv_view_blood);
                tvBlood.setText(UcaConstants.getBloodStat((Integer) list.get(4)));

                final Spinner spOpinion = (Spinner)fragmentView.findViewById(R.id.spinner_view_doctor_opinion);
                spOpinion.setSelection((Integer)list.get(5));

                final TextView tvOpinion = (TextView)fragmentView.findViewById(R.id.tv_view_doctor_opinion);
                tvOpinion.setText(UcaConstants.getOpinionStat((Integer)list.get(5)));

                // Mayoスコア設定
                final int mayoScore = activity.computeMayoScore(tvToiletCount,spBlood, spOpinion);
                final TextView tvMayoScore = (TextView)fragmentView.findViewById(R.id.tv_view_mayo_score);
                tvMayoScore.setText(String.valueOf(mayoScore));
                setMayoScoreView(mayoScore);
            }
        }

        @Override
        public void onPause() {
            super.onPause();

            // TODO 無条件でデータ更新はしない
            // TODO 例えばトグルが一度でもONになったかどうかを盛っておいて判定するなど

            // データ更新
            final TextView tvDate = (TextView)fragmentView.findViewById(R.id.tv_view_date);
            final String dateStr = UcaUtils.formatDateDb(tvDate.getText().toString());

            final TextView tvMayoScore = (TextView)fragmentView.findViewById(R.id.tv_view_mayo_score);
            final TextView tvCount = (TextView)fragmentView.findViewById(R.id.tv_view_toilet_count);

            final Spinner spBlood = (Spinner)fragmentView.findViewById(R.id.spinner_view_blood);
            final Spinner spOpinion = (Spinner)fragmentView.findViewById(R.id.spinner_view_doctor_opinion);

            UcaDatabaseHelper dbHelper = new UcaDatabaseHelper(getActivity());
            dbHelper.insert(dateStr,
                    Integer.parseInt(tvMayoScore.getText().toString()),
                    null,
                    Integer.valueOf(tvCount.getText().toString()),
                    spBlood.getSelectedItemPosition(),
                    spOpinion.getSelectedItemPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                Spinner spBlood = (Spinner)fragmentView.findViewById(R.id.spinner_view_blood);
                spBlood.setClickable(true);

                Spinner spOpinion = (Spinner)fragmentView.findViewById(R.id.spinner_view_doctor_opinion);
                spOpinion.setClickable(true);

                Button btnUp = (Button)fragmentView.findViewById(R.id.btn_view_count_up);
                btnUp.setEnabled(true);

                Button btnDown = (Button)fragmentView.findViewById(R.id.btn_view_count_down);
                btnDown.setEnabled(true);

            }else{
                Spinner spBlood = (Spinner)fragmentView.findViewById(R.id.spinner_view_blood);
                spBlood.setClickable(false);

                Spinner spOpinion = (Spinner)fragmentView.findViewById(R.id.spinner_view_doctor_opinion);
                spOpinion.setClickable(false);

                Button btnUp = (Button)fragmentView.findViewById(R.id.btn_view_count_up);
                btnUp.setEnabled(false);

                Button btnDown = (Button)fragmentView.findViewById(R.id.btn_view_count_down);
                btnDown.setEnabled(false);
            }
        }

        @Override
        public void onClick(View v) {
            final int btnId = v.getId();

            final TextView tvToiletCount = (TextView)fragmentView.findViewById(R.id.tv_view_toilet_count);
            if(btnId == R.id.btn_view_count_up){
                final int count = Integer.valueOf(tvToiletCount.getText().toString()) + 1;
                tvToiletCount.setText(Integer.toString(count));
            } else if(btnId == R.id.btn_view_count_down){
                final Integer toiletCount = Integer.valueOf(tvToiletCount.getText().toString());
                if(toiletCount >= 1 ){
                    tvToiletCount.setText(Integer.toString(toiletCount - 1));
                }
            }

            final Spinner spBlood = (Spinner)fragmentView.findViewById(R.id.spinner_view_blood);
            final Spinner spOpinion = (Spinner)fragmentView.findViewById(R.id.spinner_view_doctor_opinion);

            // Mayoスコア設定
            final int mayoScore = activity.computeMayoScore(tvToiletCount,spBlood, spOpinion);
            final TextView tvMayoScore = (TextView)fragmentView.findViewById(R.id.tv_view_mayo_score);
            tvMayoScore.setText(String.valueOf(mayoScore));
            setMayoScoreView(mayoScore);
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            final TextView tvToiletCount = (TextView)fragmentView.findViewById(R.id.tv_view_toilet_count);
            final Spinner spBlood = (Spinner)fragmentView.findViewById(R.id.spinner_view_blood);
            final Spinner spOpinion = (Spinner)fragmentView.findViewById(R.id.spinner_view_doctor_opinion);

            // Mayoスコア設定
            final int mayoScore = activity.computeMayoScore(tvToiletCount,spBlood, spOpinion);
            final TextView tvMayoScore = (TextView)fragmentView.findViewById(R.id.tv_view_mayo_score);
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

            final ImageView ivMayoScoreIcon = (ImageView)fragmentView.findViewById(R.id.ic_view_mayo_score);
            ivMayoScoreIcon.setImageResource(iconId);
            final TextView tvConditionValue = (TextView)fragmentView.findViewById(R.id.tv_view_condition_value);
            tvConditionValue.setText(wordId);
        }
    }
}
