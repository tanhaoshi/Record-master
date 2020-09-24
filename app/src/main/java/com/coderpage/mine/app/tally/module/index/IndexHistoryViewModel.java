package com.coderpage.mine.app.tally.module.index;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.support.annotation.NonNull;

/**
 * create by ths on 2020/9/22
 */
public class IndexHistoryViewModel extends AndroidViewModel implements LifecycleObserver {
    public IndexHistoryViewModel(@NonNull Application application) {
        super(application);
    }
}
