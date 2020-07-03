package com.pzy.zhliang.uid.extend.strategy;


import com.pzy.zhliang.uid.extend.annotation.UidModel;

/**
 * @类描述 uid策略接口
 * @作者  庄梦蝶殇
 * @创建时间 2018年8月31日 下午5:27:38
 * @版本 1.00
 */
public interface IUidStrategy {
    /**
     * 策略名
     */
    UidModel getName();

    /**
     * 获取ID
     * group 分组
     */
    long getUID(String group);

    /**
     * 解析ID
     * uid
     * group 分组
     * 输出json字符串：{\"UID\":\"\",\"timestamp\":\"\",\"workerId\":\"\",\"dataCenterId\":\"\",\"sequence\":\"\"}
     */
    String parseUID(long uid, String group);
}
