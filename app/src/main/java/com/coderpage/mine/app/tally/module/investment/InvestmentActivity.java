package com.coderpage.mine.app.tally.module.investment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.coderpage.mine.R;
import com.coderpage.mine.app.tally.common.router.TallyRouter;
import com.coderpage.mine.app.tally.module.fund.FundEditActivity;
import com.coderpage.mine.app.tally.module.index.IndexEditActivity;
import com.coderpage.mine.app.tally.module.investment.adapter.IndexHKAdapter;
import com.coderpage.mine.app.tally.module.investment.adapter.IndexInsideAdapter;
import com.coderpage.mine.app.tally.module.investment.adapter.IndexOfFundAdapter;
import com.coderpage.mine.app.tally.module.investment.adapter.IndexOutsideAdapter;
import com.coderpage.mine.app.tally.module.investment.model.IndexFundViewModel;
import com.coderpage.mine.app.tally.module.investment.model.InvestmentModel;
import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;
import com.coderpage.mine.ui.BaseActivity;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@Route(path = TallyRouter.INVESTMENT_PATH)
public class InvestmentActivity extends BaseActivity {

    /**  国内指数 */
    public static final String INSIDE_TYPE  = "1";

    /** 国外指数 */
    public static final String OUTSIZE_TYPE = "2";

    /** 香港指数 */
    public static final String HK_TYPE      = "3";

    private InvestmentModel           mInvestmentModel;
    private InvestmentActivityBinding mActivityBinding;

    private IndexFundViewModel mFundViewModel;

    private IndexInsideAdapter  mIndexInsideAdapter;
    private IndexOutsideAdapter mIndexOutsideAdapter;
    private IndexHKAdapter      mHKAdapter;
    private IndexOfFundAdapter  mIndexOfFundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityBinding = DataBindingUtil.setContentView(this,R.layout.activity_investment);
        mInvestmentModel = ViewModelProviders.of(this).get(InvestmentModel.class);
        getLifecycle().addObserver(mInvestmentModel);

        setToolbarAsBack(v -> finish());
        setToolbarTitle(getResources().getString(R.string.menu_tally_investment));

        initView();
        subscribeUi();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        RecyclerView insideRcView = mActivityBinding.indexRecycler;
        insideRcView.setLayoutManager(layoutManager);

        mIndexInsideAdapter = new IndexInsideAdapter(this);
        insideRcView.setAdapter(mIndexInsideAdapter);

        LinearLayoutManager outLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        RecyclerView outsideRcView = mActivityBinding.indexUSARecycler;
        outsideRcView.setLayoutManager(outLayoutManager);

        mIndexOutsideAdapter = new IndexOutsideAdapter(this);
        outsideRcView.setAdapter(mIndexOutsideAdapter);

        LinearLayoutManager hkLayoutManger = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        RecyclerView hkRcView = mActivityBinding.indexHKRecycler;
        hkRcView.setLayoutManager(hkLayoutManger);

        mHKAdapter = new IndexHKAdapter(this);
        hkRcView.setAdapter(mHKAdapter);

        LinearLayoutManager fundLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        RecyclerView fundRcView = mActivityBinding.fundRecycler;
        fundRcView.setLayoutManager(fundLayoutManager);

        mFundViewModel = ViewModelProviders.of(this).get(IndexFundViewModel.class);
        mIndexOfFundAdapter = new IndexOfFundAdapter(this,mFundViewModel);

        mFundViewModel.observerUpdate.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean){
                    mInvestmentModel.queryAllFund();
                }
            }
        });

        fundRcView.setAdapter(mIndexOfFundAdapter);
    }

    private void subscribeUi() {
        mActivityBinding.setActivity(this);
        mActivityBinding.setVm(mInvestmentModel);

        mInvestmentModel.insideObserve.observe(this, new Observer<List<IndexModel>>() {
            @Override
            public void onChanged(@Nullable List<IndexModel> indexModels) {
                mIndexInsideAdapter.setData(indexModels);
            }
        });

        mInvestmentModel.HKObserve.observe(this, new Observer<List<IndexModel>>() {
            @Override
            public void onChanged(@Nullable List<IndexModel> indexModels) {
                mHKAdapter.setData(indexModels);
            }
        });

        mInvestmentModel.outsizeObserve.observe(this, new Observer<List<IndexModel>>() {
            @Override
            public void onChanged(@Nullable List<IndexModel> indexModels) {
                mIndexOutsideAdapter.setData(indexModels);
            }
        });

        mInvestmentModel.fundObserve.observe(this, new Observer<List<FundModel>>() {
            @Override
            public void onChanged(@Nullable List<FundModel> fundModels) {
                mIndexOfFundAdapter.setData(fundModels);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
       observerStartInitData();
    }

    private void observerStartInitData(){
        mInvestmentModel.queryInsideIndex(INSIDE_TYPE);
        mInvestmentModel.queryOutsize(OUTSIZE_TYPE);
        mInvestmentModel.queryHKInside(HK_TYPE);
        mInvestmentModel.queryAllFund();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_investment_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.details:
                startActivity(new Intent(this, FundEditActivity.class));
                break;
            case R.id.edit_index:
                startActivity(new Intent(this, IndexEditActivity.class));
                break;
            case R.id.edit_data:
                readExcel("/storage/emulated/0/qpython/today_fund.xlsx");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void readExcel(String fileName) {
        try {
            InputStream inputStream = new FileInputStream(fileName);
            Workbook workbook;
            if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                ToastUtils.showShort("没找到数据!");
                return;
            }
            Sheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int r = 0; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                CellValue v0 = formulaEvaluator.evaluate(row.getCell(0));
                CellValue v1 = formulaEvaluator.evaluate(row.getCell(1));
                Log.i("Excel", "readExcel: " + v0.getStringValue() + "," + v1.getStringValue());
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

