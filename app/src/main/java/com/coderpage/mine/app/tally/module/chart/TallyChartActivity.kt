package com.coderpage.mine.app.tally.module.chart

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.Menu
import android.view.MenuItem

import com.alibaba.android.arouter.facade.annotation.Route
import com.coderpage.base.utils.ArrayUtils
import com.coderpage.base.utils.ResUtils
import com.coderpage.base.utils.UIUtils
import com.coderpage.mine.MineApp
import com.coderpage.mine.R
import com.coderpage.mine.app.tally.common.router.TallyRouter
import com.coderpage.mine.app.tally.module.chart.data.CategoryData
import com.coderpage.mine.app.tally.module.chart.data.DailyData
import com.coderpage.mine.app.tally.module.chart.data.Month
import com.coderpage.mine.app.tally.module.chart.data.MonthlyData
import com.coderpage.mine.app.tally.module.chart.data.MonthlyDataList
import com.coderpage.mine.app.tally.module.chart.data.MonthlyEntryData
import com.coderpage.mine.app.tally.module.chart.widget.MarkerViewDailyData
import com.coderpage.mine.app.tally.module.chart.widget.MarkerViewMonthData
import com.coderpage.mine.app.tally.module.chart.widget.MineBarChart
import com.coderpage.mine.app.tally.module.chart.widget.MineLineChart
import com.coderpage.mine.app.tally.module.chart.widget.MinePieChart
import com.coderpage.mine.app.tally.persistence.model.CategoryModel
import com.coderpage.mine.common.Font
import com.coderpage.mine.tally.module.chart.TallyChartActivityBinding
import com.coderpage.mine.ui.BaseActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

import java.text.DecimalFormat
import java.util.ArrayList

/**
 * @author lc. 2018-09-24 14:37
 * @since 0.6.0
 */
@Route(path = TallyRouter.CHART)
class TallyChartActivity : BaseActivity() {

    private val categoryExpenseColorArray = intArrayOf(
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryExpenseColor1),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryExpenseColor2),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryExpenseColor3),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryExpenseColor4),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryExpenseColor5),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryExpenseColor6),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryExpenseColor7),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryExpenseColor9))

    private val categoryIncomeColorArray = intArrayOf(
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryIncomeColor1),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryIncomeColor2),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryIncomeColor3),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryIncomeColor4),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryIncomeColor5),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryIncomeColor6),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryIncomeColor7),
            ResUtils.getColor(MineApp.getAppContext(), R.color.categoryIncomeColor9))

    private var mBarChart: MineBarChart? = null
    private var mLineChart: MineLineChart? = null
    private var mPieChart: MinePieChart? = null
    private var mCategoryDataRecycler: RecyclerView? = null
    private var mCategoryDataAdapter: TallyChartCategoryDataAdapter? = null

    private var mBinding: TallyChartActivityBinding? = null
    private var mViewModel: TallyChartViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.tally_module_chart_tally_chart_activity)
        mViewModel = ViewModelProviders.of(this).get(TallyChartViewModel::class.java)
        lifecycle.addObserver(mViewModel!!)

        initView()
        subScribeUi()
    }

    public override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setToolbarAsClose { v -> finish() }
    }

    private fun initView() {
        mBarChart = mBinding!!.barChart
        mLineChart = mBinding!!.lineChart
        mPieChart = mBinding!!.pieChart
        mCategoryDataRecycler = mBinding!!.recyclerCategory
        mCategoryDataRecycler!!.layoutManager = LinearLayoutManager(self(), LinearLayoutManager.VERTICAL, false)
        mCategoryDataAdapter = TallyChartCategoryDataAdapter(mViewModel)
        mCategoryDataRecycler!!.adapter = mCategoryDataAdapter
        mCategoryDataRecycler!!.setHasFixedSize(true)
        mCategoryDataRecycler!!.isNestedScrollingEnabled = false
    }

    private fun subScribeUi() {
        mBinding!!.activity = this
        mBinding!!.vm = mViewModel

        // 日消费柱状图
//        mViewModel!!.dailyExpenseList.observe(this, { dailyDataList ->
//            if (dailyDataList != null) {
//                showDailyBarChart(true, dailyDataList)
//            }
//        })

        mViewModel!!.dailyExpenseList.observe(this, Observer {
            showDailyBarChart(true,it)
        })
        // 日收入柱状图
//        mViewModel!!.dailyIncomeList.observe(this, { dailyDataList ->
//            if (dailyDataList != null) {
//                showDailyBarChart(false, dailyDataList)
//            }
//        })

        mViewModel!!.dailyIncomeList.observe(this, Observer {
            showDailyBarChart(false,it)
        })

        // 月支出、月收入折线图
//        mViewModel!!.monthlyDataList.observe(this, { monthlyDataList ->
//            if (monthlyDataList != null) {
//                showMonthlyLineChart(monthlyDataList)
//            }
//        })

        mViewModel!!.monthlyDataList.observe(this, Observer {
            showMonthlyLineChart(it)
        })

        // 支出分类饼图
//        mViewModel!!.categoryExpenseDataList.observe(this, { categoryDataList ->
//            if (categoryDataList != null) {
//                mCategoryDataAdapter!!.setDataList(categoryDataList)
//                showPieChart(categoryDataList, CategoryModel.TYPE_EXPENSE)
//            }
//        })

        mViewModel!!.categoryExpenseDataList.observe(this, Observer {
            mCategoryDataAdapter!!.setDataList(it)
            showPieChart(it,CategoryModel.TYPE_EXPENSE)
        })
        // 收入分类饼图
//        mViewModel!!.categoryIncomeDataList.observe(this, { categoryDataList ->
//            if (categoryDataList != null) {
//                mCategoryDataAdapter!!.setDataList(categoryDataList)
//                showPieChart(categoryDataList, CategoryModel.TYPE_INCOME)
//            }
//        })

        mViewModel!!.categoryIncomeDataList.observe(this, Observer {
            mCategoryDataAdapter!!.setDataList(it)
            showPieChart(it,CategoryModel.TYPE_INCOME)
        })

//        mViewModel!!.viewReliedTask.observe(this, { task ->
//            if (task != null) {
//                task!!.execute(this)
//            }
//        })

        mViewModel!!.viewReliedTask.observe(this, Observer {
            it!!.execute(this)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_tally_chart, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.menu_date_select -> mViewModel!!.onSelectDateClick(this)
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 显示柱状图
     *
     * @param isShowExpense 是否显示为支出图
     * @param dailyDataList 每日支出或每日收入数据
     */
    private fun showDailyBarChart(isShowExpense: Boolean, dailyDataList: List<DailyData>?) {
        val barColor = if (isShowExpense)
            UIUtils.getColor(this, R.color.expenseColor)
        else
            UIUtils.getColor(this, R.color.incomeColor)

        val yValues = ArrayList<BarEntry>()
        if (dailyDataList != null) {
            for (i in dailyDataList.indices) {
                val dailyData = dailyDataList[i]
                val barEntry = BarEntry(i.toFloat(), dailyData.amount)
                barEntry.data = dailyData
                yValues.add(barEntry)
            }
        }

        var barData: BarData? = null
        if (!yValues.isEmpty()) {
            val barDataSet = BarDataSet(yValues, "")
            barDataSet.color = Color.GRAY
            barDataSet.setDrawValues(false)
            barDataSet.formLineWidth = 0f
            barDataSet.barShadowColor = ResUtils.getColor(this, R.color.chartGridLine)
            barDataSet.color = barColor

            barData = BarData(barDataSet)
            barData.barWidth = 0.5f
        }

        val xAxis = mBarChart!!.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisLineColor = ResUtils.getColor(this, R.color.chartGridLine)
        xAxis.setDrawGridLines(false)
        xAxis.gridColor = ResUtils.getColor(this, R.color.chartGridLine)
        xAxis.setDrawAxisLine(true)
        xAxis.labelCount = yValues.size
        xAxis.setValueFormatter { value, axis ->
            val format = ""
            if (value != 0f
                    && value != (axis.mEntryCount - 1).toFloat()
                    && value != (axis.mEntryCount / 2).toFloat()) {
                return@setValueFormatter format
            }
            val dailyData = if (dailyDataList != null && dailyDataList.size > value)
                dailyDataList[value.toInt()]
            else
                null
            if (dailyData != null) {
                return@setValueFormatter dailyData !!. getMonth ().toString() + "-" + dailyData.dayOfMonth
            }
            format
        }

        val axisLeft = mBarChart!!.axisLeft
        axisLeft.axisMinimum = 0f
        axisLeft.setDrawLabels(false)
        axisLeft.setDrawGridLines(false)
        axisLeft.setDrawAxisLine(false)

        val axisRight = mBarChart!!.axisRight
        axisRight.axisMinimum = 0f
        axisRight.setDrawLabels(false)
        axisRight.setDrawGridLines(false)
        axisRight.setDrawAxisLine(false)

        mBarChart!!.setViewPortOffsets(
                UIUtils.dp2px(self(), 16f).toFloat(),
                UIUtils.dp2px(self(), 40f).toFloat(),
                UIUtils.dp2px(self(), 16f).toFloat(),
                UIUtils.dp2px(self(), 16f).toFloat())
        val markerView = MarkerViewDailyData(self(), R.layout.tally_module_chart_marker_view)
        markerView.setOnClickListener { v, e -> mViewModel!!.onDailyMarkerViewClick(self(), e.data as DailyData) }
        mBarChart!!.setDrawMarkOnTop(true)
        mBarChart!!.marker = markerView
        mBarChart!!.setNoDataText(ResUtils.getString(self(), R.string.tally_chart_empty_tip))
        mBarChart!!.setNoDataTextColor(ResUtils.getColor(self(), R.color.appTextColorPrimary))
        mBarChart!!.setScaleEnabled(false)
        mBarChart!!.setDrawBorders(false)
        mBarChart!!.setDrawBarShadow(true)
        mBarChart!!.setDrawGridBackground(false)
        mBarChart!!.setDrawValueAboveBar(false)
        mBarChart!!.description = null
        mBarChart!!.legend.isEnabled = false
        mBarChart!!.data = barData
        mBarChart!!.animateY(500)
    }

    /**
     * 显示月支出、月收入折线图
     *
     * @param monthlyDataList 月支出、月收入数据
     */
    private fun showMonthlyLineChart(monthlyDataList: MonthlyDataList?) {

        val entryDataArray = SparseArray<MonthlyEntryData>()

        val yValuesExpense = ArrayList<Entry>()
        if (monthlyDataList != null && monthlyDataList.expenseList != null) {
            val expenseList = monthlyDataList.expenseList
            ArrayUtils.forEach(expenseList) { count, index, item ->
                val entry = Entry(index.toFloat(), item.amount)
                val monthlyEntryData = entryDataArray.get(index, MonthlyEntryData())
                entryDataArray.put(index, monthlyEntryData)
                entry.data = monthlyEntryData.setMonth(item.month).setExpenseAmount(item.amount.toDouble())
                yValuesExpense.add(entry)
            }
        }
        val yValuesIncome = ArrayList<Entry>()
        if (monthlyDataList != null && monthlyDataList.incomeList != null) {
            val incomeList = monthlyDataList.incomeList
            ArrayUtils.forEach(incomeList) { count, index, item ->
                val entry = Entry(index.toFloat(), item.amount)
                val monthlyEntryData = entryDataArray.get(index, MonthlyEntryData())
                entry.data = monthlyEntryData.setMonth(item.month).setIncomeAmount(item.amount.toDouble())
                yValuesIncome.add(entry)
            }
        }

        val expenseDataSet = LineDataSet(yValuesExpense, "")
        expenseDataSet.color = ResUtils.getColor(this, R.color.expenseColor)
        expenseDataSet.setDrawValues(false)
        expenseDataSet.formLineWidth = 0f
        expenseDataSet.setCircleColor(ResUtils.getColor(this, R.color.expenseColor))
        expenseDataSet.circleRadius = Color.WHITE.toFloat()
        expenseDataSet.setDrawHorizontalHighlightIndicator(false)
        expenseDataSet.setDrawVerticalHighlightIndicator(true)
        expenseDataSet.highLightColor = ResUtils.getColor(self(), R.color.black)
        expenseDataSet.mode = LineDataSet.Mode.LINEAR

        val incomeDataSet = LineDataSet(yValuesIncome, "")
        incomeDataSet.color = ResUtils.getColor(this, R.color.incomeColor)
        incomeDataSet.setDrawValues(false)
        incomeDataSet.formLineWidth = 0f
        incomeDataSet.setCircleColor(ResUtils.getColor(this, R.color.incomeColor))
        incomeDataSet.setDrawHorizontalHighlightIndicator(false)
        incomeDataSet.setDrawVerticalHighlightIndicator(true)
        incomeDataSet.highLightColor = ResUtils.getColor(self(), R.color.black)
        incomeDataSet.mode = LineDataSet.Mode.LINEAR

        val xAxisLabels = ArrayList<String>()
        val entryList = if (yValuesExpense.size >= yValuesIncome.size) yValuesExpense else yValuesIncome
        for (entry in entryList) {
            val expense = entry.data as MonthlyEntryData
            val month = expense.month
            val label = UIUtils.getString(this, R.string.tally_month_format, month.month)
            xAxisLabels.add(label)
        }
        val xAxis = mLineChart!!.xAxis
        xAxis.isEnabled = true
        xAxis.setDrawLabels(true)
        xAxis.textColor = ResUtils.getColor(applicationContext, R.color.appTextColorLabel)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.axisLineColor = ResUtils.getColor(this, R.color.chartGridLine)
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.setValueFormatter { value, axis ->
            if (xAxisLabels.size > value) {
                return@setValueFormatter xAxisLabels[value.toInt()]
            }
            ""
        }

        val axisLeft = mLineChart!!.axisLeft
        axisLeft.axisMinimum = 0f
        axisLeft.setDrawLabels(false)
        axisLeft.setDrawGridLines(false)
        axisLeft.setDrawAxisLine(false)

        val axisRight = mLineChart!!.axisRight
        axisRight.axisMinimum = 0f
        axisRight.setDrawLabels(false)
        axisRight.setDrawGridLines(false)
        axisRight.setDrawAxisLine(false)

        var lineData: LineData? = LineData()
        if (!yValuesIncome.isEmpty()) {
            lineData!!.addDataSet(incomeDataSet)
        }
        if (!yValuesExpense.isEmpty()) {
            lineData!!.addDataSet(expenseDataSet)
        }

        // 收入支出都没有数据。置空
        if (yValuesExpense.isEmpty() && yValuesIncome.isEmpty()) {
            lineData = null
        }
        if (lineData != null) {
            mLineChart!!.xAxis.axisMaximum = lineData.xMax
            mLineChart!!.xAxis.axisMinimum = lineData.xMin
        }

        mLineChart!!.setViewPortOffsets(
                UIUtils.dp2px(self(), 16f).toFloat(),
                UIUtils.dp2px(self(), 40f).toFloat(),
                UIUtils.dp2px(self(), 16f).toFloat(),
                UIUtils.dp2px(self(), 16f).toFloat())
        val markerView = MarkerViewMonthData(self(), R.layout.tally_module_chart_marker_view)
        markerView.setOnClickListener { v, e -> mViewModel!!.onMonthlyMarkerViewClick(self(), e.data as MonthlyEntryData) }
        mLineChart!!.setNoDataText(ResUtils.getString(self(), R.string.tally_chart_empty_tip))
        mLineChart!!.setNoDataTextColor(ResUtils.getColor(self(), R.color.appTextColorPrimary))
        mLineChart!!.isDragEnabled = true
        mLineChart!!.setScaleEnabled(false)
        mLineChart!!.setVisibleXRange(0f, 11f)
        mLineChart!!.setDrawBorders(false)
        mLineChart!!.setDrawGridBackground(false)
        mLineChart!!.description = null
        mLineChart!!.legend.isEnabled = false
        mLineChart!!.setScaleMinima(1f, 1f)
        mLineChart!!.setDrawMarkers(true)
        mLineChart!!.setDrawMarkOnTop(true)
        mLineChart!!.marker = markerView
        mLineChart!!.data = lineData
        mLineChart!!.animateY(500)
    }

    private fun showPieChart(categoryDataList: List<CategoryData>?, type: Int) {
        val percentFormat = DecimalFormat("0.00")
        val valueTypeface = Typeface.createFromAsset(assets, "font/" + Font.QUICKSAND_MEDIUM.getName())

        val pieEntryList = ArrayList<PieEntry>()
        ArrayUtils.forEach(categoryDataList) { count, index, item -> pieEntryList.add(PieEntry(item.amount.toFloat(), item.categoryName)) }

        val pieDataSet = PieDataSet(pieEntryList, "")
        val colors = if (type == CategoryModel.TYPE_INCOME) categoryIncomeColorArray else categoryExpenseColorArray
        pieDataSet.setColors(*colors)
        val colorList = ArrayList<Int>(colors.size)
        for (color in colors) {
            colorList.add(color)
        }

        pieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        pieDataSet.valueLineColor = ResUtils.getColor(self(), R.color.appTextColorPrimary)
        pieDataSet.setValueTextColors(colorList)
        pieDataSet.valueTextSize = 9f
        pieDataSet.valueTypeface = valueTypeface
        pieDataSet.isValueLineVariableLength = true
        pieDataSet.setValueFormatter { value, entry, dataSetIndex, viewPortHandler -> percentFormat.format(value.toDouble()) + "%" }

        var pieData: PieData? = null
        if (!pieEntryList.isEmpty()) {
            pieData = PieData(pieDataSet)
        }

        mPieChart!!.setNoDataText(ResUtils.getString(self(), R.string.tally_chart_empty_tip))
        mPieChart!!.setNoDataTextColor(ResUtils.getColor(self(), R.color.appTextColorPrimary))
        mPieChart!!.extraTopOffset = 20f
        mPieChart!!.extraBottomOffset = 20f
        mPieChart!!.setUsePercentValues(true)
        mPieChart!!.description = null
        mPieChart!!.setCenterTextSize(20f)
        mPieChart!!.setDrawEntryLabels(false)
        mPieChart!!.isHighlightPerTapEnabled = true
        mPieChart!!.legend.isEnabled = false
        mPieChart!!.legend.orientation = Legend.LegendOrientation.HORIZONTAL
        mPieChart!!.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        mPieChart!!.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        mPieChart!!.legend.isWordWrapEnabled = true
        mPieChart!!.data = pieData
        mPieChart!!.animateY(1400, Easing.EasingOption.EaseInOutQuart)
    }

    companion object {

        val EXTRA_YEAR = "extra_year"
        val EXTRA_MONTH = "extra_month"

        /**
         * 打开图片页
         *
         * @param activity activity
         * @param year     年
         * @param month    月
         */
        fun open(activity: Activity, year: Int, month: Int) {
            val intent = Intent(activity, TallyChartActivity::class.java)
            intent.putExtra(EXTRA_YEAR, year)
            intent.putExtra(EXTRA_MONTH, month)
            activity.startActivity(intent)
        }
    }
}
