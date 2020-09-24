package com.coderpage.mine.app.tally.module.index;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.coderpage.base.common.IError;
import com.coderpage.base.common.Result;
import com.coderpage.base.common.SimpleCallback;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;

/**
 * create by ths on 2020/9/11
 */
public class IndexEditViewModel extends AndroidViewModel implements LifecycleObserver {

    public MutableLiveData<Boolean> mLiveData = new MutableLiveData<>();

    private IndexEditRepository mRepository;

    public IndexEditViewModel(@NonNull Application application) {
        super(application);
        mRepository = new IndexEditRepository();
    }

    public void saveIndexData(IndexModel indexModel){
        mRepository.saveIndex(indexModel, new SimpleCallback<Result<Long, IError>>() {
            @Override
            public void success(Result<Long, IError> longIErrorResult){
                Log.i("IndexEditViewModel","look at current insert result = " + longIErrorResult.data());
                mLiveData.setValue(true);
            }
        });
    }
}
