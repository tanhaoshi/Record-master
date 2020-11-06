package com.coderpage.mine.app.tally.module.fund.repository

import com.coderpage.base.common.IError
import com.coderpage.base.common.Result
import com.coderpage.base.common.SimpleCallback
import com.coderpage.mine.app.tally.persistence.model.FundModel
import com.coderpage.mine.app.tally.persistence.sql.TallyDatabase

/**
 * create by ths on 2020/11/5
 */
class FundDetailsADRepository {

    val mDataBase : TallyDatabase = TallyDatabase.getInstance()

    fun deleteFundDetails(fundModel: FundModel,simpleCallback: SimpleCallback<Result<Long,IError>>){
        mDataBase.fundDisposeDao().deleteFund(fundModel.createEntity())
        simpleCallback.success(null)
    }
}