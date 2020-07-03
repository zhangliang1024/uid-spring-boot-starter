package com.pzy.zhliang.uid.config.constant;

/**
 * @Author: zhliang
 * @Date: 2020/6/30 19:42
 * @Description:
 * @Version: V1.0
 */
public class IdAssignerType {

    /**
     * 基于zookeeper
     */
    public static final String ZOOKEEPER_TYPE = "zookeeper";
    /**
     * 基于redis
     */
    public static final String REDIS_TYPE = "redis";
    /**
     * 基于定长
     */
    public static final String SIMPLE_TYPE = "simple";
    /**
     * 默认数据库
     */
    public static final String DEFAULT_TYPE = "default";


}
