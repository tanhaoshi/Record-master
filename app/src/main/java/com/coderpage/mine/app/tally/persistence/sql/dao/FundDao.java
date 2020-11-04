package com.coderpage.mine.app.tally.persistence.sql.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.coderpage.mine.app.tally.persistence.model.FundModel;
import com.coderpage.mine.app.tally.persistence.model.IndexModel;
import com.coderpage.mine.app.tally.persistence.sql.entity.FundEntity;
import com.coderpage.mine.app.tally.persistence.sql.entity.IndexEntity;

import java.util.List;

/**
 * create by ths on 2020/9/8
 */
@Dao
public interface FundDao {


    // 查询所有的记录
    @Query("select * from fund where fund_type == :type and fund_name == :fundName order by create_time desc")
    List<FundModel> queryAllFund(String type, String fundName);

    // 根据 指定时间 选择查询的日期内数据
    @Query("select * from fund where create_time <= :endTime and create_time >= :startTime and fund_type == :type and fund_name == :fundName")
    List<FundModel> queryLatelyData(String type,String fundName,long startTime,long endTime);

    @Insert
    long insert(FundEntity fundEntity);

    @Insert
    long insert(IndexEntity indexEntity);

    @Query("select * from fund group by fund_name order by create_time desc")
    List<FundModel> getAllFund();

    @Query("DELETE FROM FUND")
    void deleteAll();
}
