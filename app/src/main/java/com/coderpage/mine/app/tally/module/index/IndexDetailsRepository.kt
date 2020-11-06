package com.coderpage.mine.app.tally.module.index

import com.coderpage.base.common.SimpleCallback
import com.coderpage.concurrency.MineExecutors
import com.coderpage.mine.app.tally.persistence.model.IndexModel
import com.coderpage.mine.app.tally.persistence.sql.TallyDatabase
import com.coderpage.mine.app.tally.utils.DateUtils

/**
 * create by ths on 2020/9/18
 */
class IndexDetailsRepository {

    private val mDatabase: TallyDatabase = TallyDatabase.getInstance()

    // 查询近一周涨幅
    fun queryLatelyWeek(type: String, indexName: String, simpleCallback: SimpleCallback<List<IndexModel>>) {
        MineExecutors.ioExecutor().execute {
            val indexModels = mDatabase.indexDao().getLatelyWeekIndex(type, indexName,DateUtils.getCurrentWeekOneDay(),DateUtils.getCurrentWeekLastDay())

            if (indexModels != null && indexModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(indexModels) }
            }
        }
    }

    // 查询历史数据
    fun getAllIndexData(type: String, indexName: String, simpleCallback: SimpleCallback<List<IndexModel>>) {
        MineExecutors.ioExecutor().execute {
            val indexModels = mDatabase.indexDao().queryAllIndex(type, indexName)

            if (indexModels != null && indexModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(indexModels) }
            }
        }
    }

    // 查询最近一个月数据
    fun getLatelyMonthRepository(type:String,indexName:String,simpleCallback: SimpleCallback<List<IndexModel>>){
        MineExecutors.ioExecutor().execute {
            var indexModels = mDatabase.indexDao().queryLatelyMonth(type,indexName,DateUtils.getCurrentMonthFirstDay(),DateUtils.getCurrentMonthLastDay())
            if(indexModels != null &&
                    indexModels.size > 0){
                MineExecutors.executeOnUiThread{simpleCallback.success(indexModels)}
            }
        }
    }

    // 查询最近几个月的数据
    fun getUnKnowMonthRepository(type:String,indexName:String,simpleCallback: SimpleCallback<List<IndexModel>>) {
        MineExecutors.ioExecutor().execute {
            var indexModels = mDatabase.indexDao().queryLatelyMonth(type, indexName, DateUtils.getUnKnowMonthFirstDay(-3), System.currentTimeMillis())
            if (indexModels != null &&
                    indexModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(indexModels) }
            }
        }
    }

    // 查询最近半年的数据
    fun getUnKnowHalfRepository(type:String,indexName:String,simpleCallback: SimpleCallback<List<IndexModel>>) {
        MineExecutors.ioExecutor().execute {
            var indexModels = mDatabase.indexDao().queryLatelyMonth(type, indexName, DateUtils.getUnKnowMonthFirstDay(-6), System.currentTimeMillis())
            if (indexModels != null &&
                    indexModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(indexModels) }
            }
        }
    }

    // 查询最近一年的数据
    fun getUnKnowYearRepository(type:String,indexName:String,simpleCallback: SimpleCallback<List<IndexModel>>) {
        MineExecutors.ioExecutor().execute {
            var indexModels = mDatabase.indexDao().queryLatelyMonth(type, indexName, DateUtils.getUnKnowYearOneDay(-1), System.currentTimeMillis())
            if (indexModels != null &&
                    indexModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(indexModels) }
            }
        }
    }

    // 查询最近三年的数据
    fun getThreeYearRepository(type:String,indexName:String,simpleCallback: SimpleCallback<List<IndexModel>>) {
        MineExecutors.ioExecutor().execute {
            var indexModels = mDatabase.indexDao().queryLatelyMonth(type, indexName, DateUtils.getUnKnowYearOneDay(-3), System.currentTimeMillis())
            if (indexModels != null &&
                    indexModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(indexModels) }
            }
        }
    }

    // 查询最近五年的数据
    fun getFiveYearRepository(type:String,indexName:String,simpleCallback: SimpleCallback<List<IndexModel>>) {
        MineExecutors.ioExecutor().execute {
            var indexModels = mDatabase.indexDao().queryLatelyMonth(type, indexName, DateUtils.getUnKnowYearOneDay(-5), System.currentTimeMillis())
            if (indexModels != null &&
                    indexModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(indexModels) }
            }
        }
    }
}
