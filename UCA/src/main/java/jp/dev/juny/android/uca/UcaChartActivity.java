package jp.dev.juny.android.uca;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import jp.dev.juny.android.uca.common.UcaConstants;
import jp.dev.juny.android.uca.common.UcaDatabaseHelper;
import jp.dev.juny.android.uca.view.UcaChartView;


public class UcaChartActivity extends AbstractUcaActivity {

    UcaChartView ucaChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uca_chart);

        // データベースより情報を取得
        final UcaDatabaseHelper dbHelper = new UcaDatabaseHelper(this);
        final List<List<Object>> dataList = dbHelper.findAll();

        // フォントファイル読み込み
        // TODO 事前に共通処理で定数化できるならしたい
        final Typeface typeface = Typeface.createFromAsset(getAssets(), UcaConstants.FONT_FILE_NAME_MAIN);

        // グラフを生成
        // TODO データが多くなった時のためにメインスレッドとは分けたい
        ucaChartView = (UcaChartView)findViewById(R.id.chart_view);
        ucaChartView.createChart(dataList, typeface);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.uca_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_recordings) {
            // メインである記録機能が選択された場合、Activityの全スタックをクリアして遷移する
            // (クリアしないとハードキーの戻るボタン押下時にめんどくさくなる)
            final Intent intent = new Intent(this, UcaEditActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (id == R.id.action_calendar) {
            startActivity(new Intent(this, UcaCalendarActivity.class));
        }
        // R.id.action_settingsは親クラスで処理
        return super.onOptionsItemSelected(item);
    }

}
