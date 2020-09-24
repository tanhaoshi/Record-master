package com.coderpage.mine.app.tally.module.fund;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.coderpage.base.utils.UIUtils;
import com.coderpage.mine.R;
import com.coderpage.mine.app.tally.module.fund.adapter.AddFundAdapter;
import com.coderpage.mine.app.tally.module.fund.model.FundViewModel;
import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.ui.refresh.RefreshHeadView;
import com.coderpage.mine.app.tally.utils.KeyboardUtils;
import com.coderpage.mine.ui.BaseActivity;
import com.coderpage.mine.ui.widget.recyclerview.ItemMarginDecoration;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

public class FundEditActivity extends BaseActivity {

    private FundViewModel           mFundViewModel;
    private FundEditActivityBinding mBinding;
    private AddFundAdapter          mAddFundAdapter;
    private RecyclerView            mRecyclerView;
    private TwinklingRefreshLayout  mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_fund_edit);
        mFundViewModel = ViewModelProviders.of(this).get(FundViewModel.class);
        getLifecycle().addObserver(mFundViewModel);

        setToolbarTitle(getResources().getString(R.string.add_fund));
        setToolbarAsBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KeyboardUtils.isSoftInputVisible(FundEditActivity.this)){
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                }
                finish();
            }
        });

        initView();

        subscribeUi();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFundViewModel.initData();
    }

    private void initView() {
        mRecyclerView  = mBinding.recyclerView;
        mRefreshLayout = mBinding.refreshLayout;
        mRefreshLayout.setHeaderView(new RefreshHeadView(this));

        ItemMarginDecoration itemMarginDecoration = new ItemMarginDecoration(0, 0, 0, 0);
        // 最后一个 ITEM 距离底部距离大一些，防止被底部按钮遮挡
        itemMarginDecoration.setLastItemOffset(0, 0, 0, UIUtils.dp2px(this, 80));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.addItemDecoration(itemMarginDecoration);
        mRecyclerView.setLayoutManager(layoutManager);

        mAddFundAdapter = new AddFundAdapter(this);

        mRecyclerView.setAdapter(mAddFundAdapter);
    }

    private void subscribeUi() {
        mBinding.setActivity(this);
        mBinding.setVm(mFundViewModel);

        mFundViewModel.mLiveData.observe(this, new Observer<List<FundModel>>() {
            @Override
            public void onChanged(@Nullable List<FundModel> fundModels) {
                mAddFundAdapter.setData(fundModels);
            }
        });
    }

}
