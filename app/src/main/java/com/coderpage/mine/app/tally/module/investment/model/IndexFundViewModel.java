package com.coderpage.mine.app.tally.module.investment.model;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.coderpage.base.common.IError;
import com.coderpage.base.common.Result;
import com.coderpage.base.common.SimpleCallback;
import com.coderpage.base.utils.UIUtils;
import com.coderpage.mine.app.tally.module.investment.repository.InvestmentRepository;
import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.ui.dialog.FundEditIndexDialog;

/**
 * create by ths on 2020/9/16
 */
public class IndexFundViewModel extends AndroidViewModel implements LifecycleObserver {

    private InvestmentRepository mRepository;

    public  MutableLiveData<Boolean> observerUpdate = new MutableLiveData<>();

    public IndexFundViewModel(@NonNull Application application) {
        super(application);
        mRepository = new InvestmentRepository();
    }

    public void onItemClick(View view, Activity activity, FundModel fundModel){
        new FundEditIndexDialog(activity).setListener((dialog, percent,rangeType) -> {
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
                    UIUtils.hideSoftKeyboard(activity,view);
                    observerUpdate.setValue(true);
                }
            });
            dialog.dismiss();
        }).show();
    }
}
