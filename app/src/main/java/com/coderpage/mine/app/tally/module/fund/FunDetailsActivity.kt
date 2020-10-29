package com.coderpage.mine.app.tally.module.fund

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import com.coderpage.mine.R
import com.coderpage.mine.app.tally.module.fund.adapter.FundDetailsAdapter
import com.coderpage.mine.app.tally.module.fund.model.FundDetailsViewModel
import com.coderpage.mine.app.tally.module.index.IndexDetailsViewModel
import com.coderpage.mine.app.tally.persistence.model.FundModel
import com.coderpage.mine.tally.module.fund.FundDetailsActivityBinding
import com.coderpage.mine.ui.BaseActivity

class FunDetailsActivity : BaseActivity() {

    internal var mBinding: FundDetailsActivityBinding ?= null
    internal var mViewModel :FundDetailsViewModel ?= null
    var fundDetailsAdapter : FundDetailsAdapter ?= null
    private var mFundModel : FundModel ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding   = DataBindingUtil.setContentView(this,R.layout.activity_fun_details)
        mViewModel = ViewModelProviders.of(this).get(FundDetailsViewModel::class.java)

        getIntentData()

        setToolbarAsBack{ finish() }

        mBinding?.vm = mViewModel

        initView()
    }

    private fun getIntentData() {
        var intent = intent
        val bundle = intent.getBundleExtra("bundle")
        if(bundle != null){
            mFundModel = bundle.getSerializable("fundModel") as FundModel
        }
        
        if(mFundModel != null){
            setToolbarTitle(mFundModel!!.fundName)
        }
    }

    private fun initView() {
        var linearLayoutManager    = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        var recyclerView           = mBinding!!.recyclerView
        recyclerView.layoutManager = linearLayoutManager

        fundDetailsAdapter = FundDetailsAdapter(this)
        recyclerView.adapter = fundDetailsAdapter

        mViewModel?.fundHistoryObserver?.observe(this, Observer {
            fundDetailsAdapter!!.setData(it!!)
        })

        mViewModel?.fundWeekObserver?.observe(this, Observer {
            mBinding?.indexWeek?.text = "$it%"
        })

        mViewModel?.fundMouthObserver?.observe(this, Observer {
            mBinding?.indexMouth?.text = "$it%"
        })

        mViewModel?.fundThreeMouthObserver?.observe(this, Observer {
            mBinding?.indexThreeMouth?.text = "$it%"
        })

        mViewModel?.fundHalfYearObserver?.observe(this, Observer {
            mBinding?.indexHalfYear?.text = "$it%"
        })

        mViewModel?.fundYearObserver?.observe(this, Observer {
            mBinding?.indexYear?.text = "$it%"
        })

        mViewModel?.fundThreeYearObserver?.observe(this, Observer {
            mBinding?.indexThreeYear?.text = "$it%"
        })

        mViewModel?.fundFiveYearObserver?.observe(this, Observer {
            mBinding?.indexFiveYear?.text = "$it%"
        })

        mViewModel?.fundStartObserver?.observe(this, Observer {
            mBinding?.indexStart?.text = "$it%"
        })
    }

    override fun onStart() {
        super.onStart()
        mViewModel?.getAllFundData(mFundModel!!.fundType,mFundModel!!.fundName)
        mViewModel?.getLatelyWeek(mFundModel!!.fundType,mFundModel!!.fundName)
        mViewModel?.getLatelyMonth(mFundModel!!.fundType,mFundModel!!.fundName)
        mViewModel?.getLatelyUnKnowMonth(mFundModel!!.fundType,mFundModel!!.fundName)
        mViewModel?.getLatelyUnKnowYear(mFundModel!!.fundType,mFundModel!!.fundName)
        mViewModel?.getLatelyOneYear(mFundModel!!.fundType,mFundModel!!.fundName)
        mViewModel?.getLatelyThreeYear(mFundModel!!.fundType,mFundModel!!.fundName)
        mViewModel?.getLatelyFiveYear(mFundModel!!.fundType,mFundModel!!.fundName)
        mViewModel?.getLatelyStartData(mFundModel!!.fundType,mFundModel!!.fundName)
    }
}
