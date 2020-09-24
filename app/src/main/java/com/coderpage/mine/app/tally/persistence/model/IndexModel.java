package com.coderpage.mine.app.tally.persistence.model;

import android.arch.persistence.room.ColumnInfo;

import com.coderpage.mine.app.tally.persistence.sql.entity.IndexEntity;

import java.io.Serializable;

/**
 * create by ths on 2020/9/11
 */
public class IndexModel implements Serializable {

    @ColumnInfo(name = "index_id")
    private long id;

    @ColumnInfo(name = "index_sync_id")
    private long fundSyncId;

    /** 记录时间（UNIX TIME） */
    @ColumnInfo(name = "create_time")
    private long time;

    // id, 同步id , 创建时间, 指数名称, 指数 , 幅度 , 百分比 , 类型 , 类型名
    /** 指数名称 */
    @ColumnInfo(name = "index_name")
    private String indexName;

    /** 指数 */
    @ColumnInfo(name = "index_number")
    private String indexNumber;

    /** 幅度 */
    @ColumnInfo(name = "index_range")
    private String indexRange;

    /** 百分比 */
    @ColumnInfo(name = "index_percent")
    private String indexPercent;

    /** 指数类型 主要区分 国内国外  1 为国内 2 为 香港 3 为 美国*/
    @ColumnInfo(name = "index_type")
    private String indexType;

    /** 指数名称 */
    @ColumnInfo(name = "index_type_unique")
    private String indexUnique;

    @ColumnInfo(name = "index_increase_type")
    private int indexIncreaseType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFundSyncId() {
        return fundSyncId;
    }

    public void setFundSyncId(long fundSyncId) {
        this.fundSyncId = fundSyncId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(String indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getIndexRange() {
        return indexRange;
    }

    public void setIndexRange(String indexRange) {
        this.indexRange = indexRange;
    }

    public String getIndexPercent() {
        return indexPercent;
    }

    public void setIndexPercent(String indexPercent) {
        this.indexPercent = indexPercent;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getIndexUnique() {
        return indexUnique;
    }

    public void setIndexUnique(String indexUnique) {
        this.indexUnique = indexUnique;
    }

    public int getIndexIncreaseType() {
        return indexIncreaseType;
    }

    public void setIndexIncreaseType(int indexIncreaseType) {
        this.indexIncreaseType = indexIncreaseType;
    }

    public IndexEntity createIndexEntity(){
        IndexEntity indexEntity = new IndexEntity();
        indexEntity.setFundSyncId(getFundSyncId());
        indexEntity.setIndexName(getIndexName());
        indexEntity.setIndexType(getIndexType());
        indexEntity.setIndexNumber(getIndexNumber());
        indexEntity.setIndexRange(getIndexRange());
        indexEntity.setIndexPercent(getIndexPercent());
        indexEntity.setTime(System.currentTimeMillis());
        indexEntity.setIndexIncreaseType(getIndexIncreaseType());
        return indexEntity;
    }
}
