package com.coderpage.mine.app.tally.module.investment.adapter

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.coderpage.mine.MineApp
import com.coderpage.mine.R
import com.coderpage.mine.app.tally.module.investment.model.IndexFundViewModel
import com.coderpage.mine.app.tally.persistence.model.FundModel
import com.coderpage.mine.tally.module.fund.AdapterIndexOfBinding

import java.util.ArrayList

/**
 * create by ths on 2020/9/16
 */
class IndexOfFundAdapter(private val mActivity: Activity, private val mFundViewModel: IndexFundViewModel) :
        RecyclerView.Adapter<IndexOfFundAdapter.FundVH>() {

    private val mInflater: LayoutInflater
    private val mFundModels = ArrayList<FundModel>()

    init {
        mInflater = LayoutInflater.from(mActivity)
    }

    fun setData(list: List<FundModel>?) {
        if (list == null) {
            return
        }

        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return mFundModels.size
            }

            override fun getNewListSize(): Int {
                return list.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val oldModel = mFundModels[oldItemPosition]
                val newModel = list[newItemPosition]

                if (oldModel.id != newModel.id) {
                    return false
                }

                return if (oldModel.fundName == newModel.fundName) {
                    false
                } else true

            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return false
            }
        })
        mFundModels.clear()
        mFundModels.addAll(list)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FundVH {
        return FundVH(DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.adapter_index_of_fund, parent, false))
    }

    override fun onBindViewHolder(holder: FundVH, position: Int) {
        holder.bindTo(mFundModels[position])
    }

    override fun getItemCount(): Int {
        return mFundModels.size
    }

    public inner class FundVH(var mBinding: AdapterIndexOfBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun bindTo(fundModel: FundModel) {
            mBinding.activity = mActivity
            mBinding.data = fundModel
            mBinding.vm = mFundViewModel
            mBinding.executePendingBindings()

            if (fundModel.fundIncreaseType == 0) {
                mBinding.etAmount.text = "+" + fundModel.fundPercent + "%"
                mBinding.etAmount.setTextColor(mActivity.resources.getColor(R.color.indexRangeUp))

            } else {
                mBinding.etAmount.text = "-" + fundModel.fundPercent + "%"
                mBinding.etAmount.setTextColor(mActivity.resources.getColor(R.color.categoryIncomeColor4))
            }
        }
    }
}
