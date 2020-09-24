package com.coderpage.mine.app.tally.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.coderpage.base.utils.UIUtils;
import com.coderpage.concurrency.MineExecutors;
import com.coderpage.mine.R;
import com.coderpage.mine.app.tally.databinding.CommonBindAdapter;
import com.coderpage.mine.app.tally.utils.KeyboardUtils;
import com.coderpage.mine.common.Font;
import com.coderpage.mine.dialog.FundEditIndexDialogBinding;

/**
 * create by ths on 2020/9/10
 */
public class FundEditIndexDialog extends Dialog {

    private FundEditIndexDialog.Listener mListener;
    private FundEditIndexDialogBinding mBinding;
    private Activity mActivity;

    public FundEditIndexDialog(Activity activity) {
        super(activity, R.style.Widget_Dialog_BottomSheet);
        initView(activity);
        this.mActivity = activity;
    }

    private void initView(Activity activity) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.dialog_edit_index, null, false);
        CommonBindAdapter.setTypeFace(mBinding.tvBudgetUnit, Font.QUICKSAND_REGULAR);
        CommonBindAdapter.setTypeFace(mBinding.etBudget, Font.QUICKSAND_MEDIUM);

        mBinding.tvBudgetUnit.setText("基金日涨幅:");
        mBinding.tvFundCode.setText("日涨跌类型:");

//         输入框获取焦点时，弹出软键盘
        mBinding.etBudget.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && getWindow() != null) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });

        mBinding.ivClose.setOnClickListener(v -> dismiss());

        mBinding.tvConfirm.setOnClickListener(v -> {
            String budgetStr = mBinding.etBudget.getText().toString();
            String increaseRange = mBinding.etFundCode.getText().toString();
            // 输入预算 <= 0 提示错误信息
            if (budgetStr.length() <= 0) {
                Toast.makeText(activity, "涨幅不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(increaseRange.length() < 1){
                Toast.makeText(activity, "涨幅类型不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mListener != null) {
                mListener.onBudgetUpdate(this, budgetStr,increaseRange);
            }
        });

        View contentView = mBinding.getRoot();
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        setContentView(contentView);
        initWindow(contentView.getMeasuredHeight());
    }

    private void initWindow(int height) {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = window.getWindowManager().getDefaultDisplay().getWidth();
        attributes.height = height;
        window.setAttributes(attributes);
    }

    @Override
    public void show() {
        super.show();
        mBinding.etBudget.setFocusable(true);
        mBinding.etBudget.requestFocus();
    }

    @Override
    public void dismiss() {
        View view = getCurrentFocus();
        if(view instanceof TextView){
            InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
        super.dismiss();
    }

    public FundEditIndexDialog setListener(Listener listener) {
        mListener = listener;
        return this;
    }

    public interface Listener {
        /**
         * @param dialog dialog
         */
        void onBudgetUpdate(DialogInterface dialog, String percent,String rangeType);
    }
}
