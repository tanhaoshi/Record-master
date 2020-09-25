package com.coderpage.mine.app.tally.persistence.sql.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.coderpage.mine.app.tally.persistence.model.IndexModel;
import com.coderpage.mine.app.tally.persistence.sql.entity.IndexEntity;

import java.util.List;

/**
 * create by ths on 2020/9/11
 */
@Dao
public interface IndexDao {

    @Insert
    long insert(IndexEntity indexEntity);

    @Query("select * from indexs where index_type == :type group by index_name order by create_time asc ")
    List<IndexModel> getInsideIndex(String type);

    // <= datetime('now','start of day','+0 day','weekday 1')

    // >= datetime('now','start of day','-6day')
    // 查询近一周数据
    @Query("select * from indexs where index_type == :type and index_name == :indexName and " +
            "create_time <= :endTime and create_time >= :startTime")
    List<IndexModel> getLatelyWeekIndex(String type, String indexName,long startTime,long endTime);

    // 查询本周数据
    @Query("select * from indexs where index_type == :type and index_name == :indexName and "
    + "create_time >=datetime('now','start of day','-6day','weekday 1') and create_time <=datetime('now','start of day','+0 day','weekday 1')")
    List<IndexModel> getCurrentWeekIndex(String type,String indexName);

    // 查询所有的记录
    @Query("select * from indexs where index_type == :type and index_name == :indexName order by create_time desc")
    List<IndexModel> queryAllIndex(String type,String indexName);

    // 查询近一个月的记录
    // select * from indexs where create_time between datetime('now','-1 month') and datetime('now')
    // select * from indexs where create_time between date('now', "-1 month") and date('now')
    // between datetime('now','start of month','+1 second') and datetime('now','start of month','+1 month','-1 second')
    @Query("select * from indexs where create_time <= :endTime and create_time >= :startTime and index_type == :type and index_name == :indexName")
    List<IndexModel> queryLatelyMonth(String type,String indexName,long startTime,long endTime);

    // 查询最近三个月的记录
    @Query("select * from indexs where create_time between datetime('now','-3 month') and datetime('now') and index_type == :type and index_name == :indexName")
    List<IndexModel> queryThreeMonth(String type,String indexName);

    // 查询最近半年的记录
    @Query("select * from indexs where create_time between datetime('now','-6 month') and datetime('now') and index_type == :type and index_name == :indexName")
    List<IndexModel> queryLatelyHalfIndex(String type,String indexName);

    // eg : 查询最近一年的记录  startTime = 2020-1-20 , endTime = 2020-12-30 (将查询指定的区域 在这块区域中 查询)
    @Query("select * from indexs where datetime(create_time) >= datetime(:startTime) and datetime(create_time) <= datetime(:endTime) " +
            "and index_type == :type and index_name == :indexName")
    List<IndexModel> queryLayelyYear(String type,String indexName,String startTime,String endTime);

}
