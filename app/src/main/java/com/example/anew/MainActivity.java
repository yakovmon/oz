package com.example.anew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleData;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //private ArrayList<Entry> entries;
    private String nameCompeny         = "Apple Inc";
    private String symbl               = "AAPL";
    private double percentageChange    = 0.87;
    private CandleStickChart candleStickChart;
    private LineChart lineChart;
    private int lengthData             = 54;   // this will be replace by entries.length()
    private int graphActive            = 0;   // 0 -> candleStickChart active, 1 - > lineChart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        candleStickChart = findViewById(R.id.candleStickChart);
        lineChart        = findViewById(R.id.lineChart);
//        Button daily     = findViewById(R.id.daily);
//        Button week      = findViewById(R.id.week);
//        Button month     = findViewById(R.id.month);
//        Button line      = findViewById(R.id.line);
//        Button candle    = findViewById(R.id.candle);

        String symbol = "GOOG";
        unpackParams();


        buildCandleStickChart();
        buildLineChart();

        line.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ((ViewFlipper) findViewById(R.id.chart_vf)).setDisplayedChild(1);
//                chnageVisibility(1);
            }
        });

        candle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ((ViewFlipper) findViewById(R.id.chart_vf)).setDisplayedChild(0);
//                chnageVisibility(0);
            }
        });


        daily.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showReleventRangeTime(returnGraphActive(), lengthData);
            }
        });

        week.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showReleventRangeTime(returnGraphActive(), 7);
            }
        });

        month.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showReleventRangeTime(returnGraphActive(), 30);
            }
        });
    }

    private void chnageVisibility(int viewUserWant){
        if(viewUserWant != graphActive){
            if(viewUserWant == 1){
                candleStickChart.setVisibility(View.GONE);
                lineChart.setVisibility(View.VISIBLE);
            }
            else
            {
                lineChart.setVisibility(View.GONE);
                candleStickChart.setVisibility(View.VISIBLE);
            }
            changeActiveChart();
        }

    }

    private void buildLineChart() {
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(false);

        lineChart.getXAxis().setDrawLabels(false);
        LineDataSet set1;
        set1 = new LineDataSet(getDataOfCandle(),"");
        set1.setDrawIcons(false);
        set1.setLineWidth(2f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setDrawFilled(true);
        Drawable drawable = percentageChange < 0 ?
                ContextCompat.getDrawable(this, R.drawable.fade_red):
                ContextCompat.getDrawable(this, R.drawable.fade_green);
        set1.setFillDrawable(drawable);

        LineData lineData = new LineData(set1);

        // hide description and legend
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);
        lineChart.getLegend().setEnabled(false);

        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private void buildCandleStickChart() {
        YAxis yAxisLeft = candleStickChart.getAxisLeft();
        yAxisLeft.setEnabled(false);

        candleStickChart.getXAxis().setDrawLabels(false);

        CandleDataSet candleDataSet = new CandleDataSet(getDataOfCandle(), "Inducesmile");
        candleDataSet.setColor(Color.rgb(80, 80, 80));
        candleDataSet.setShadowColor(getResources().getColor(R.color.colorPrimaryDark));
        candleDataSet.setShadowWidth(0.8f);
        candleDataSet.setDecreasingColor(getResources().getColor(R.color.colorPrimary));
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setIncreasingColor(getResources().getColor(R.color.colorAccent));
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setNeutralColor(Color.LTGRAY);
        candleDataSet.setDrawValues(false);
        CandleData candleData = new CandleData(candleDataSet);

        // hide description and legend
        Description description = new Description();
        description.setText("");
        candleStickChart.setDescription(description);
        candleStickChart.getLegend().setEnabled(false);


        candleStickChart.setData(candleData);
        candleStickChart.invalidate();
    }
    private BarLineChartBase returnGraphActive(){
        return (graphActive == 1) ? lineChart: candleStickChart;
    }
    private void changeActiveChart(){
        graphActive ^= 1;
    }
    private void showReleventRangeTime(BarLineChartBase chart, int interval)
    {
        chart.fitScreen();
        chart.setVisibleXRangeMaximum(interval);
        chart.moveViewToX(lengthData - interval);
    }
    private ArrayList getDataOfCandle(){
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new CandleEntry(1f, 300.57f,296.57f,296.57f,297.43f));
        entries.add(new CandleEntry(2f, 300.60f,296.19f,296.24f,300.35f));
        entries.add(new CandleEntry(3f, 293.68f,289.52f,289.93f,293.65f));
        entries.add(new CandleEntry(4f, 292.69f,285.22f,289.46f,291.52f));
        entries.add(new CandleEntry(5f, 293.97f,288.12f,291.12f,289.80f));
        entries.add(new CandleEntry(6f, 289.98f,284.70f,284.82f,289.91f));
        entries.add(new CandleEntry(7f, 284.89f,282.92f,284.69f,284.27f));
        entries.add(new CandleEntry(8f, 284.25f,280.37f,280.53f,284.00f));
        entries.add(new CandleEntry(9f, 282.65f,278.56f,282.23f,279.44f));
        entries.add(new CandleEntry(10f, 281.18f,278.95f,279.50f,280.02f));
        entries.add(new CandleEntry(11f, 281.90f,279.12f,279.80f,279.74f));
        entries.add(new CandleEntry(12f, 281.77f,278.80f,279.57f,280.41f));
        entries.add(new CandleEntry(13f, 280.79f,276.98f,277.00f,279.86f));
        entries.add(new CandleEntry(14f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(15f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(16f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(17f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(18f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(19f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(20f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(21f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(22f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(23f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(24f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(25f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(26f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(27f, 300.57f,296.57f,296.57f,297.43f));
        entries.add(new CandleEntry(28f, 300.60f,296.19f,296.24f,300.35f));
        entries.add(new CandleEntry(29f, 293.68f,289.52f,289.93f,293.65f));
        entries.add(new CandleEntry(30f, 292.69f,285.22f,289.46f,291.52f));
        entries.add(new CandleEntry(31f, 293.97f,288.12f,291.12f,289.80f));
        entries.add(new CandleEntry(32f, 289.98f,284.70f,284.82f,289.91f));
        entries.add(new CandleEntry(33f, 284.89f,282.92f,284.69f,284.27f));
        entries.add(new CandleEntry(34f, 284.25f,280.37f,280.53f,284.00f));
        entries.add(new CandleEntry(35f, 282.65f,278.56f,282.23f,279.44f));
        entries.add(new CandleEntry(36f, 281.18f,278.95f,279.50f,280.02f));
        entries.add(new CandleEntry(37f, 281.90f,279.12f,279.80f,279.74f));
        entries.add(new CandleEntry(38f, 281.77f,278.80f,279.57f,280.41f));
        entries.add(new CandleEntry(39f, 280.79f,276.98f,277.00f,279.86f));
        entries.add(new CandleEntry(40f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(41f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(42f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(43f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(44f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(45f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(46f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(47f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(48f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(49f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(50f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(51f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(52f, 275.30f,270.93f,271.46f,275.15f));
        entries.add(new CandleEntry(53f, 300.57f,296.57f,296.57f,297.43f));
        entries.add(new CandleEntry(54f, 300.60f,296.19f,296.24f,300.35f));
        return entries;
    }

    public void onWeekClick(View view) {
        showReleventRangeTime(returnGraphActive(), 7);
    }
}


