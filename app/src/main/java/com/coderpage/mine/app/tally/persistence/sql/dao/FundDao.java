package com.coderpage.mine.app.tally.persistence.sql.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.persistence.sql.entity.FundEntity;

import java.util.List;

/**
 * create by ths on 2020/9/8
 */
@Dao
public interface FundDao {

    // 根据指定时间查询 一批数据
    //where fund.fund_type == :type order by create_time desc
    @Query("select * from fund where fund.fund_type == :type order by create_time desc")
    List<FundModel> getAppointFund(String type);

    // 目前是要进行插入
    @Insert
    long insert(FundEntity fundEntity);

    @Query("select * from fund group by fund_name order by create_time")
    List<FundModel> getAllFund();
}
