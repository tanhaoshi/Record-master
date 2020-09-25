package com.coderpage.mine.app.tally.module.index

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import com.coderpage.mine.R
import com.coderpage.mine.app.tally.persistence.model.IndexModel
import com.coderpage.mine.tally.module.index.IndexDetailsActivityBinding
import com.coderpage.mine.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_index_details.*

class IndexDetailsActivity : BaseActivity() {

    internal var mBinding   : IndexDetailsActivityBinding ?= null
    internal var mViewModel : IndexDetailsViewModel ?= null
    var indexHistoryAdapter : IndexHistoryAdapter ?= null

    private var mIndexModel: IndexModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_index_details)
        mViewModel = ViewModelProviders.of(this).get(IndexDetailsViewModel::class.java)

        getIntentData()

        setToolbarAsBack { finish() }

        mBinding?.vm = mViewModel

        initView()
    }

    private fun initView() {
        var linearLayoutManager    = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        var recyclerView           = mBinding!!.recyclerView
        recyclerView.layoutManager = linearLayoutManager

        indexHistoryAdapter  = IndexHistoryAdapter(this)
        recyclerView.adapter = indexHistoryAdapter

        mViewModel?.historyObserver?.observe(this, Observer {
            indexHistoryAdapter!!.setData(it!!)
        })

        mViewModel?.indexWeekObserver?.observe(this, Observer {
            mBinding?.indexWeek?.text = "$it%"
                    ""
        })

        mViewModel?.indexMouthObserver?.observe(this, Observer {
            mBinding?.indexMouth?.text = "$it%"
        })

        mViewModel?.indexThreeMouthObserver?.observe(this, Observer {
            mBinding?.indexThreeMouth?.text = "$it%"
        })

        mViewModel?.indexHalfYearObserver?.observe(this, Observer {
            mBinding?.indexHalfYear?.text = "$it%"
        })

        mViewModel?.indexYearObserver?.observe(this, Observer {
            mBinding?.indexYear?.text = "$it%"
        })

        mViewModel?.indexThreeYearObserver?.observe(this, Observer {
            mBinding?.indexThreeYear?.text = "$it%"
        })

        mViewModel?.indexFiveYearObserver?.observe(this, Observer {
            mBinding?.indexFiveYear?.text = "$it%"
        })

        mViewModel?.indexStartObserver?.observe(this, Observer {
            mBinding?.indexStart?.text = "$it%"
        })
    }

    private fun getIntentData() {
        val intent = intent
        val bundle = intent.getBundleExtra("bundle")
        if (bundle != null) {
            mIndexModel = bundle.getSerializable("indexModel") as IndexModel
        }

        if (mIndexModel != null) {
            setToolbarTitle(mIndexModel!!.indexName)
        }
    }

    override fun onStart() {
        super.onStart()
        mViewModel?.getLatelyWeek(mIndexModel!!.indexType, mIndexModel!!.indexName)
        mViewModel?.getHistoryData(mIndexModel!!.indexType, mIndexModel!!.indexName)
        mViewModel?.getLatelyMonth(mIndexModel!!.indexType,mIndexModel!!.indexName)
        mViewModel?.getLatelyUnKnowMonth(mIndexModel!!.indexType,mIndexModel!!.indexName)
        mViewModel?.getLatelyUnKnowYear(mIndexModel!!.indexType,mIndexModel!!.indexName)
        mViewModel?.getLatelyOneYear(mIndexModel!!.indexType,mIndexModel!!.indexName)
        mViewModel?.getLatelyThreeYear(mIndexModel!!.indexType,mIndexModel!!.indexName)
        mViewModel?.getLatelyFiveYear(mIndexModel!!.indexType,mIndexModel!!.indexName)
        mViewModel?.getLatelyStartData(mIndexModel!!.indexType,mIndexModel!!.indexName)
    }
}

