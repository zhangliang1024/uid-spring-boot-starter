package com.pzy.zhliang.uid.leaf;

/**
 * Leaf理论的分段批量id生成服务
 * @作者 庄梦蝶殇
 * @创建时间 2018年9月5日 上午11:46:37
 * @版本 1.00
 */
public interface ISegmentService {
    /**
     * 获取id
     */
    Long getId();

    /**
     * 设置业务标识
     * bizTag 业务标识
     */
    void setBizTag(String bizTag);
}
