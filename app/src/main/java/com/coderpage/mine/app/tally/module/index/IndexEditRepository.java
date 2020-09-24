package com.coderpage.mine.app.tally.module.index;

import com.coderpage.base.common.IError;
import com.coderpage.base.common.Result;
import com.coderpage.base.common.SimpleCallback;
import com.coderpage.concurrency.MineExecutors;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;
import com.coderpage.mine.app.tally.persistence.sql.TallyDatabase;

/**
 * create by ths on 2020/9/11
 */
public class IndexEditRepository {

    private TallyDatabase mDatabase;

    public IndexEditRepository(){
        mDatabase = TallyDatabase.getInstance();
    }

    public void saveIndex(IndexModel indexModel, SimpleCallback<Result<Long, IError>> simpleCallback){
        MineExecutors.ioExecutor().execute(() ->{
            long id = mDatabase.indexDao().insert(indexModel.createIndexEntity());
            MineExecutors.executeOnUiThread(() ->simpleCallback.success(new Result<>(id,null)));
        });
    }
}
