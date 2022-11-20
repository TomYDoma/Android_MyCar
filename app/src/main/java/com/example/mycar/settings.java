package com.example.mycar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class settings extends Activity {
    String fuel = " ";
    String repair = " ";
    String wash = " ";
    String shina = " ";
    String prochee = " ";

    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Получаю с галвной активности данные для диаграммы
        setContentView(R.layout.settings);
        Bundle arguments = getIntent().getExtras();
        fuel = arguments.get("fuel").toString();
        repair = arguments.get("repair").toString();
        wash = arguments.get("wash").toString();
        shina = arguments.get("shina").toString();
        prochee = arguments.get("prochee").toString();

        int f = Integer.parseInt(fuel);
        int r = Integer.parseInt(repair);
        int w = Integer.parseInt(wash);
        int s = Integer.parseInt(shina);
        int p = Integer.parseInt(prochee);



        //Вывожу на экран
        TextView zapTextView = findViewById(R.id.zap);
        zapTextView.setText(fuel + "р");

        TextView remTextView = findViewById(R.id.rem);
        remTextView.setText(repair + "р");

        TextView washTextView = findViewById(R.id.wash);
        washTextView.setText(wash + "р");

        TextView shinaTextView = findViewById(R.id.shina);
        shinaTextView.setText(shina + "р");

        TextView procheeTextView = findViewById(R.id.prochee);
        procheeTextView.setText(prochee + "р");


        //Работа с диаграммой
        pieChart = findViewById(R.id.piediagramm);
        setupPieChart();
        loadPieChartData(f,r,w,s,p);


    }
    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setCenterText("Диаграмма расходов");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setTextColor(Color.WHITE);
        l.setEnabled(false);
    }

    private void loadPieChartData(int fuel, int repair, int wash, int shina, int prochee) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(fuel, "Заправка"));
        entries.add(new PieEntry(shina, "Шиномонтаж"));
        entries.add(new PieEntry(repair, "Ремонт"));
        entries.add(new PieEntry(wash, "Мойка"));
        entries.add(new PieEntry(prochee, "Прочее"));


        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Диаграмма расходов");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }


    public void OnClickMainBack(View view) {
        Intent intent = new Intent(settings.this, MainActivity.class);
        startActivity(intent);
    }
}