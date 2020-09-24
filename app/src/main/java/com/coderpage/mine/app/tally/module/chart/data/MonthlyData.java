package com.coderpage.mine.app.tally.module.chart.data;

import java.util.Locale;

/**
 * @author lc. 2017-10-19 20:42
 * @since 0.5.1
 */

public class MonthlyData {

    /** 月份 */
    private Month month;
    /** 月总金额 */
    private float amount;
    /** 账单记录数量 */
    private long count;

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "[month=%s,total=%f]", String.valueOf(month), amount);
    }
}
