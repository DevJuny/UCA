package jp.dev.juny.android.uca.view;

import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.drawable.Drawable;
//import android.text.TextPaint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Toast;
//import android.view.View;
import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.StandardChartTheme;
import org.afree.chart.axis.CategoryAxis;
import org.afree.chart.axis.DateAxis;
import org.afree.chart.axis.DateTickUnit;
import org.afree.chart.axis.DateTickUnitType;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.axis.ValueAxis;
import org.afree.chart.plot.CategoryPlot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.XYPlot;
import org.afree.chart.renderer.category.CategoryItemRenderer;
import org.afree.chart.renderer.category.LineAndShapeRenderer;
import org.afree.chart.renderer.xy.XYItemRenderer;
import org.afree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.afree.chart.title.TextTitle;
import org.afree.data.category.DefaultCategoryDataset;
import org.afree.data.general.SeriesException;
import org.afree.data.time.Day;
import org.afree.data.time.TimeSeries;
import org.afree.data.time.TimeSeriesCollection;
import org.afree.data.xy.XYDataset;
import org.afree.data.xy.XYSeries;
import org.afree.data.xy.XYSeriesCollection;
import org.afree.graphics.PaintType;
import org.afree.graphics.SolidColor;
import org.afree.graphics.geom.Font;
import org.afree.graphics.geom.RectShape;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import jp.dev.juny.android.uca.R;
import jp.dev.juny.android.uca.common.UcaConstants;

/**
 * TODO: document your custom view class.
 */
public class UcaChartView extends UcaBaseChartView {

    // VIEWの背景色（薄緑）
    private static PaintType mainColor = new SolidColor(Color.rgb(127, 188, 173));

    // VIEWの文字色（白）
    private static PaintType fontNormalColor = new SolidColor(Color.rgb(245, 245, 245));

    // 線の色1
    private static PaintType lineColor1 = new SolidColor(Color.rgb(44, 83, 121));

//        // VIEWの文字色（濃緑）
//        PaintType subContentsColor = new SolidColor(Color.rgb(0, 177, 144));
//
//        // 線の色1
//        PaintType lineColor2 = new SolidColor(Color.rgb(233, 135, 46));
//
//        // 線の色1
//        PaintType lineColor3 = new SolidColor(Color.rgb(248, 130, 60));


    /**
     * グラフ生成
     */
    public void createChart(List<List<Object>> dataList, Typeface typeface) {

        // 日本語が文字化けしないテーマ
        ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());

        // 描画領域を設定
        setMaximumDrawHeight(3000);
        setMaximumDrawWidth(3000);

        // チャートデータを生成
        AFreeChart chart = getTimeSeriesChart(dataList);

        // 全体の背景を設定
        chart.setBackgroundPaintType(mainColor);

        // チャートタイトルの設定
        TextTitle title = new TextTitle("Mayoスコア増減グラフ");
        title.setFont(new Font(typeface, Typeface.BOLD, 50));
        title.setPaintType(fontNormalColor);
        chart.setTitle(title);

        // グラフの描画領域の設定
        XYPlot  plot2 = chart.getXYPlot();
        plot2.setBackgroundPaintType(mainColor);

        XYItemRenderer renderer1 = plot2.getRenderer();
        // 太さ 0番目の線を3fの太さで
        renderer1.setSeriesStroke(0, 2f);
        renderer1.setSeriesPaintType(0, lineColor1);

        // Y軸（縦軸）の設定
        NumberAxis rangeAxis2 = (NumberAxis)plot2.getRangeAxis();
        rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // 目盛の整数表示
        rangeAxis2.setLowerBound(0);
        rangeAxis2.setUpperBound(9);
        rangeAxis2.setLimitRange(0, 9);
        rangeAxis2.setLabelPaintType(fontNormalColor);
//        rangeAxis2.setAutoRange(true);

        // X軸（横軸）の設定
        DateAxis  domainAxis = (DateAxis)plot2.getDomainAxis();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");

        // 上限・下限の設定
        // TODO 空かどうかの判定はもっと早い段階でやる
        // TODO データ量が複数件ある場合、グラフ表示を行う
        if(dataList.size() >= 2){
            final String upperDateStr = (String)dataList.get(dataList.size()-1).get(0);
            final String lowerDateStr;

            if(dataList.size() <= 6){
                domainAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1));
            }

            if(dataList.size() > 30){
                // TODO 30で整合取れるかは検証すること！
                lowerDateStr = (String)dataList.get(dataList.size() -30 ).get(0);
                domainAxis.setRange(UcaConstants.FORMATTER_DB.parse(lowerDateStr, new ParsePosition(0)), UcaConstants.FORMATTER_DB.parse(upperDateStr, new ParsePosition(0)));
            }

//            if(dataList.size() <= 30){
////                lowerDateStr = (String)dataList.get(0).get(0);
////                domainAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1));
//            }else{
//                // TODO 30で整合取れるかは検証すること！
//                lowerDateStr = (String)dataList.get(dataList.size() -30 ).get(0);
//                domainAxis.setRange(UcaConstants.FORMATTER_DB.parse(lowerDateStr, new ParsePosition(0)), UcaConstants.FORMATTER_DB.parse(upperDateStr, new ParsePosition(0)));
//            }
//            domainAxis.setAutoRange(true);


        }


        // メモリの日付表示フォーマットを設定
        domainAxis.setDateFormatOverride(new SimpleDateFormat("yy/MM/dd"));
//        domainAxis.setFixedAutoRange();

        domainAxis.setLabelPaintType(fontNormalColor);

//        LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot2.getRenderer();
//        renderer.setSeriesPaintType(0, new SolidColor(Color.rgb(0, 177, 144)));
//        renderer.setSeriesStroke(0, new Float(5f));

        Font xyTitleFont = new Font(typeface, Typeface.BOLD, 40);
        rangeAxis2.setLabelFont(xyTitleFont);
        domainAxis.setLabelFont(xyTitleFont);

        Font tickFont = new Font(typeface, Typeface.NORMAL, 30);
        rangeAxis2.setTickLabelFont(tickFont);
        domainAxis.setTickLabelFont(tickFont);

        rangeAxis2.setTickLabelPaintType(fontNormalColor);
        domainAxis.setTickLabelPaintType(fontNormalColor);

        // Viewにチャートを設定
        setChart(chart);
    }

    private AFreeChart getTimeSeriesChart(List<List<Object>> dataList){
        // XYDatasetオブジェクトの作成
        XYDataset xyData = createXYDataset(dataList);
        // XYDatasetをデータにしてJFreeChartを作成
        AFreeChart tsChart = ChartFactory.createTimeSeriesChart ("グラフ",
                "日付",
                "Partial Mayo Score",
                xyData,
                false, true, false);
        return tsChart;
    }

    private XYDataset createXYDataset(List<List<Object>> dataList) {
        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();

//        for (int series = 0; series < SERIES_COUNT; series++) {
//            timeSeriesCollection.addSeries(createTimeSeries(series));
//        }
        TimeSeries ts = new TimeSeries("Series");
        DateFormat df = new SimpleDateFormat("yyyyMMdd"); // TODO 定数かしているはず
        for(List<Object> recordList : dataList){
            final int mayoScore = (Integer)recordList.get(1);
            final String dateStr = (String)recordList.get(0);

            try {
                ts.add(new Day(df.parse(dateStr)), mayoScore);
            } catch(Exception ex){
                // TODO
            }
        }
        timeSeriesCollection.addSeries(ts);

        return timeSeriesCollection;
    }

    public UcaChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init(attrs, 0);
    }

//    private RectShape chartArea;
//
//    private void init(AttributeSet attrs, int defStyle) {
//        chartArea = new RectShape();
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        chartArea.setWidth(w);          // (2)
//        chartArea.setHeight(h);
//    }

}
