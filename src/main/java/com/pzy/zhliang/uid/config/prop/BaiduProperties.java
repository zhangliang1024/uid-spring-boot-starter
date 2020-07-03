package com.pzy.zhliang.uid.config.prop;

import com.pzy.zhliang.uid.config.constant.IdAssignerType;
import com.pzy.zhliang.uid.config.constant.UidGeneratorType;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @创建人：zhiang
 * @创建时间：2020/7/2 11:25
 * @version：V1.0
 */
public class BaiduProperties {

    /**
     * 基于ZK生成workId
     */
    @NestedConfigurationProperty
    private ZookeeperProperties zookeeper;
    /**
     * 基于Redis生成workId
     */
    @NestedConfigurationProperty
    private RedisProperties redis;
    /**
     * 默认：数据库生成workId
     */
    @NestedConfigurationProperty
    private DefaultProperties def;

    /**
     * workId生成策略: 默认数据库
     */
    private String assigner = IdAssignerType.DEFAULT_TYPE;
    /**
     * uid生成策略: default
     */
    private String generator = UidGeneratorType.GENERATOR_DEFAULT;


    public ZookeeperProperties getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(ZookeeperProperties zookeeper) {
        this.zookeeper = zookeeper;
    }

    public RedisProperties getRedis() {
        return redis;
    }

    public void setRedis(RedisProperties redis) {
        this.redis = redis;
    }

    public DefaultProperties getDef() {
        return def;
    }

    public void setDef(DefaultProperties def) {
        this.def = def;
    }

    public String getAssigner() {
        return assigner;
    }

    public void setAssigner(String assigner) {
        this.assigner = assigner;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

}
