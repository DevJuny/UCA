package jp.dev.juny.android.uca;

import android.app.Fragment;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import jp.dev.juny.android.uca.common.UcaConstants;
import jp.dev.juny.android.uca.common.UcaDatabaseHelper;
import jp.dev.juny.android.uca.common.UcaUtils;

/**
 * UcaEditActivity
 * <p/>
 * UCAの過去の症状を表示、または更新するActivity </br>
 * TODO UcaEditActivityと共通化を検討
 * <p/>
 * Created by jun on 2014/06/25.
 */
public class UcaViewActivity extends AbstractUcaActivity {

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uca_view);
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
        getMenuInflater().inflate(R.menu.uca_view, menu);
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
        if (id == R.id.action_recordings) {
            // メインである記録機能が選択された場合、Activityの全スタックをクリアして遷移する
            // (クリアしないとハードキーの戻るボタン押下時にめんどくさくなる)
            final Intent intent = new Intent(this, UcaEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.action_calendar) {
            // カレンダーが選択された場合はIntentによる移動ではなく
            // 本Activityのスタックを終わらせて前画面遷移により移動する
            finish();
        } else if (id == R.id.action_chart) {
            // グラフ表示時は通常遷移
            startActivity(new Intent(this, UcaChartActivity.class));
        }
        // R.id.action_settingsは親クラスで処理
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
            implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

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
            fragmentView = inflater.inflate(R.layout.frag_uca_view, container, false);

            // 利用するフォントの生成
            typeface = Typeface.createFromAsset(activity.getAssets(), "mplus-1m-light.ttf");

            // 各ボタンにイベントリスナー登録
            // トイレ回数カウントアップ
            final Button btnUp = (Button) fragmentView.findViewById(R.id.btn_view_count_up);
            btnUp.setOnClickListener(this);

            // トイレ回数カウントダウン
            final Button btnDown = (Button) fragmentView.findViewById(R.id.btn_view_count_down);
            btnDown.setOnClickListener(this);

            // 編集ON,OFFスイッチ
            final Switch swUpdate = (Switch) fragmentView.findViewById(R.id.sw_view_update);
            swUpdate.setOnCheckedChangeListener(this);
            swUpdate.setTypeface(typeface);

            // 各スピナカスタマイズ
            // 出血スピナ
            final Spinner spnBlood = (Spinner) fragmentView.findViewById(R.id.spinner_view_blood);
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
            final Spinner spnDoctorOpinion = (Spinner) fragmentView.findViewById(R.id.spinner_view_doctor_opinion);
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
            final TextView tvDate = (TextView) fragmentView.findViewById(R.id.tv_view_date);
            tvDate.setTypeface(typeface);

            final TextView tvConditionLabel = (TextView) fragmentView.findViewById(R.id.tv_view_condition_label);
            tvConditionLabel.setTypeface(typeface);

            final TextView tvConditionValue = (TextView) fragmentView.findViewById(R.id.tv_view_condition_value);
            tvConditionValue.setTypeface(typeface);

            final TextView tvMayoScoreLabel = (TextView) fragmentView.findViewById(R.id.tv_view_mayo_score_label);
            tvMayoScoreLabel.setTypeface(typeface);

            final TextView tvMayoScore = (TextView) fragmentView.findViewById(R.id.tv_view_mayo_score);
            tvMayoScore.setTypeface(typeface);

            final TextView tvBlood = (TextView) fragmentView.findViewById(R.id.tv_view_blood);
            tvBlood.setTypeface(typeface);

            final TextView tvDoctor = (TextView) fragmentView.findViewById(R.id.tv_view_doctor_opinion);
            tvDoctor.setTypeface(typeface);

            final TextView tvToiletCountLabel = (TextView) fragmentView.findViewById(R.id.tv_view_toilet_count_label);
            tvToiletCountLabel.setTypeface(typeface);

            final TextView tvToiletCount = (TextView) fragmentView.findViewById(R.id.tv_view_toilet_count);
            tvToiletCount.setTypeface(typeface);

            return fragmentView;
        }

        /**
         * onResume
         *
         * Activityが有効になる直前、以下の処理を行う </br>
         * ・カレンダーViewから渡された選択日を取得する　</br>
         * ・選択日の情報をデータベースから取得し、各Viewへ設定する </br>
         *
         */
        @Override
        public void onResume() {
            super.onResume();

            // カレンダーViewからの選択日を取得
            final String selectDataStr = getActivity().getIntent().
                    getStringExtra(UcaConstants.INTENT_KEY_CAL_SELECT_DATE);

            // 日付Viewに設定
            final TextView tvDate = (TextView) fragmentView.findViewById(R.id.tv_view_date);
            tvDate.setText(UcaUtils.formatDateDisp(selectDataStr));

            // データベースより選択日の情報を取得
            final UcaDatabaseHelper dbHelper = new UcaDatabaseHelper(getActivity());
            final List<Object> list = dbHelper.findByDate(selectDataStr);

            // データが存在した場合、各Viewに値を設定
            if (!list.isEmpty()) {
                final TextView tvToiletCount = (TextView) fragmentView.findViewById(R.id.tv_view_toilet_count);
                tvToiletCount.setText(Integer.toString((Integer) list.get(3))); // TODO マジックナンバーやめる

                final Spinner spBlood = (Spinner) fragmentView.findViewById(R.id.spinner_view_blood);
                spBlood.setSelection((Integer) list.get(4));

                final TextView tvBlood = (TextView) fragmentView.findViewById(R.id.tv_view_blood);
                tvBlood.setText(UcaConstants.getBloodStat((Integer) list.get(4)));

                final Spinner spOpinion = (Spinner) fragmentView.findViewById(R.id.spinner_view_doctor_opinion);
                spOpinion.setSelection((Integer) list.get(5));

                final TextView tvOpinion = (TextView) fragmentView.findViewById(R.id.tv_view_doctor_opinion);
                tvOpinion.setText(UcaConstants.getOpinionStat((Integer) list.get(5)));

                // Mayoスコア設定
                final int mayoScore = activity.computeMayoScore(tvToiletCount, spBlood, spOpinion);
                final TextView tvMayoScore = (TextView) fragmentView.findViewById(R.id.tv_view_mayo_score);
                tvMayoScore.setText(String.valueOf(mayoScore));

                // コンディション表示更新
                setConditionView(mayoScore);
            }
        }

        /**
         * Activityが非アクティブになったタイミングで入力値をデータベースに登録
         */
        @Override
        public void onPause() {
            super.onPause();

            // TODO 無条件でデータ更新はしない
            // TODO 修正前情報を持っておいて判定するなど

            // 各Viewを取得
            final TextView tvDate = (TextView) fragmentView.findViewById(R.id.tv_view_date);
            final String dateStr = UcaUtils.formatDateDb(tvDate.getText().toString());

            final TextView tvMayoScore = (TextView) fragmentView.findViewById(R.id.tv_view_mayo_score);
            final TextView tvCount = (TextView) fragmentView.findViewById(R.id.tv_view_toilet_count);

            final Spinner spBlood = (Spinner) fragmentView.findViewById(R.id.spinner_view_blood);
            final Spinner spOpinion = (Spinner) fragmentView.findViewById(R.id.spinner_view_doctor_opinion);

            // データ更新
            final UcaDatabaseHelper dbHelper = new UcaDatabaseHelper(getActivity());
            dbHelper.insert(dateStr,
                    Integer.parseInt(tvMayoScore.getText().toString()),
                    null,
                    Integer.valueOf(tvCount.getText().toString()),
                    spBlood.getSelectedItemPosition(),
                    spOpinion.getSelectedItemPosition());
        }

        /**
         * データ修正ボタンの切り替えにより各Viewの編集可否を切り替え
         *
         * @param buttonView
         * @param isChecked
         */
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (isChecked) {
                Spinner spBlood = (Spinner) fragmentView.findViewById(R.id.spinner_view_blood);
                spBlood.setClickable(true);

                Spinner spOpinion = (Spinner) fragmentView.findViewById(R.id.spinner_view_doctor_opinion);
                spOpinion.setClickable(true);

                Button btnUp = (Button) fragmentView.findViewById(R.id.btn_view_count_up);
                btnUp.setEnabled(true);

                Button btnDown = (Button) fragmentView.findViewById(R.id.btn_view_count_down);
                btnDown.setEnabled(true);

            } else {
                Spinner spBlood = (Spinner) fragmentView.findViewById(R.id.spinner_view_blood);
                spBlood.setClickable(false);

                Spinner spOpinion = (Spinner) fragmentView.findViewById(R.id.spinner_view_doctor_opinion);
                spOpinion.setClickable(false);

                Button btnUp = (Button) fragmentView.findViewById(R.id.btn_view_count_up);
                btnUp.setEnabled(false);

                Button btnDown = (Button) fragmentView.findViewById(R.id.btn_view_count_down);
                btnDown.setEnabled(false);
            }
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
            final TextView tvToiletCount = (TextView) fragmentView.findViewById(R.id.tv_view_toilet_count);
            if (btnId == R.id.btn_view_count_up) {
                // [+]ボタン
                final int count = Integer.valueOf(tvToiletCount.getText().toString()) + 1;
                tvToiletCount.setText(Integer.toString(count));
            } else if (btnId == R.id.btn_view_count_down) {
                // [-]ボタン
                final Integer toiletCount = Integer.valueOf(tvToiletCount.getText().toString());
                // 負数にはしない
                if (toiletCount > 0) {
                    tvToiletCount.setText(Integer.toString(toiletCount - 1));
                }
            }

            /* Mayoスコア計算 */

            // スピナ取得
            final Spinner spBlood = (Spinner) fragmentView.findViewById(R.id.spinner_view_blood);
            final Spinner spOpinion = (Spinner) fragmentView.findViewById(R.id.spinner_view_doctor_opinion);

            // Mayoスコア設定
            final int mayoScore = activity.computeMayoScore(tvToiletCount, spBlood, spOpinion);
            final TextView tvMayoScore = (TextView) fragmentView.findViewById(R.id.tv_view_mayo_score);
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
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Mayoスコア計算用に各View取得
            final TextView tvToiletCount = (TextView) fragmentView.findViewById(R.id.tv_view_toilet_count);
            final Spinner spBlood = (Spinner) fragmentView.findViewById(R.id.spinner_view_blood);
            final Spinner spOpinion = (Spinner) fragmentView.findViewById(R.id.spinner_view_doctor_opinion);

            // Mayoスコア設定
            final int mayoScore = activity.computeMayoScore(tvToiletCount, spBlood, spOpinion);
            final TextView tvMayoScore = (TextView) fragmentView.findViewById(R.id.tv_view_mayo_score);
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
            final ImageView ivMayoScoreIcon = (ImageView) fragmentView.findViewById(R.id.ic_view_mayo_score);
            ivMayoScoreIcon.setImageResource(iconId);

            // 文言設定
            final TextView tvConditionValue = (TextView) fragmentView.findViewById(R.id.tv_view_condition_value);
            tvConditionValue.setText(wordId);
        }
    }
}
