package com.pzy.zhliang.uid.extend.annotation;

/**
 * @类描述 id生产模式
 * @作者  庄梦蝶殇
 * @创建时间 2018年9月3日 上午9:54:09
 * @版本 1.00
 */
public enum UidModel {
    /**
     * 步长自增(空实现,依赖数据库步长设置)
     */
    step("step"),
    /**
     * 分段批量(基于leaf)
     */
    segment("segment"),
    /**
     * Snowflake算法(源自twitter)
     */
    snowflake("snowflake"),
    /**
     * 百度UidGenerator
     */
    baidu("baidu");

    UidModel(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
