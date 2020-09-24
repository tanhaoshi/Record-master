package com.coderpage.mine.app.tally.module.fund.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.coderpage.mine.MineApp;
import com.coderpage.mine.R;
import com.coderpage.mine.app.tally.module.fund.model.FundAdapterViewModel;
import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.tally.module.fund.AddFundItemBinding;
import com.coderpage.mine.tally.module.fund.AddFundItemBindingImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * create by ths on 2020/9/10
 */
public class AddFundAdapter extends RecyclerView.Adapter<AddFundAdapter.VHFund>{

    private LayoutInflater  mInflater;
    private Activity        mActivity;
    private FundAdapterViewModel mAdapterViewModel;
    private List<FundModel> mFundModels = new ArrayList<>();

    public AddFundAdapter(FragmentActivity activity){
        this.mActivity         = activity;
        this.mInflater         = LayoutInflater.from(activity);
        this.mAdapterViewModel = new FundAdapterViewModel(MineApp.getAppContext());
    }

    public void setData(List<FundModel> models){
        if(models == null){
            return;
        }
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return mFundModels.size();
            }

            @Override
            public int getNewListSize() {
                return models.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                FundModel oldModel = mFundModels.get(oldItemPosition);
                FundModel newModel = models.get(newItemPosition);

                if(oldModel.getId() != newModel.getId()){
                    return false;
                }

                if(oldModel.getFundName() != newModel.getFundName()){
                    return false;
                }

                return true;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return false;
            }
        });

        mFundModels.clear();
        mFundModels.addAll(models);
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public VHFund onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VHFund(DataBindingUtil.inflate(mInflater, R.layout.adapter_fund_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VHFund holder, int position) {
        holder.bindTo(mFundModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mFundModels.size();
    }

    class VHFund extends RecyclerView.ViewHolder{

        private AddFundItemBinding mBinding;

        public VHFund(AddFundItemBindingImpl itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        void bindTo(FundModel fundModel){
            mBinding.setActivity(mActivity);
            mBinding.setVm(mAdapterViewModel);
            mBinding.setData(fundModel);
            mBinding.executePendingBindings();
        }
    }
}
