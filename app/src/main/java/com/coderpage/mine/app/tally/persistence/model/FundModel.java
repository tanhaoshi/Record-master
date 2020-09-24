package com.coderpage.mine.app.tally.persistence.model;

import android.arch.persistence.room.ColumnInfo;

import com.coderpage.mine.app.tally.persistence.sql.entity.FundEntity;

import java.io.Serializable;

/**
 * create by ths on 2020/9/9
 */
public class FundModel implements Serializable {

    @ColumnInfo(name = "fund_id")
    private long id;

    /** 指数代码 */
    @ColumnInfo(name = "fund_sync_id")
    private long fundSyncId;

    /** 记录时间（UNIX TIME） */
    @ColumnInfo(name = "create_time")
    private long time;

    // id, 同步id , 创建时间, 指数名称, 指数 , 幅度 , 百分比 , 类型 , 类型名
    /** 指数名称 */
    @ColumnInfo(name = "fund_name")
    private String fundName;

    /** 指数 */
    @ColumnInfo(name = "fund_number")
    private String fundNumber;

    /** 幅度 */
    @ColumnInfo(name = "fund_range")
    private String fundRange;

    /** 百分比 */
    @ColumnInfo(name = "fund_percent")
    private String fundPercent;

    /** 指数类型 主要区分 国内国外*/
    @ColumnInfo(name = "fund_type")
    private String fundType;

    /** 指数名称 */
    @ColumnInfo(name = "fund_type_unique")
    private String fundUnique;

    @ColumnInfo(name = "fund_increase_type")
    private int fundIncreaseType;

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

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundNumber() {
        return fundNumber;
    }

    public void setFundNumber(String fundNumber) {
        this.fundNumber = fundNumber;
    }

    public String getFundRange() {
        return fundRange;
    }

    public void setFundRange(String fundRange) {
        this.fundRange = fundRange;
    }

    public String getFundPercent() {
        return fundPercent;
    }

    public void setFundPercent(String fundPercent) {
        this.fundPercent = fundPercent;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getFundUnique() {
        return fundUnique;
    }

    public void setFundUnique(String fundUnique) {
        this.fundUnique = fundUnique;
    }

    public int getFundIncreaseType() {
        return fundIncreaseType;
    }

    public void setFundIncreaseType(int fundIncreaseType) {
        this.fundIncreaseType = fundIncreaseType;
    }

    public FundEntity createEntity() {
        FundEntity entity = new FundEntity();
        entity.setId(getId());
        entity.setFundName(getFundName());
        entity.setFundNumber(getFundNumber());
        entity.setFundPercent(getFundPercent());
        entity.setFundRange(getFundRange());
        entity.setFundSyncId(getFundSyncId());
        entity.setFundType(getFundType());
        entity.setFundUnique(getFundUnique());
        entity.setTime(getTime());
        entity.setFundIncreaseType(getFundIncreaseType());
        return entity;
    }
}
