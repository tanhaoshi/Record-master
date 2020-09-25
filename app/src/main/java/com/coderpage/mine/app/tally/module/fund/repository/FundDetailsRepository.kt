package com.coderpage.mine.app.tally.module.fund.repository

import android.util.Log
import com.alibaba.fastjson.JSON
import com.coderpage.base.common.SimpleCallback
import com.coderpage.concurrency.MineExecutors
import com.coderpage.mine.app.tally.persistence.model.FundModel
import com.coderpage.mine.app.tally.persistence.model.IndexModel
import com.coderpage.mine.app.tally.persistence.sql.TallyDatabase
import com.coderpage.mine.app.tally.utils.DateUtils

/**
 * create by ths on 2020/9/25
 */
class FundDetailsRepository{

    private val mDatabase : TallyDatabase = TallyDatabase.getInstance()

    //查询近一周涨幅
    fun queryLatelyWeek(type: String, indexName: String, simpleCallback: SimpleCallback<List<FundModel>>) {
        MineExecutors.ioExecutor().execute {
            val fundModels = mDatabase.fundDisposeDao().queryLatelyData(type, indexName, DateUtils.getCurrentWeekOneDay(), DateUtils.getCurrentWeekLastDay())

            Log.i("IndexDetailsRepository","look at response json data = " + JSON.toJSONString(fundModels))

            if (fundModels != null && fundModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(fundModels) }
            }
        }
    }

    //查询历史数据
    fun getAllFundData(type: String, fundName: String, simpleCallback: SimpleCallback<List<FundModel>>) {
        MineExecutors.ioExecutor().execute {
            val fundModels = mDatabase.fundDisposeDao().queryAllFund(type, fundName)

            if (fundModels != null && fundModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(fundModels) }
            }
        }
    }

    //查询最近一个月数据
    fun getLatelyMonthRepository(type:String,fundName:String,simpleCallback: SimpleCallback<List<FundModel>>){
        MineExecutors.ioExecutor().execute {
            var fundModels = mDatabase.fundDisposeDao().queryLatelyData(type,fundName,DateUtils.getCurrentMonthFirstDay(),DateUtils.getCurrentMonthLastDay())
            Log.i("IndexDetailsRepository","look at response json month data = " + JSON.toJSONString(fundModels))
            if(fundModels != null &&
                    fundModels.size > 0){
                MineExecutors.executeOnUiThread{simpleCallback.success(fundModels)}
            }
        }
    }

    //查询最近几个月的数据
    fun getUnKnowMonthRepository(type:String,fundName:String,simpleCallback: SimpleCallback<List<FundModel>>) {
        MineExecutors.ioExecutor().execute {
            var indexModels = mDatabase.fundDisposeDao().queryLatelyData(type, fundName, DateUtils.getUnKnowMonthFirstDay(-3), System.currentTimeMillis())
            Log.i("IndexDetailsRepository", "look at response json month data = " + JSON.toJSONString(indexModels))
            if (indexModels != null &&
                    indexModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(indexModels) }
            }
        }
    }

    // 查询最近半年的数据
    fun getUnKnowHalfRepository(type:String,fundName:String,simpleCallback: SimpleCallback<List<FundModel>>) {
        MineExecutors.ioExecutor().execute {
            var fundModels = mDatabase.fundDisposeDao().queryLatelyData(type, fundName, DateUtils.getUnKnowMonthFirstDay(-6), System.currentTimeMillis())
            Log.i("IndexDetailsRepository", "look at response json month data = " + JSON.toJSONString(fundModels))
            if (fundModels != null &&
                    fundModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(fundModels) }
            }
        }
    }

    // 查询最近一年的数据
    fun getUnKnowYearRepository(type:String,fundName:String,simpleCallback: SimpleCallback<List<FundModel>>) {
        MineExecutors.ioExecutor().execute {
            var fundModels = mDatabase.fundDisposeDao().queryLatelyData(type, fundName, DateUtils.getUnKnowYearOneDay(-1), System.currentTimeMillis())
            Log.i("IndexDetailsRepository", "look at response json month data = " + JSON.toJSONString(fundModels))
            if (fundModels != null &&
                    fundModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(fundModels) }
            }
        }
    }

    // 查询最近三年的数据
    fun getThreeYearRepository(type:String,fundName:String,simpleCallback: SimpleCallback<List<FundModel>>) {
        MineExecutors.ioExecutor().execute {
            var fundModels = mDatabase.fundDisposeDao().queryLatelyData(type, fundName, DateUtils.getUnKnowYearOneDay(-3), System.currentTimeMillis())
            Log.i("IndexDetailsRepository", "look at response json month data = " + JSON.toJSONString(fundModels))
            if (fundModels != null &&
                    fundModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(fundModels) }
            }
        }
    }

    // 查询最近五年的数据
    fun getFiveYearRepository(type:String,fundName:String,simpleCallback: SimpleCallback<List<FundModel>>) {
        MineExecutors.ioExecutor().execute {
            var fundModels = mDatabase.fundDisposeDao().queryLatelyData(type, fundName, DateUtils.getUnKnowYearOneDay(-5), System.currentTimeMillis())
            Log.i("IndexDetailsRepository", "look at response json month data = " + JSON.toJSONString(fundModels))
            if (fundModels != null &&
                    fundModels.size > 0) {
                MineExecutors.executeOnUiThread { simpleCallback.success(fundModels) }
            }
        }
    }
}
