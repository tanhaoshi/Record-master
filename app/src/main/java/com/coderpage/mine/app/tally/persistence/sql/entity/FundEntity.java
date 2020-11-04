package com.coderpage.mine.app.tally.persistence.sql.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * create by ths on 2020/9/8
 *
 * indices 索引
 */
@Entity(tableName = "fund", indices = {@Index(value = {"fund_type_unique"}, unique = true)})
public class FundEntity {

    @ColumnInfo(name = "fund_id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "fund_sync_id")
    private long fundSyncId;

    /** 记录时间（UNIX TIME） */
    @ColumnInfo(name = "create_time")
    private long time;

    // id, 同步id , 创建时间, 指数名称, 基金 , 幅度 , 百分比 , 类型 , 类型名
    /** 指数名称 */
    @ColumnInfo(name = "fund_name")
    private String fundName;

    /** 基金 */
    @ColumnInfo(name = "fund_number")
    private String fundNumber;

    /** 幅度 */
    @ColumnInfo(name = "fund_range")
    private String fundRange;

    /** 日增长百分比 */
    @ColumnInfo(name = "fund_percent")
    private String fundPercent;

    /** 基金类型 主要区分 国内国外*/
    @ColumnInfo(name = "fund_type")
    private String fundType;

    /** 基金唯一标识符 */
    @ColumnInfo(name = "fund_type_unique")
    private String fundUnique;

    /** 区分 跌还是涨了 */
    @ColumnInfo(name = "fund_increase_type")
    private int fundIncreaseType;

    /** 基金净值 当天 */
    @ColumnInfo(name="fund_today_worth")
    private String fundTodayWorth;

    /** 昨天基金净值 */
    @ColumnInfo(name="fund_yesterday_worth")
    private String fundYesterdayWorth;

    /** 日增长纯净值  使用当天减去 昨天的 */
    @ColumnInfo(name = "fund_net_profit")
    private String fundNetProfit;

    /** 当前申购状态 */
    @ColumnInfo(name = "fund_apply_status")
    private String fundApplyStatus;

    /** 当前赎回状态 */
    @ColumnInfo(name = "fund_redeem_status")
    private String fundRedeemStatus;

    /** 手续费 */
    @ColumnInfo(name = "fund_service_charge")
    private String fundServiceCharge;

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

    public String getFundTodayWorth() {
        return fundTodayWorth;
    }

    public void setFundTodayWorth(String fundTodayWorth) {
        this.fundTodayWorth = fundTodayWorth;
    }

    public String getFundYesterdayWorth() {
        return fundYesterdayWorth;
    }

    public void setFundYesterdayWorth(String fundYesterdayWorth) {
        this.fundYesterdayWorth = fundYesterdayWorth;
    }

    public String getFundNetProfit() {
        return fundNetProfit;
    }

    public void setFundNetProfit(String fundNetProfit) {
        this.fundNetProfit = fundNetProfit;
    }

    public String getFundApplyStatus() {
        return fundApplyStatus;
    }

    public void setFundApplyStatus(String fundApplyStatus) {
        this.fundApplyStatus = fundApplyStatus;
    }

    public String getFundRedeemStatus() {
        return fundRedeemStatus;
    }

    public void setFundRedeemStatus(String fundRedeemStatus) {
        this.fundRedeemStatus = fundRedeemStatus;
    }

    public String getFundServiceCharge() {
        return fundServiceCharge;
    }

    public void setFundServiceCharge(String fundServiceCharge) {
        this.fundServiceCharge = fundServiceCharge;
    }
}
