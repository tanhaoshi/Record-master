package com.coderpage.mine.app.tally.module.fund;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.coderpage.mine.R;
import com.coderpage.mine.app.tally.module.fund.model.FundViewModel;
import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.utils.KeyboardUtils;
import com.coderpage.mine.ui.BaseActivity;

import static android.widget.Toast.LENGTH_SHORT;


public class FundEditActivity extends BaseActivity implements View.OnClickListener{

    private FundViewModel           mFundViewModel;
    private FundEditActivityBinding mBinding;

    private FundModel mFundModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_fund_edit);
        mFundViewModel = ViewModelProviders.of(this).get(FundViewModel.class);
        getLifecycle().addObserver(mFundViewModel);

        setToolbarTitle("编辑基金");
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

        getIntentData();
        subscribeUi();
        initView();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if(bundle != null){
            mFundModel = (FundModel) bundle.getSerializable("fundModel");
        }
    }

    private void initView() {
        if(null != mFundModel){
            mBinding.fundName.setText(mFundModel.getFundName());
            mBinding.indexArea.setText(mFundModel.getFundYesterdayWorth());
            mBinding.currentIndex.setText(mFundModel.getFundTodayWorth());
            mBinding.currentType.setText(String.valueOf(mFundModel.getFundIncreaseType()) + "(0为涨1为跌)");
            mBinding.currentRange.setText(mFundModel.getFundNetProfit());
            mBinding.currentPercent.setText(mFundModel.getFundPercent());
        }
    }

    private void subscribeUi() {
        mFundViewModel.alertMessage.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean){
                    ToastUtils.showShort("更新成功!");
                    finish();
                }
            }
        });

        mBinding.tvConfirm.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fund_edit,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.edit_details:
                Intent intent = new Intent(FundEditActivity.this, FundDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("fundModel",mFundModel);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvConfirm:
                submit();
                break;
        }
    }

    private void submit(){
        if(mBinding.indexArea.getText().toString().length() == 0){
            Toast.makeText(this,"昨日净值必须填", LENGTH_SHORT).show();
            return;
        }

        if(mBinding.currentIndex.getText().toString().length() == 0){
            Toast.makeText(this,"当前净值必须填写", LENGTH_SHORT).show();
            return;
        }

        if(mBinding.currentRange.getText().toString().length() == 0){
            Toast.makeText(this,"日涨跌净值必须填写", LENGTH_SHORT).show();
            return;
        }

        if(mBinding.currentPercent.getText().toString().length() == 0){
            Toast.makeText(this,"日涨幅百分比必须填写", LENGTH_SHORT).show();
            return;
        }

        if(mBinding.currentType.getText().toString().length() < 1){
            Toast.makeText(this,"日涨跌类型必须填写", LENGTH_SHORT).show();
            return;
        }

        FundModel fundModel = new FundModel();
        fundModel.setId(mFundModel.getId());
        fundModel.setFundNetProfit(String.valueOf(Double.valueOf(mBinding.currentIndex.getText().toString()) - Double.valueOf(mBinding.indexArea.getText().toString())));
        fundModel.setFundName(mBinding.fundName.getText().toString());
        fundModel.setFundYesterdayWorth(mBinding.indexArea.getText().toString());
        fundModel.setFundTodayWorth(mBinding.currentIndex.getText().toString());
        fundModel.setFundPercent(mBinding.currentPercent.getText().toString());
        fundModel.setFundIncreaseType(Integer.valueOf(mBinding.currentType.getText().toString().substring(0,1)));
        fundModel.setFundRange(mBinding.currentRange.getText().toString());

        mFundViewModel.updateViewModel(fundModel);
    }
}
