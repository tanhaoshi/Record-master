package com.coderpage.mine.app.tally.module.fund.adapter

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.blankj.utilcode.util.TimeUtils
import com.coderpage.mine.MineApp
import com.coderpage.mine.R
import com.coderpage.mine.app.tally.module.fund.AdapterFundDetailsBinding
import com.coderpage.mine.app.tally.module.fund.model.AdapterFDViewModel
import com.coderpage.mine.app.tally.persistence.model.FundModel

/**
 * create by ths on 2020/9/25
 */
class FundDetailsAdapter() : RecyclerView.Adapter<FundDetailsAdapter.FundDetailsViewHolder>() {

    public fun setData(newList : List<FundModel>){
        if(newList == null){
            return
        }

        var result : DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback(){
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                var oldIndex = list[oldItemPosition]
                var newIndex = newList[newItemPosition]

                if(oldIndex.id != newIndex.id){
                    return false
                }

                if(oldIndex.fundName == newIndex.fundName){
                    return false
                }

                return true
            }

            override fun getOldListSize(): Int {
                return list.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return false
            }
        })

        list.clear()
        list.addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FundDetailsViewHolder {
        return FundDetailsViewHolder(DataBindingUtil.inflate(layoutInflater!!, R.layout.adapter_fund_details,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FundDetailsViewHolder, position: Int) {
        holder.binTo(list[position])
    }

    var mActivity : Activity ?= null
    var layoutInflater : LayoutInflater ?= null
    var adapterFDViewModel : AdapterFDViewModel ?= null
    var list = ArrayList<FundModel>()

    constructor(activity:Activity) : this(){
        this.mActivity = activity
        layoutInflater = LayoutInflater.from(mActivity)
        adapterFDViewModel = AdapterFDViewModel(MineApp.getAppContext())
    }

    inner class FundDetailsViewHolder(var mBinding:AdapterFundDetailsBinding) : RecyclerView.ViewHolder(mBinding.root){

         fun binTo(fundModel: FundModel){
            mBinding.data = fundModel
            mBinding.vm   = adapterFDViewModel
            mBinding.activity = mActivity
            mBinding.executePendingBindings()

            mBinding.tvCategoryName.text = fundModel.fundName

            mBinding.dateTime.text = TimeUtils.millis2String(fundModel.time,"yyyy-MM-dd")

            if(fundModel.fundIncreaseType == 0){
                mBinding.etAmount.setTextColor(mActivity!!.resources.getColor(R.color.indexRangeUp))
                mBinding.etAmount.text = "+"+fundModel.fundPercent +"%"
            }else{
                mBinding.etAmount.setTextColor(mActivity!!.resources.getColor(R.color.categoryIncomeColor4))
                mBinding.etAmount.text = fundModel.fundPercent +"%"
            }
        }
    }
}
