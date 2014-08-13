package jp.dev.juny.android.uca;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.plot.CategoryPlot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.data.xy.XYSeries;
import org.afree.data.xy.XYSeriesCollection;
import org.afree.graphics.SolidColor;

import jp.dev.juny.android.uca.view.UcaGraphView;


public class UcaGraphActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uca_graph);

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("XYSeries");
        series.add(1, 1);
        series.add(2, 2);
        series.add(3, 3);
        series.add(4, 4);
        series.add(5, 5);
        series.add(6, 6);
        series.add(7, 7);
        dataset.addSeries(series);

        AFreeChart chart = ChartFactory.createXYLineChart("タイトル", "X軸ラベル",
                "ｙ軸ラベル", dataset, PlotOrientation.VERTICAL, false, true, false);

        chart.setBackgroundPaintType(new SolidColor(Color.GRAY));//背景の色
        chart.setBorderPaintType(new SolidColor(Color.BLACK));//枠線の色



        CategoryPlot plot = chart.getCategoryPlot();

        // 縦軸の設定
        NumberAxis numberAxis = (NumberAxis)plot.getRangeAxis();
        numberAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberAxis.setLowerBound(0);
        numberAxis.setUpperBound(10);





        UcaGraphView spcv = (UcaGraphView) findViewById(R.id.graph2);
        spcv.setChart(chart);

//        //グラフにするデータの作成(1)
//        XYSeries series = new XYSeries("data", true, false);
//        for (int i = 0; i < 255; i++) {
//            series.add(i+1, i);
//        }
//        XYSeriesCollection ds = new XYSeriesCollection(series);
//
//        //AFreeChartの作成(2)
//        AFreeChart xyplot = ChartFactory.createXYBarChart(
//                "XYBarサンプル",
//                "Xラベル",
//                true,
//                "Yラベル",
//                ds,
//                PlotOrientation.VERTICAL,
//                true,
//                true,
//                false);
//
//        //Viewへグラフを設定
//        UcaGraphView charview = (UcaGraphView) findViewById(R.id.graph1);
//        charview.setChart(xyplot);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.uca_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
