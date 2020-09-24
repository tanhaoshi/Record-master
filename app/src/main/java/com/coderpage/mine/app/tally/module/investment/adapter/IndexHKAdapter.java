package com.coderpage.mine.app.tally.module.investment.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.coderpage.mine.MineApp;
import com.coderpage.mine.R;
import com.coderpage.mine.app.tally.module.investment.model.IndexHKViewModel;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;
import com.coderpage.mine.tally.module.index.IndexItemHKBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * create by ths on 2020/9/16
 */
public class IndexHKAdapter extends RecyclerView.Adapter<IndexHKAdapter.HKVH>{

    private LayoutInflater mInflater;
    private List<IndexModel> mIndexModels = new ArrayList<>();
    private Activity mActivity;
    private IndexHKViewModel mIndexHKViewModel;

    public IndexHKAdapter(Activity activity){
        this.mActivity = activity;
        mInflater = LayoutInflater.from(activity);
        mIndexHKViewModel = new IndexHKViewModel(MineApp.getAppContext());
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
    public HKVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HKVH(DataBindingUtil.inflate(mInflater,R.layout.adapter_hk_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HKVH holder, int position) {
        holder.bindTo(mIndexModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mIndexModels.size();
    }

    class HKVH extends RecyclerView.ViewHolder{

        IndexItemHKBinding mHKBinding;

        public HKVH(IndexItemHKBinding binding) {
            super(binding.getRoot());
            mHKBinding = binding;
        }

        public void bindTo(IndexModel indexModel){
            mHKBinding.setActivity(mActivity);
            mHKBinding.setData(indexModel);
            mHKBinding.setVM(mIndexHKViewModel);
            mHKBinding.executePendingBindings();

            if(Double.valueOf(indexModel.getIndexIncreaseType()) == 0){
                mHKBinding.increase.setText("+" + indexModel.getIndexRange());
                mHKBinding.increase.setTextColor(mActivity.getResources().getColor(R.color.indexRangeUp));

                mHKBinding.increasePercent.setText("+"+indexModel.getIndexPercent() + "%");
                mHKBinding.increasePercent.setTextColor(mActivity.getResources().getColor(R.color.indexRangeUp));

                mHKBinding.fundDesc.setTextColor(mActivity.getResources().getColor(R.color.indexRangeUp));
            }else{
                mHKBinding.increase.setText("-" + indexModel.getIndexRange());
                mHKBinding.increase.setTextColor(mActivity.getResources().getColor(R.color.categoryIncomeColor4));

                mHKBinding.increasePercent.setText("-"+indexModel.getIndexPercent() + "%");
                mHKBinding.increasePercent.setTextColor(mActivity.getResources().getColor(R.color.categoryIncomeColor4));

                mHKBinding.fundDesc.setTextColor(mActivity.getResources().getColor(R.color.categoryIncomeColor4));
            }
        }
    }
}
