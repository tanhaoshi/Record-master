package com.coderpage.mine.app.tally.module.index;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleObserver;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.coderpage.base.common.IError;
import com.coderpage.base.common.Result;
import com.coderpage.base.common.SimpleCallback;
import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;
import com.coderpage.mine.app.tally.ui.dialog.PermissionReqDialog;

/**
 * create by ths on 2020/9/22
 */
public class IndexHistoryViewModel extends AndroidViewModel implements LifecycleObserver {

    private IndexHistoryRepository mRepository;

    public IndexHistoryViewModel(@NonNull Application application) {
        super(application);
        mRepository = new IndexHistoryRepository();
    }

    public void setOnLongClick(Activity activity, IndexModel indexModel){
        new PermissionReqDialog(activity,"是否确定删除该指数数据","请确认")
                .setTitleText("温馨提示!")
                .setPositiveText("确认")
                .setListener(new PermissionReqDialog.Listener() {
                    @Override
                    public void onCancelClick(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onConfirmClick(DialogInterface dialog) {
                        deleteIndexViewModel(indexModel,activity);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void deleteIndexViewModel(IndexModel indexModel,Activity activity){
        mRepository.deleteRepository(indexModel, new SimpleCallback<Result<Long, IError>>() {
            @Override
            public void success(Result<Long, IError> longIErrorResult) {
                ToastUtils.showShort("删除成功!");
                activity.finish();
            }
        });
    }
}
