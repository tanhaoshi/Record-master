package com.coderpage.mine.app.tally.module.index;


import com.coderpage.base.common.IError;
import com.coderpage.base.common.Result;
import com.coderpage.base.common.SimpleCallback;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;
import com.coderpage.mine.app.tally.persistence.sql.TallyDatabase;
import com.coderpage.mine.app.tally.persistence.sql.entity.IndexEntity;

/**
 * create by ths on 2020/11/5
 */
public class IndexHistoryRepository {

    private TallyDatabase mDatabase;

    public IndexHistoryRepository(){
        if(mDatabase == null){
            mDatabase = TallyDatabase.getInstance();
        }
    }

    public void deleteRepository(IndexModel indexModel, SimpleCallback<Result<Long, IError>> callback){
        IndexEntity indexEntity = indexModel.createIndexEntity();
        mDatabase.indexDao().deleteIndexs(indexEntity.getId());
        callback.success(null);
    }
}
