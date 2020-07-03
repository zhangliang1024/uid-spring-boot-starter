package com.pzy.zhliang.uid.config;

import com.pzy.zhliang.uid.config.constant.IdAssignerType;
import com.pzy.zhliang.uid.config.constant.UidGeneratorType;
import com.pzy.zhliang.uid.config.prop.BaiduProperties;
import com.pzy.zhliang.uid.extend.annotation.UidModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import javax.annotation.PostConstruct;


/**
 * @创建人：zhiang
 * @创建时间：2020/6/29 14:51
 * @version：V1.0
 */
@ConfigurationProperties(prefix = UidProperties.UID_PREFIX)
public class UidProperties {

    private static final Logger logger = LoggerFactory.getLogger(UidProperties.class);

    public static final String UID_PREFIX = "pzy.uid";
    /**
     * 是否启用
     */
    private boolean enable;

    @NestedConfigurationProperty
    private BaiduProperties baidu;

    /**
     * UID 生成策略: 默认baidu
     *  baidu
     *  snowflake
     *  segment
     *  step
     */
    private String strategy = UidModel.snowflake.getName();

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public BaiduProperties getBaidu() {
        return baidu;
    }

    public void setBaidu(BaiduProperties baidu) {
        this.baidu = baidu;
    }

    @PostConstruct
    private void checkUidPros() {
        logger.info("========= Uid Prop 初始化 =========");
        logger.info("========= Uid 启用策略：{} =========",strategy);
        if(UidModel.baidu.getName().equals(strategy)){
            if(baidu != null){
                logger.info("========= workId生成策略：{} =========",baidu.getAssigner());
                logger.info("========= uid生成策略：{} =========",baidu.getGenerator());
            }else {
                logger.info("========= workId生成策略：{} =========", IdAssignerType.DEFAULT_TYPE);
                logger.info("========= uid生成策略：{} =========", UidGeneratorType.GENERATOR_DEFAULT);
            }
        }
    }
}
