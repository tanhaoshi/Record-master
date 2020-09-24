package com.coderpage.mine.app.tally.module.fund.model;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.coderpage.base.common.IError;
import com.coderpage.base.common.Result;
import com.coderpage.base.common.SimpleCallback;
import com.coderpage.mine.app.tally.module.fund.repository.FundRepository;
import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.ui.dialog.FundEditIndexDialog;
import com.coderpage.mine.app.tally.ui.dialog.SetBudgetMonthDialog;

/**
 * create by ths on 2020/9/10
 */
public class FundAdapterViewModel extends AndroidViewModel implements LifecycleObserver {

    private FundRepository mRepository;

    public FundAdapterViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FundRepository();
    }

    public void onItemClick(View view, Activity activity, FundModel fundModel){
        new FundEditIndexDialog(activity).setListener((dialog,percent,rangeType) -> {
            FundModel model = new FundModel();
            model.setFundName(fundModel.getFundName());
            model.setFundNumber(fundModel.getFundNumber());
            model.setTime(System.currentTimeMillis());
            model.setFundType("1");
            model.setFundPercent(percent);
            model.setFundSyncId(System.currentTimeMillis());
            model.setFundIncreaseType(Integer.valueOf(rangeType));
            mRepository.saveFund(model, new SimpleCallback<Result<Long, IError>>() {
                @Override
                public void success(Result<Long, IError> longIErrorResult) {
                    Log.i("FundAdapter","look at insert result = " + longIErrorResult.data());
                }
            });
            dialog.dismiss();
        }).show();
    }
}
