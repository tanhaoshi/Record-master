package com.coderpage.mine.app.tally.module.index

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.util.Log

import com.alibaba.fastjson.JSON
import com.coderpage.base.common.SimpleCallback
import com.coderpage.mine.app.tally.persistence.model.IndexModel

/**
 * create by ths on 2020/9/18
 */
class IndexDetailsViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {

    var indexWeekObserver = MutableLiveData<String>()

    var indexMouthObserver = MutableLiveData<String>()

    var indexThreeMouthObserver = MutableLiveData<String>()

    var indexHalfYearObserver = MutableLiveData<String>()

    var indexYearObserver = MutableLiveData<String>()

    var indexThreeYearObserver = MutableLiveData<String>()

    var indexFiveYearObserver = MutableLiveData<String>()

    var indexStartObserver = MutableLiveData<String>()

    var historyObserver = MutableLiveData<List<IndexModel>>()

    var mRepository: IndexDetailsRepository

    init {
        mRepository = IndexDetailsRepository()
    }

    // 查询最近一周数据 涨幅情况
    fun getLatelyWeek(type: String, indexName: String) {
        mRepository.queryLatelyWeek(type, indexName, SimpleCallback { indexModels ->
            var tempValue : Double = 0.0

            for (indexModel in indexModels){
                if(indexModel.indexIncreaseType == 0){
                    tempValue += (indexModel.indexPercent.toDouble())
                }else{
                    tempValue -= (indexModel.indexPercent.toDouble())
                }
            }
            indexWeekObserver.value = tempValue.toString()
        })
    }

    // 查询历史数据
    fun getHistoryData(type: String, indexName: String) {
        mRepository.getAllIndexData(type, indexName, SimpleCallback { indexModels ->
            historyObserver.value = indexModels
        })
    }

    // 查询最近一个月数据 涨幅情况
    fun getLatelyMonth(type:String,indexName:String){
        mRepository.getLatelyMonthRepository(type,indexName, SimpleCallback { indexModels ->
            var tempValue : Double = 0.0

            for (indexModel in indexModels){
                if(indexModel.indexIncreaseType == 0){
                    tempValue += (indexModel.indexPercent.toDouble())
                }else{
                    tempValue -= (indexModel.indexPercent.toDouble())
                }
            }
            indexMouthObserver.value = tempValue.toString()
        })
    }
}
