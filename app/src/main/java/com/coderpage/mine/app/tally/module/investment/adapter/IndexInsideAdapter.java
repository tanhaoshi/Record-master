package com.coderpage.mine.app.tally.module.investment.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.coderpage.mine.MineApp;
import com.coderpage.mine.R;
import com.coderpage.mine.app.tally.module.investment.model.IndexAdapterViewModel;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;
import com.coderpage.mine.tally.module.index.IndexItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * create by ths on 2020/9/8
 */
public class IndexInsideAdapter extends RecyclerView.Adapter<IndexInsideAdapter.UsaVH>{

    private LayoutInflater  mInflater;
    private List<IndexModel> mIndexModels = new ArrayList<>();
    private IndexAdapterViewModel mAdapterViewModel;
    private Activity mActivity;

    public IndexInsideAdapter(Activity context){
        mActivity = context;
        mInflater = LayoutInflater.from(context);
        mAdapterViewModel = new IndexAdapterViewModel(MineApp.getAppContext());
    }

    public void setData(List<IndexModel> list){
        if(list == null){
            return;
        }
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return mIndexModels.size();
            }

            @Override
            public int getNewListSize() {
                return list.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                IndexModel oldIndex = mIndexModels.get(oldItemPosition);
                IndexModel newIndex = list.get(newItemPosition);

                if(oldIndex.getId() != newIndex.getId()){
                    return false;
                }

                if(oldIndex.getIndexName().equals(newIndex.getIndexName())){
                    return false;
                }

                return true;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return false;
            }
        });
        mIndexModels.clear();
        mIndexModels.addAll(list);
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public UsaVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsaVH(DataBindingUtil.inflate(mInflater, R.layout.adapter_fundusa_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UsaVH holder, int position) {
        holder.bindTo(mIndexModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mIndexModels.size();
    }

    class UsaVH extends RecyclerView.ViewHolder{

        IndexItemBinding mBinding;

        public UsaVH(IndexItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindTo(IndexModel indexModel){
            mBinding.setActivity(mActivity);
            mBinding.setData(indexModel);
            mBinding.setVM(mAdapterViewModel);
            mBinding.executePendingBindings();

            if(Double.valueOf(indexModel.getIndexIncreaseType()) == 0){
                mBinding.increase.setText("+" + indexModel.getIndexRange());
                mBinding.increase.setTextColor(mActivity.getResources().getColor(R.color.indexRangeUp));

                mBinding.increasePercent.setText("+"+indexModel.getIndexPercent() + "%");
                mBinding.increasePercent.setTextColor(mActivity.getResources().getColor(R.color.indexRangeUp));

                mBinding.fundDesc.setTextColor(mActivity.getResources().getColor(R.color.indexRangeUp));
            }else{
                mBinding.increase.setText("-" + indexModel.getIndexRange());
                mBinding.increase.setTextColor(mActivity.getResources().getColor(R.color.categoryIncomeColor4));

                mBinding.increasePercent.setText("-"+indexModel.getIndexPercent() + "%");
                mBinding.increasePercent.setTextColor(mActivity.getResources().getColor(R.color.categoryIncomeColor4));

                mBinding.fundDesc.setTextColor(mActivity.getResources().getColor(R.color.categoryIncomeColor4));
            }

        }
    }

}
