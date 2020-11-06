package com.coderpage.mine.app.tally.module.investment.model;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.coderpage.base.common.IError;
import com.coderpage.base.common.Result;
import com.coderpage.base.common.SimpleCallback;
import com.coderpage.base.utils.UIUtils;
import com.coderpage.mine.app.tally.module.fund.FundEditActivity;
import com.coderpage.mine.app.tally.module.investment.repository.InvestmentRepository;
import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;
import com.coderpage.mine.app.tally.ui.dialog.FundEditIndexDialog;
import com.coderpage.mine.app.tally.ui.dialog.PermissionReqDialog;

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
        Intent intent = new Intent(activity, FundEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("fundModel",fundModel);
        intent.putExtra("bundle",bundle);
        activity.startActivity(intent);
    }

    public void onItemLongClick(Activity activity,FundModel fundModel){
        new PermissionReqDialog(activity,"是否确定删除该条数据","请确认!")
                .setTitleText("温馨提示!")
                .setPositiveText("确认")
                .setListener(new PermissionReqDialog.Listener() {
                    @Override
                    public void onCancelClick(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onConfirmClick(DialogInterface dialog) {
                        deleteViewModel(fundModel);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void insertFundData(FundModel fundModel){
        mRepository.saveFund(fundModel, new SimpleCallback<Result<Long, IError>>() {
            @Override
            public void success(Result<Long, IError> longIErrorResult) {
//                observerUpdate.setValue(true);
            }
        });
    }

    public void updateFund(){
        observerUpdate.setValue(true);
    }

    public void deleteViewModel(FundModel fundModel){
        mRepository.deleteRepository(fundModel, new SimpleCallback<Result<Long, IError>>() {
            @Override
            public void success(Result<Long, IError> longIErrorResult) {
                observerUpdate.setValue(true);
            }
        });
    }
}
