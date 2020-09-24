package com.coderpage.mine.app.tally.module.investment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
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
        }
        return super.onOptionsItemSelected(item);
    }
}

