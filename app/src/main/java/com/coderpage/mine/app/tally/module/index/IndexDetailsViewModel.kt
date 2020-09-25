package com.coderpage.mine.app.tally.module.index

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.util.Log

import com.alibaba.fastjson.JSON
import com.coderpage.base.common.SimpleCallback
import com.coderpage.mine.app.tally.persistence.model.IndexModel
import com.coderpage.mine.app.tally.utils.DateUtils

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

    var mRepository: IndexDetailsRepository = IndexDetailsRepository()

    // 查询最近一周数据 涨幅情况
    fun getLatelyWeek(type: String, indexName: String) {
        mRepository.queryLatelyWeek(type, indexName, SimpleCallback { indexModels ->
            var tempValue: Double = 0.0

            for (indexModel in indexModels) {
                if (indexModel.indexIncreaseType == 0) {
                    tempValue += (indexModel.indexPercent.toDouble())
                } else {
                    tempValue -= (indexModel.indexPercent.toDouble())
                }
            }
            indexWeekObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询历史数据
    fun getHistoryData(type: String, indexName: String) {
        mRepository.getAllIndexData(type, indexName, SimpleCallback { indexModels ->
            historyObserver.value = indexModels
        })
    }

    // 查询最近一个月数据 涨幅情况
    fun getLatelyMonth(type: String, indexName: String) {
        mRepository.getLatelyMonthRepository(type, indexName, SimpleCallback { indexModels ->
            var tempValue: Double = 0.0

            for (indexModel in indexModels) {
                if (indexModel.indexIncreaseType == 0) {
                    tempValue += (indexModel.indexPercent.toDouble())
                } else {
                    tempValue -= (indexModel.indexPercent.toDouble())
                }
            }
            indexMouthObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询几个月前的数据 涨幅情况
    fun getLatelyUnKnowMonth(type: String, indexName: String) {
        mRepository.getUnKnowMonthRepository(type, indexName, SimpleCallback { indexModels ->
            var tempValue: Double = 0.0

            for (indexModel in indexModels) {
                if (indexModel.indexIncreaseType == 0) {
                    tempValue += (indexModel.indexPercent.toDouble())
                } else {
                    tempValue -= (indexModel.indexPercent.toDouble())
                }
            }
            indexThreeMouthObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询近半年的数据 涨幅情况
    fun getLatelyUnKnowYear(type: String, indexName: String) {
        mRepository.getUnKnowHalfRepository(type, indexName, SimpleCallback { indexModels ->
            var tempValue: Double = 0.0

            for (indexModel in indexModels) {
                if (indexModel.indexIncreaseType == 0) {
                    tempValue += (indexModel.indexPercent.toDouble())
                } else {
                    tempValue -= (indexModel.indexPercent.toDouble())
                }
            }
            indexHalfYearObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询最近一年的数据 涨幅情况
    fun getLatelyOneYear(type: String, indexName: String) {
        mRepository.getUnKnowYearRepository(type, indexName, SimpleCallback { indexModels ->
            var tempValue: Double = 0.0

            for (indexModel in indexModels) {
                if (indexModel.indexIncreaseType == 0) {
                    tempValue += (indexModel.indexPercent.toDouble())
                } else {
                    tempValue -= (indexModel.indexPercent.toDouble())
                }
            }
            indexYearObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询最近三年的数据 涨幅情况
    fun getLatelyThreeYear(type: String, indexName: String) {
        mRepository.getThreeYearRepository(type, indexName, SimpleCallback { indexModels ->
            var tempValue: Double = 0.0

            for (indexModel in indexModels) {
                if (indexModel.indexIncreaseType == 0) {
                    tempValue += (indexModel.indexPercent.toDouble())
                } else {
                    tempValue -= (indexModel.indexPercent.toDouble())
                }
            }
            indexThreeYearObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询最近五年的数据 涨幅情况
    fun getLatelyFiveYear(type: String, indexName: String) {
        mRepository.getFiveYearRepository(type, indexName, SimpleCallback { indexModels ->
            var tempValue: Double = 0.0

            for (indexModel in indexModels) {
                if (indexModel.indexIncreaseType == 0) {
                    tempValue += (indexModel.indexPercent.toDouble())
                } else {
                    tempValue -= (indexModel.indexPercent.toDouble())
                }
            }
            indexFiveYearObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询成立以来数据 涨幅情况
    fun getLatelyStartData(type: String, indexName: String) {
        mRepository.getAllIndexData(type, indexName, SimpleCallback { indexModels ->
            var tempValue: Double = 0.0

            for (indexModel in indexModels) {
                if (indexModel.indexIncreaseType == 0) {
                    tempValue += (indexModel.indexPercent.toDouble())
                } else {
                    tempValue -= (indexModel.indexPercent.toDouble())
                }
            }
            indexStartObserver.value = DateUtils.double2String(tempValue)
        })
    }

}
