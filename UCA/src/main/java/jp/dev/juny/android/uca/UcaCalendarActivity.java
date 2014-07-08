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


public class UcaCalendarActivity extends AbstractUcaActivity {

    CalendarView cal;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uca_calendar);

        cal = (CalendarView)findViewById(R.id.cal_calendar);
        activity = this;

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                final String selectDayStr = "" + year + String.format("%02d", month + 1) + String.format("%02d", dayOfMonth);

                final SharedPreferences pref = getSharedPreferences(UcaConstants.PREFERENCES_NAME, MODE_PRIVATE);
                final String preferDateStr = pref.getString(UcaConstants.PREFERENCES_ITEM_DATE, null);

                final int selectDateInt = Integer.valueOf(selectDayStr);
                final int preferDateInt = Integer.valueOf(preferDateStr);

                final Intent intent;
                if(selectDateInt == preferDateInt){
                    finish();
//                    intent = new Intent(activity, UcaEditActivity.class);
//                    startActivity(intent);
                }else if(selectDateInt < preferDateInt){
                    intent = new Intent(activity, UcaViewActivity.class);
                    // TODO Util化、またformatを日付形式に指定もできそう
                    intent.putExtra("Date", selectDayStr);
                    startActivity(intent);
                }else{
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
            finish();
        }
        // R.id.action_settingsは親クラスで処理
        return super.onOptionsItemSelected(item);
    }

}
