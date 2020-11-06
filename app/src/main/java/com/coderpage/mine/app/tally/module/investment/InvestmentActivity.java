package com.coderpage.mine.app.tally.module.investment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
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
import com.coderpage.concurrency.MineExecutors;
import com.coderpage.mine.R;
import com.coderpage.mine.app.tally.common.router.TallyRouter;
import com.coderpage.mine.app.tally.module.investment.adapter.IndexHKAdapter;
import com.coderpage.mine.app.tally.module.investment.adapter.IndexInsideAdapter;
import com.coderpage.mine.app.tally.module.investment.adapter.IndexOfFundAdapter;
import com.coderpage.mine.app.tally.module.investment.adapter.IndexOutsideAdapter;
import com.coderpage.mine.app.tally.module.investment.model.IndexFundViewModel;
import com.coderpage.mine.app.tally.module.investment.model.InvestmentModel;
import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;
import com.coderpage.mine.app.tally.ui.dialog.PermissionReqDialog;
import com.coderpage.mine.ui.BaseActivity;
import com.coderpage.mine.utils.AndroidUtils;
import com.coderpage.network.NetService;
import com.coderpage.network.NetWorkUtils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

@Route(path = TallyRouter.INVESTMENT_PATH)
public class InvestmentActivity extends BaseActivity {

    /**  国内指数 */
    public static final String INSIDE_TYPE  = "1";

    /** 国外指数 */
    public static final String OUTSIZE_TYPE = "2";

    /**  外围指数  */
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
            case R.id.edit_fund_data:
                editFundDataDialog();
                break;
            case R.id.edit_index_data:
                editIndexDataDialog();
                break;
            case R.id.edit_index_delete:
                mInvestmentModel.deleteIndex();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editFundDataDialog(){
        new PermissionReqDialog(this,"是否确定导入基金数据","请确认导入是否有重复的数据")
                .setTitleText("温馨提示!")
                .setPositiveText("确认")
                .setListener(new PermissionReqDialog.Listener() {
                    @Override
                    public void onCancelClick(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onConfirmClick(DialogInterface dialog) {
                        readExcel("/storage/emulated/0/qpython/today_fund.xlsx");
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void editIndexDataDialog(){
        new PermissionReqDialog(this,"是否确定导入指数数据","请确认导入是否有重复的数据")
                .setTitleText("温馨提示!")
                .setPositiveText("确认")
                .setListener(new PermissionReqDialog.Listener() {
                    @Override
                    public void onCancelClick(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onConfirmClick(DialogInterface dialog) {
                        readIndexData();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void readExcel(String fileName){
        showProcessDialog("数据加载中!");
        MineExecutors.ioExecutor().execute(() ->{
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
                for (int r = 1; r < rowsCount; r++) {
                    Row row = sheet.getRow(r);
                    CellValue v0 = formulaEvaluator.evaluate(row.getCell(0));
                    CellValue v1 = formulaEvaluator.evaluate(row.getCell(1));
                    CellValue v2 = formulaEvaluator.evaluate(row.getCell(2));
                    CellValue v3 = formulaEvaluator.evaluate(row.getCell(3));
                    CellValue v4 = formulaEvaluator.evaluate(row.getCell(4));
                    CellValue v5 = formulaEvaluator.evaluate(row.getCell(5));
                    CellValue v6 = formulaEvaluator.evaluate(row.getCell(6));
                    CellValue v7 = formulaEvaluator.evaluate(row.getCell(7));
                    CellValue v8 = formulaEvaluator.evaluate(row.getCell(8));

                    FundModel fundModel = new FundModel();
                    fundModel.setFundName(v1.getStringValue());
                    fundModel.setFundNumber(v0.getStringValue());
                    // 插入数据时 当天 数据不一定就能全部更新, 所以我们将更新数据动作 放到了第二天进行
                    // 第二天的任意时刻,都在这个基础上 定位为昨天的今天这个时间,比如我们今天9点钟左右导入数据，
                    // 实际上是昨天一天的数据
                    fundModel.setTime(System.currentTimeMillis() - 86400000);
                    fundModel.setFundType("1");
                    fundModel.setFundPercent(v5 == null ? "" : v5.getStringValue());
                    fundModel.setFundSyncId(System.currentTimeMillis());
                    fundModel.setFundIncreaseType(v5 == null ? 0 : Double.valueOf(v5.getStringValue()) > 0 ? 0 : 1);
                    fundModel.setFundYesterdayWorth(v2 == null ? "" : v2.getStringValue());
                    fundModel.setFundTodayWorth(v3 == null ? "" : v3.getStringValue());
                    fundModel.setFundNetProfit(v4 == null ? "" : v4.getStringValue());
                    fundModel.setFundApplyStatus(v6 == null ? "" : v6.getStringValue());
                    fundModel.setFundRedeemStatus(v7 == null ? "" : v7.getStringValue());
                    fundModel.setFundServiceCharge(v8 == null ? "" : v8.getStringValue());

                    mFundViewModel.insertFundData(fundModel);
                }
                workbook.close();
                MineExecutors.executeOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFundViewModel.updateFund();
                        dismissProcessDialog();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private volatile int count = 0;

//    String[] ind = {"股票名称","今日收盘指数","指数涨幅","涨幅百分比","成交多少手","成交金额"};

    private void readIndexData(){
        showProcessDialog("数据加载中!");
        //上证
        httpClientForInside("http://hq.sinajs.cn/list=s_sh000001");//你要访问的股票
        //深证成指
        httpClientForInside("http://hq.sinajs.cn/list=s_sz399001");//你要访问的股票
        //创业板指
        httpClientForInside("http://hq.sinajs.cn/list=s_sz399006");
        //道琼斯指
        httpClientForOutside("http://hq.sinajs.cn/list=int_dji");
        //纳斯达克指数
        httpClientForOutside("http://hq.sinajs.cn/list=int_nasdaq");
        //恒生指数
        httpClientForOutside("http://hq.sinajs.cn/list=int_hangseng");
        //沪深300
        httpClientForHK("http://hq.sinajs.cn/list=s_sh000300");
        //中证500
        httpClientForHK("http://hq.sinajs.cn/list=s_sh000905");
        //科创50
        httpClientForHK("http://hq.sinajs.cn/list=s_sh000688");
    }

    private String httpClientForInside(String ssUrl){
        NetWorkUtils.getInstance().createService(NetService.class)
                .getIndexList(ssUrl)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try{
                            String content = responseBody.string();
                            String[] fcs = content.split("\"");          //按"号分割字符
                            String scs = fcs[1];
                            StringBuffer sb = new StringBuffer(scs);
                            sb.insert(scs.length(),",0");
                            String[] tcs = sb.toString().split(",");          //按,号分割字符

                            IndexModel indexModel = new IndexModel();
                            indexModel.setFundSyncId(System.currentTimeMillis());
                            indexModel.setIndexName(tcs[0]);
                            indexModel.setIndexType(INSIDE_TYPE);
                            indexModel.setIndexNumber(AndroidUtils.formatDouble2(Double.valueOf(tcs[1])));
                            indexModel.setIndexRange(AndroidUtils.formatDouble2(Double.valueOf(tcs[2])));
                            indexModel.setIndexPercent(tcs[3]);
                            indexModel.setIndexIncreaseType(Double.valueOf(tcs[2]) > 0 ? 0 : 1);
                            indexModel.setIndexYesNumber(AndroidUtils.formatDouble2(Double.valueOf(tcs[1]) - Double.valueOf(tcs[2])));
                            indexModel.setIndexDealNumber(tcs[4]);
                            indexModel.setIndexDealAmount(tcs[5]);

                            mInvestmentModel.insertIndexData(indexModel);

                            count = count + 1;

                            if(count == 9){
                                mInvestmentModel.queryInsideIndex(INSIDE_TYPE);
                                mInvestmentModel.queryOutsize(OUTSIZE_TYPE);
                                mInvestmentModel.queryHKInside(HK_TYPE);
                                dismissProcessDialog();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("InvestmentActivity","error message = " + e.getMessage().toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
       return "";
    }

    private String httpClientForOutside(String url){
        NetWorkUtils.getInstance().createService(NetService.class)
                .getIndexList(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try{
                            String content = responseBody.string();
                            String[] fcs = content.split("\"");          //按"号分割字符
                            String scs = fcs[1];
                            StringBuffer sb = new StringBuffer(scs);
                            sb.insert(scs.length(),",0");
                            String[] tcs = sb.toString().split(",");          //按,号分割字符

                            IndexModel indexModel = new IndexModel();
                            indexModel.setFundSyncId(System.currentTimeMillis());
                            indexModel.setIndexName(tcs[0]);
                            indexModel.setIndexType(OUTSIZE_TYPE);
                            indexModel.setIndexNumber(AndroidUtils.formatDouble2(Double.valueOf(tcs[1])));
                            indexModel.setIndexRange(AndroidUtils.formatDouble2(Double.valueOf(tcs[2])));
                            indexModel.setIndexPercent(tcs[3].replace("%",""));
                            indexModel.setIndexIncreaseType(Double.valueOf(tcs[2]) > 0 ? 0 : 1);
                            indexModel.setIndexYesNumber(AndroidUtils.formatDouble2(Double.valueOf(tcs[1]) - Double.valueOf(tcs[2])));

                            mInvestmentModel.insertIndexData(indexModel);

                            count = count + 1;

                            if(count == 9){
                                mInvestmentModel.queryInsideIndex(INSIDE_TYPE);
                                mInvestmentModel.queryOutsize(OUTSIZE_TYPE);
                                mInvestmentModel.queryHKInside(HK_TYPE);
                                dismissProcessDialog();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("InvestmentActivity","error message = " + e.getMessage().toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return "";
    }

    public String httpClientForHK(String url){
        NetWorkUtils.getInstance().createService(NetService.class)
                .getIndexList(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try{
                            String content = responseBody.string();
                            String[] fcs = content.split("\"");          //按"号分割字符
                            String scs = fcs[1];
                            StringBuffer sb = new StringBuffer(scs);
                            sb.insert(scs.length(),",0");
                            String[] tcs = sb.toString().split(",");          //按,号分割字符

                            IndexModel indexModel = new IndexModel();
                            indexModel.setFundSyncId(System.currentTimeMillis());
                            indexModel.setIndexName(tcs[0]);
                            indexModel.setIndexType(HK_TYPE);
                            indexModel.setIndexNumber(AndroidUtils.formatDouble2(Double.valueOf(tcs[1])));
                            indexModel.setIndexRange(AndroidUtils.formatDouble2(Double.valueOf(tcs[2])));
                            indexModel.setIndexPercent(tcs[3]);
                            indexModel.setIndexIncreaseType(Double.valueOf(tcs[2]) > 0 ? 0 : 1);
                            indexModel.setIndexYesNumber(AndroidUtils.formatDouble2(Double.valueOf(tcs[1]) - Double.valueOf(tcs[2])));
                            indexModel.setIndexDealNumber(tcs[4]);
                            indexModel.setIndexDealAmount(tcs[5]);

                            mInvestmentModel.insertIndexData(indexModel);

                            count = count + 1;

                            if(count == 9){
                                mInvestmentModel.queryInsideIndex(INSIDE_TYPE);
                                mInvestmentModel.queryOutsize(OUTSIZE_TYPE);
                                mInvestmentModel.queryHKInside(HK_TYPE);
                                dismissProcessDialog();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("InvestmentActivity","error message = " + e.getMessage().toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return "";
    }
}

