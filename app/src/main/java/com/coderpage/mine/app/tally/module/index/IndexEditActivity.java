package com.coderpage.mine.app.tally.module.index;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.coderpage.mine.R;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;
import com.coderpage.mine.app.tally.utils.KeyboardUtils;
import com.coderpage.mine.ui.BaseActivity;

import static android.widget.Toast.LENGTH_SHORT;

public class IndexEditActivity extends BaseActivity {

    private IndexEditViewModel       mEditViewModel;
    private IndexEditActivityBinding mBinding;

    private IndexModel mIndexModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding       = DataBindingUtil.setContentView(this,R.layout.activity_index_editctivity);
        mEditViewModel = ViewModelProviders.of(this).get(IndexEditViewModel.class);
        getLifecycle().addObserver(mEditViewModel);

        setToolbarAsBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(KeyboardUtils.isSoftInputVisible(IndexEditActivity.this)){
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    }
                }
                finish();
            }
        });

        setToolbarTitle(getResources().getString(R.string.menu_tally_investment));

        getIntentData();
        initView();
        subscribeUi();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if(bundle != null){
            mIndexModel = (IndexModel) bundle.getSerializable("indexModel");
        }

        if(mIndexModel != null){
            mBinding.indexName.setText(mIndexModel.getIndexName());
            mBinding.indexArea.setText(mIndexModel.getIndexType());
        }
    }

    private void initView() {
        mBinding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit(){
        if(mBinding.indexName.getText().toString().length() <= 2){
            Toast.makeText(this,"指数名称不能小于2", LENGTH_SHORT).show();
            return;
        }

        if(mBinding.indexArea.getText().toString().length() == 0){
            Toast.makeText(this,"指数类型必须填", LENGTH_SHORT).show();
            return;
        }

        if(mBinding.currentIndex.getText().toString().length() == 0){
            Toast.makeText(this,"指数必须填写", LENGTH_SHORT).show();
            return;
        }

        if(mBinding.currentRange.getText().toString().length() == 0){
            Toast.makeText(this,"日涨幅必须填写", LENGTH_SHORT).show();
            return;
        }

        if(mBinding.currentPercent.getText().toString().length() == 0){
            Toast.makeText(this,"日涨幅百分比必须填写", LENGTH_SHORT).show();
            return;
        }

        if(mBinding.currentType.getText().toString().length() < 1){
            Toast.makeText(this,"日涨跌类型必须填写", LENGTH_SHORT).show();
            return;
        }

        IndexModel indexModel = new IndexModel();
        indexModel.setFundSyncId(System.currentTimeMillis());
        indexModel.setIndexName(mBinding.indexName.getText().toString());
        indexModel.setIndexType(mBinding.indexArea.getText().toString());
        indexModel.setIndexNumber(mBinding.currentIndex.getText().toString());
        indexModel.setIndexRange(mBinding.currentRange.getText().toString());
        indexModel.setIndexPercent(mBinding.currentPercent.getText().toString());
        indexModel.setIndexIncreaseType(Integer.valueOf(mBinding.currentType.getText().toString()));

        mEditViewModel.saveIndexData(indexModel);
    }

    private void subscribeUi() {
        mBinding.setData(mEditViewModel);

        mEditViewModel.mLiveData.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(IndexEditActivity.this,"添加成功!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mIndexModel != null){
            getMenuInflater().inflate(R.menu.menu_index_details,menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.details:
                Intent intent = new Intent(IndexEditActivity.this,IndexDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("indexModel",mIndexModel);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
