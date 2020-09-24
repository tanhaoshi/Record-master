package com.coderpage.mine.app.tally.module.investment.model;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.coderpage.mine.app.tally.module.index.IndexEditActivity;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;

/**
 * create by ths on 2020/9/16
 */
public class IndexHKViewModel extends AndroidViewModel implements LifecycleObserver {

    public IndexHKViewModel(@NonNull Application application) {
        super(application);
    }

    public void onItemClick(IndexModel indexModel, Activity activity){
        Intent intent = new Intent(activity, IndexEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("indexModel",indexModel);
        intent.putExtra("bundle",bundle);
        activity.startActivity(intent);
    }
}
