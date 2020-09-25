package com.coderpage.mine.app.tally.module.fund.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import com.coderpage.mine.app.tally.module.fund.repository.FundDetailsRepository
import com.coderpage.mine.app.tally.persistence.model.FundModel
import com.coderpage.mine.app.tally.persistence.model.IndexModel

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

    var fundhistoryObserver = MutableLiveData<List<FundModel>>()

    var mRepository : FundDetailsRepository = FundDetailsRepository()
}
