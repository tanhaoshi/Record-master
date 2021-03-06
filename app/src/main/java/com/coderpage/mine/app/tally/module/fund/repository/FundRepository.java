package com.coderpage.mine.app.tally.module.fund.repository;

import com.coderpage.base.common.Callback;
import com.coderpage.base.common.IError;
import com.coderpage.base.common.Result;
import com.coderpage.base.common.SimpleCallback;
import com.coderpage.concurrency.MineExecutors;
import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.persistence.sql.TallyDatabase;

import java.util.List;

/**
 * create by ths on 2020/9/9
 */
public class FundRepository {

    private TallyDatabase mDataBase;

    public FundRepository() {
        mDataBase = TallyDatabase.getInstance();
    }

    public void queryAllFun(Callback<List<FundModel>, IError> callback){
        MineExecutors.ioExecutor().execute(() ->{
            List<FundModel> fundModels = mDataBase.fundDisposeDao().getAllFund();
            callback.success(fundModels);
        });
    }

    public void saveFund(FundModel fundModel, SimpleCallback<Result<Long, IError>> callback){
        MineExecutors.ioExecutor().execute(() ->{
            long id = mDataBase.fundDisposeDao().insert(fundModel.createEntity());
            MineExecutors.executeOnUiThread(() ->callback.success(new Result<>(id,null)));
        });
    }

    public void deleteAllOfRepository(){
        mDataBase.fundDisposeDao().deleteAll();
    }

    public void updateRepository(FundModel fundModel,SimpleCallback<Result<Long,IError>> callback){
        mDataBase.fundDisposeDao().update(fundModel.createEntity());
        callback.success(null);
    }
}
