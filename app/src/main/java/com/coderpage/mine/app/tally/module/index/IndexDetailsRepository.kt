package com.coderpage.mine.app.tally.module.index

import com.coderpage.base.common.SimpleCallback
import com.coderpage.concurrency.MineExecutors
import com.coderpage.mine.app.tally.persistence.model.IndexModel
import com.coderpage.mine.app.tally.persistence.sql.TallyDatabase

/**
 * create by ths on 2020/9/18
 */
class IndexDetailsRepository {

    private val mDatabase: TallyDatabase

    init {
        mDatabase = TallyDatabase.getInstance()
    }

    //查询近一周涨幅
    fun queryLatelyWeek(type: String, indexName: String, simpleCallback: SimpleCallback<List<IndexModel>>) {
        MineExecutors.ioExecutor().execute {
            val indexModels = mDatabase.indexDao().getLatelyWeekIndex(type, indexName)

            if (indexModels != null && indexModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(indexModels) }
            }
        }
    }

    //查询历史数据
    fun getAllIndexData(type: String, indexName: String, simpleCallback: SimpleCallback<List<IndexModel>>) {
        MineExecutors.ioExecutor().execute {
            val indexModels = mDatabase.indexDao().queryAllIndex(type, indexName)

            if (indexModels != null && indexModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(indexModels) }
            }
        }
    }
}
