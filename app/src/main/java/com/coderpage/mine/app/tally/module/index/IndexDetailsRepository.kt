package com.coderpage.mine.app.tally.module.index

import android.util.Log
import com.alibaba.fastjson.JSON
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

            Log.i("IndexDetailsRepository","look at response json data = " + JSON.toJSONString(indexModels))

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

    //查询最近一个月数据
    fun getLatelyMonthRepository(type:String,indexName:String,simpleCallback: SimpleCallback<List<IndexModel>>){
        MineExecutors.ioExecutor().execute {
            var indexModels = mDatabase.indexDao().queryLatelyMonth(type,indexName)
            Log.i("IndexDetailsRepository","look at response json month data = " + JSON.toJSONString(indexModels))
            if(indexModels != null && indexModels.size > 0){
                MineExecutors.executeOnUiThread{simpleCallback.success(indexModels)}
            }
        }
    }
}
