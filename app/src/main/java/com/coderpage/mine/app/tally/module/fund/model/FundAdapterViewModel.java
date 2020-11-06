package com.coderpage.mine.app.tally.module.fund.model;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.coderpage.mine.app.tally.module.fund.FundDetailsActivity;
import com.coderpage.mine.app.tally.module.fund.repository.FundRepository;
import com.coderpage.mine.app.tally.persistence.model.FundModel;

/**
 * create by ths on 2020/9/10
 */
public class FundAdapterViewModel extends AndroidViewModel implements LifecycleObserver {

    private FundRepository mRepository;

    public FundAdapterViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FundRepository();
    }

    public void onItemClick(View view, Activity activity, FundModel fundModel){
        Intent intent = new Intent(activity, FundDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("fundModel",fundModel);
        intent.putExtra("bundle",bundle);
        activity.startActivity(intent);
    }
}
