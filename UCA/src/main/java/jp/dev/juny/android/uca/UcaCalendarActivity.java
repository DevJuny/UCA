package jp.dev.juny.android.uca;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import jp.dev.juny.android.uca.common.UcaConstants;

/**
 * UcaCalendarActivity
 * <p/>
 * UCAの履歴の日付を選択するカレンダーActivity
 * <p/>
 * TODO CalendarViewを利用しているが、独自実装に切り替えたい
 * TODO Fragmentによる実装に切り替えたい
 * <p/>
 * Created by jun on 2014/06/25.
 */
public class UcaCalendarActivity extends AbstractUcaActivity {

    CalendarView cal;
    Activity activity;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uca_calendar);

        cal = (CalendarView) findViewById(R.id.cal_calendar);
        activity = this;

        // カレンダーにリスナー登録
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            /**
             *
             * @param view
             * @param year
             * @param month
             * @param dayOfMonth
             */
            @Override
            public void onSelectedDayChange(final CalendarView view, final int year, final int month, final int dayOfMonth) {

                // 選択日付をyyyyMMddの形式で数値化
                final String selectDayStr = "" + year + String.format("%02d", month + 1) + String.format("%02d", dayOfMonth);
                final int selectDateInt = Integer.valueOf(selectDayStr);

                // プリファレンスより本日の日付を取得し数値化
                final SharedPreferences pref = getSharedPreferences(UcaConstants.PREFERENCES_NAME, MODE_PRIVATE);
                final String preferDateStr = pref.getString(UcaConstants.PREFERENCES_ITEM_DATE, null);
                final int preferDateInt = Integer.valueOf(preferDateStr);

                // 選択日により画面遷移
                if (selectDateInt == preferDateInt) {
                    // 本日が選択された場合はIntentによる移動ではなく
                    // 本Activityのスタックを終わらせて前画面遷移により移動する
                    finish();
                } else if (selectDateInt < preferDateInt) {
                    // 過去の日付が選択された場合は履歴参照用のActivityを起動
                    final Intent intent = new Intent(activity, UcaViewActivity.class);
                    intent.putExtra(UcaConstants.INTENT_KEY_CAL_SELECT_DATE, selectDayStr);
                    startActivity(intent);
                } else {
                    // 未来の日付はトーストを表示し選択不可
                    Toast.makeText(activity, R.string.toast_msg_selected_future, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.uca_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_recordings) {
//            Intent intent = new Intent(this, UcaEditActivity.class);
//            startActivity(intent);
            // メインである記録機能が選択された場合、Activityの全スタックをクリアして遷移する
            // (クリアしないとハードキーの戻るボタン押下時にめんどくさくなる)
            final Intent intent = new Intent(this, UcaEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.action_chart) {
            // グラフ表示時は通常遷移
            startActivity(new Intent(this, UcaChartActivity.class));
        }
        // R.id.action_settingsは親クラスで処理
        return super.onOptionsItemSelected(item);
    }

}
