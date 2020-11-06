package com.coderpage.mine.app.tally.module.fund.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import com.coderpage.base.common.SimpleCallback
import com.coderpage.mine.app.tally.module.fund.repository.FundDetailsRepository
import com.coderpage.mine.app.tally.persistence.model.FundModel
import com.coderpage.mine.app.tally.utils.DateUtils

/**
 * create by ths on 2020/9/25
 */
class FundDetailsViewModel(application:Application) : AndroidViewModel(application),LifecycleObserver{

    var fundWeekObserver = MutableLiveData<String>()

    var fundMouthObserver = MutableLiveData<String>()

    var fundThreeMouthObserver = MutableLiveData<String>()

    var fundHalfYearObserver = MutableLiveData<String>()

    var fundYearObserver = MutableLiveData<String>()

    var fundThreeYearObserver = MutableLiveData<String>()

    var fundFiveYearObserver = MutableLiveData<String>()

    var fundStartObserver = MutableLiveData<String>()

    var fundHistoryObserver = MutableLiveData<List<FundModel>>()

    var mRepository : FundDetailsRepository = FundDetailsRepository()

    // 查询最近一周数据 涨幅情况
    fun getLatelyWeek(type: String, indexName: String) {
        mRepository.queryLatelyWeek(type, indexName, SimpleCallback { fundModels ->
            var tempValue: Double = 0.0

            for (fundModel in fundModels) {
                tempValue += (fundModel.fundPercent.toDouble())
            }
            fundWeekObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询历史数据
    fun getAllFundData(type: String, indexName: String) {
        mRepository.getAllFundData(type, indexName, SimpleCallback { fundModels ->
            fundHistoryObserver.value = fundModels
        })
    }

    // 查询最近一个月数据 涨幅情况
    fun getLatelyMonth(type: String, indexName: String) {
        mRepository.getLatelyMonthRepository(type, indexName, SimpleCallback { fundModels ->
            var tempValue: Double = 0.0

            for (fundModel in fundModels) {
                tempValue += (fundModel.fundPercent.toDouble())
            }

            fundMouthObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询几个月前的数据 涨幅情况
    fun getLatelyUnKnowMonth(type: String, indexName: String) {
        mRepository.getUnKnowMonthRepository(type, indexName, SimpleCallback { fundModels ->
            var tempValue: Double = 0.0

            for (fundModel in fundModels) {
                tempValue += (fundModel.fundPercent.toDouble())
            }

            fundThreeMouthObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询近半年的数据 涨幅情况
    fun getLatelyUnKnowYear(type: String, indexName: String) {
        mRepository.getUnKnowHalfRepository(type, indexName, SimpleCallback { fundModels ->
            var tempValue: Double = 0.0

            for (fundModel in fundModels) {
                tempValue += (fundModel.fundPercent.toDouble())
            }

            fundHalfYearObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询最近一年的数据 涨幅情况
    fun getLatelyOneYear(type: String, indexName: String) {
        mRepository.getUnKnowYearRepository(type, indexName, SimpleCallback { fundModels ->
            var tempValue: Double = 0.0

            for (fundModel in fundModels) {
                tempValue += (fundModel.fundPercent.toDouble())
            }

            fundYearObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询最近三年的数据 涨幅情况
    fun getLatelyThreeYear(type: String, indexName: String) {
        mRepository.getThreeYearRepository(type, indexName, SimpleCallback { fundModels ->
            var tempValue: Double = 0.0

            for (fundModel in fundModels) {
                tempValue += (fundModel.fundPercent.toDouble())
            }

            fundThreeYearObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询最近五年的数据 涨幅情况
    fun getLatelyFiveYear(type: String, indexName: String) {
        mRepository.getFiveYearRepository(type, indexName, SimpleCallback { fundModels ->
            var tempValue: Double = 0.0

            for (fundModel in fundModels) {
                tempValue += (fundModel.fundPercent.toDouble())
            }

            fundFiveYearObserver.value = DateUtils.double2String(tempValue)
        })
    }

    // 查询成立以来数据 涨幅情况
    fun getLatelyStartData(type: String, indexName: String) {
        mRepository.getAllFundData(type, indexName, SimpleCallback { fundModels ->
            var tempValue: Double = 0.0

            for (fundModel in fundModels) {
                tempValue += (fundModel.fundPercent.toDouble())
            }

            fundStartObserver.value = DateUtils.double2String(tempValue)
        })
    }
}
