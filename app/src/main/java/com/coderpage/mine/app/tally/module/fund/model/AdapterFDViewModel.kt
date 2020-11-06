package com.coderpage.mine.app.tally.module.fund.model

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleObserver
import android.content.DialogInterface
import android.widget.Toast
import com.blankj.utilcode.util.ToastUtils
import com.coderpage.base.common.SimpleCallback
import com.coderpage.mine.app.tally.module.fund.repository.FundDetailsADRepository
import com.coderpage.mine.app.tally.persistence.model.FundModel
import com.coderpage.mine.app.tally.ui.dialog.PermissionReqDialog

/**
 * create by ths on 2020/9/25
 */
class AdapterFDViewModel(application: Application) : AndroidViewModel(application),LifecycleObserver{

    var fundDetailsADRepository : FundDetailsADRepository = FundDetailsADRepository()

    fun setOnLongClick(activity: Activity,fundModel: FundModel){
        PermissionReqDialog(activity, "是否确定删除该基金", "请确认")
                .setTitleText("温馨提示!")
                .setPositiveText("确认")
                .setListener(object : PermissionReqDialog.Listener {
                    override fun onCancelClick(dialog: DialogInterface) {
                        dialog.dismiss()
                    }

                    override fun onConfirmClick(dialog: DialogInterface) {
                        deleteFund(activity,fundModel)
                        dialog.dismiss()
                    }
                })
                .show()
    }

    fun deleteFund(activity: Activity,fundModel: FundModel){
        fundDetailsADRepository.deleteFundDetails(fundModel, SimpleCallback {
            ToastUtils.showShort("删除成功!")
            activity.finish()
        })
    }
}
