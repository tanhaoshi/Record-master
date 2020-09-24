package com.coderpage.mine.app.tally.module.investment.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.coderpage.base.common.Callback;
import com.coderpage.base.common.IError;
import com.coderpage.mine.app.tally.module.investment.repository.InvestmentRepository;
import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;

import java.util.List;

/**
 * create by ths on 2020/9/7
 */
public class InvestmentModel extends AndroidViewModel implements LifecycleObserver {

    public MutableLiveData<List<IndexModel>> insideObserve = new MutableLiveData<>();

    public MutableLiveData<List<IndexModel>> HKObserve = new MutableLiveData<>();

    public MutableLiveData<List<IndexModel>> outsizeObserve = new MutableLiveData<>();

    public MutableLiveData<List<FundModel>> fundObserve = new MutableLiveData<>();

    private InvestmentRepository mRepository;

    public InvestmentModel(@NonNull Application application) {
        super(application);
        mRepository = new InvestmentRepository();
    }

    public void queryInsideIndex(String type){
        List<IndexModel> list = mRepository.queryDomestic(type);

        if(null != list){
            insideObserve.setValue(list);
        }
    }

    public void queryHKInside(String type){
        List<IndexModel> list = mRepository.queryDomestic(type);

        if(null != list){
            HKObserve.setValue(list);
        }
    }

    public void queryOutsize(String type){
        List<IndexModel> list = mRepository.queryDomestic(type);

        if(null != list){
            outsizeObserve.setValue(list);
        }
    }

    public void queryAllFund(){
         mRepository.queryAllFun(new Callback<List<FundModel>, IError>() {
            @Override
            public void success(List<FundModel> fundModels) {
                fundObserve.setValue(fundModels);
            }

            @Override
            public void failure(IError iError) {
                Log.i("InvestmentModel","look at error message = " + iError.msg());
            }
        });
    }
}
