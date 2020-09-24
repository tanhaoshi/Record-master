package com.coderpage.mine.app.tally.module.fund.model;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.coderpage.base.common.Callback;
import com.coderpage.base.common.IError;
import com.coderpage.base.common.Result;
import com.coderpage.base.common.SimpleCallback;
import com.coderpage.mine.app.tally.module.fund.repository.FundRepository;
import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.ui.dialog.FundEditDialog;

import java.util.List;

/**
 * create by ths on 2020/9/9
 */
public class FundViewModel extends AndroidViewModel implements LifecycleObserver {

    private FundRepository mRepository;

    public MutableLiveData<List<FundModel>> mLiveData = new MutableLiveData<>();

    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public FundViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FundRepository();
    }

    public void initData() {
        mRepository.queryAllFun(new Callback<List<FundModel>, IError>() {
            @Override
            public void success(List<FundModel> fundModels) {
                mLiveData.postValue(fundModels);
            }

            @Override
            public void failure(IError iError) {
                errorMessage.postValue(iError.msg());
            }
        });
    }

    private void saveData(FundModel fundModel){
        if(fundModel == null){
            return;
        }

        mRepository.saveFund(fundModel, new SimpleCallback<Result<Long, IError>>() {
            @Override
            public void success(Result<Long, IError> longIErrorResult) {
                initData();
            }
        });
    }

    public void onAddNewRecordClick(Activity activity) {
        new FundEditDialog(activity).setListener((dialog, name,code) -> {
            FundModel fundModel = new FundModel();
            fundModel.setFundName(name);
            fundModel.setFundNumber(code);
            fundModel.setFundSyncId(System.currentTimeMillis());
            fundModel.setTime(System.currentTimeMillis());
            fundModel.setFundType("1");
            fundModel.setFundPercent("0");
            saveData(fundModel);
            dialog.dismiss();
        }).show();
    }

}
