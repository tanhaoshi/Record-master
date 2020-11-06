package com.coderpage.mine.app.tally.module.index

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.blankj.utilcode.util.TimeUtils
import com.coderpage.mine.MineApp
import com.coderpage.mine.R
import com.coderpage.mine.app.tally.persistence.model.IndexModel
import com.coderpage.mine.utils.AndroidUtils

/**
 * create by ths on 2020/9/22
 */
class IndexHistoryAdapter(): RecyclerView.Adapter<IndexHistoryAdapter.HistoryVH>() {

      var mActivity             : Activity ?= null
      var layoutInflater        : LayoutInflater ?= null
      var indexHistoryViewModel : IndexHistoryViewModel ?= null
      var list                  = ArrayList<IndexModel>()

      constructor(activity: Activity) : this() {
         this.mActivity = activity
         layoutInflater = LayoutInflater.from(mActivity)
         indexHistoryViewModel = IndexHistoryViewModel(MineApp.getAppContext())
      }

      fun setData(newList: List<IndexModel>){
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

               if(oldIndex.indexName == newIndex.indexName){
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

      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryVH {
          return HistoryVH(DataBindingUtil.inflate(layoutInflater!!, R.layout.adapter_index_history,parent,false))
      }

      override fun getItemCount(): Int {
          return list.size
      }

      override fun onBindViewHolder(holder: HistoryVH, position: Int) {
          holder.bindTo(list[position])
      }

       inner class HistoryVH(var mBinding : AdapterIndexHistoryBinding) : RecyclerView.ViewHolder(mBinding.root){

           fun bindTo(indexModel: IndexModel){
              mBinding.data = indexModel
              mBinding.vm   = indexHistoryViewModel
              mBinding.activity = mActivity
              mBinding.executePendingBindings()

              mBinding.tvCategoryName.text = indexModel.indexName
              mBinding.tvRecordDec.text = TimeUtils.millis2String(indexModel.time,"yyyy-MM-dd") +"  "+AndroidUtils.getDayOfWeek(TimeUtils.millis2String(indexModel.time,"yyyy-MM-dd"))

               mBinding.rootLayout.setOnLongClickListener {
                   indexHistoryViewModel?.setOnLongClick(mActivity,indexModel)
                   true
               }

              if(indexModel.indexIncreaseType == 0){
                  mBinding.etAmount.setTextColor(mActivity!!.resources.getColor(R.color.indexRangeUp))
                  mBinding.tvTime.setTextColor(mActivity!!.resources.getColor(R.color.indexRangeUp))

                  mBinding.etAmount.text = "+"+indexModel.indexPercent +"%"
                  mBinding.tvTime.text = "+"+indexModel.indexRange
              }else{
                  mBinding.etAmount.setTextColor(mActivity!!.resources.getColor(R.color.categoryIncomeColor4))
                  mBinding.tvTime.setTextColor(mActivity!!.resources.getColor(R.color.categoryIncomeColor4))

                  mBinding.etAmount.text =  "-"+indexModel.indexPercent + "%"
                  mBinding.tvTime.text =  "-"+indexModel.indexRange
              }
          }
      }
}
