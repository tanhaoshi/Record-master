package com.coderpage.mine.app.tally.module.investment.repository;



import com.coderpage.base.common.Callback;
import com.coderpage.base.common.IError;
import com.coderpage.base.common.Result;
import com.coderpage.base.common.SimpleCallback;
import com.coderpage.concurrency.MineExecutors;
import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;
import com.coderpage.mine.app.tally.persistence.sql.TallyDatabase;

import java.util.List;


/**
 * create by ths on 2020/9/9
 */
public class InvestmentRepository {

    private TallyDatabase mDataBase;

    public InvestmentRepository() {
        mDataBase = TallyDatabase.getInstance();
    }

    public List<IndexModel> queryDomestic(String type){
        List<IndexModel> list = mDataBase.indexDao().getInsideIndex(type);
        if(null != list && list.size() > 0){
            return list;
        }else{
            return null;
        }
    }

    public void queryAllFun(Callback<List<FundModel>, IError> callback){
        MineExecutors.ioExecutor().execute(() ->{
            List<FundModel> fundModels = mDataBase.fundDisposeDao().getAllFund();

            if(null != fundModels && fundModels.size() > 0){
                MineExecutors.executeOnUiThread(() -> callback.success(fundModels));
            }
        });
    }

    public void saveFund(FundModel fundModel, SimpleCallback<Result<Long, IError>> callback){
        MineExecutors.ioExecutor().execute(() ->{
            long id = mDataBase.fundDisposeDao().insert(fundModel.createEntity());
            MineExecutors.executeOnUiThread(() ->callback.success(new Result<>(id,null)));
        });
    }
}
