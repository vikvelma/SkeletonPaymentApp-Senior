package com.imobile3.groovypayments.ui.chart;

import android.os.Bundle;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.imobile3.groovypayments.R;
import com.imobile3.groovypayments.ui.BaseActivity;
import com.imobile3.groovypayments.ui.dialog.ProgressDialog;

import androidx.lifecycle.ViewModelProviders;

import java.util.List;

public class PieChartActivity extends BaseActivity {
    private static final String PRODUCT_NAME = "x";
    private static final String PRODUCT_QUANTITY = "value";

    private ProgressDialog mProgressDialog;
    private PieChartViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_common);
        setUpMainNavBar();
        mProgressDialog = new ProgressDialog(this);

        mViewModel.getProductList().observe(this, PieChartActivity.this::setPieChart);
    }

    private void setPieChart(List<DataEntry> data) {
        mProgressDialog
                .setMessage(getResources().getString(R.string.daily_report_progress_message));
        mProgressDialog.show();
        AnyChartView chartView = findViewById(R.id.chart_view);

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[] {PRODUCT_NAME,
                PRODUCT_QUANTITY}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(PieChartActivity.this, event.getData().get(PRODUCT_NAME) + ":" +
                        event.getData().get(PRODUCT_QUANTITY), Toast.LENGTH_SHORT).show();
            }
        });

        pie.data(data);
        pie.labels().position("outside");
        pie.legend()
                .itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE)
                .align(Align.CENTER);

        chartView.setOnRenderedListener(new AnyChartView.OnRenderedListener() {
            @Override
            public void onRendered() {
                mProgressDialog.dismiss();
            }
        });

        chartView.setChart(pie);
    }

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, new PieChartViewModelFactory())
                .get(PieChartViewModel.class);
    }

    @Override
    protected void setUpMainNavBar() {
        super.setUpMainNavBar();
        mMainNavBar.showBackButton();
        mMainNavBar.showLogo();
        mMainNavBar.showSubtitle(getString(R.string.daily_report_subtitle));
    }
}
