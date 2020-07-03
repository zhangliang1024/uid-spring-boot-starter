package com.pzy.zhliang.uid.config;

import com.pzy.zhliang.uid.UidContext;
import com.pzy.zhliang.uid.baidu.UidGenerator;
import com.pzy.zhliang.uid.baidu.impl.CachedUidGenerator;
import com.pzy.zhliang.uid.baidu.impl.DefaultUidGenerator;
import com.pzy.zhliang.uid.baidu.worker.*;
import com.pzy.zhliang.uid.baidu.worker.dao.WorkerNodeDAO;
import com.pzy.zhliang.uid.config.constant.IdAssignerType;
import com.pzy.zhliang.uid.config.constant.UidGeneratorType;
import com.pzy.zhliang.uid.config.prop.BaiduProperties;
import com.pzy.zhliang.uid.config.prop.DefaultProperties;
import com.pzy.zhliang.uid.config.prop.RedisProperties;
import com.pzy.zhliang.uid.config.prop.ZookeeperProperties;
import com.pzy.zhliang.uid.extend.annotation.UidModel;
import com.pzy.zhliang.uid.extend.strategy.BaiduUidStrategy;
import com.pzy.zhliang.uid.extend.strategy.LeafSegmentStrategy;
import com.pzy.zhliang.uid.extend.strategy.SpringStrategy;
import com.pzy.zhliang.uid.extend.strategy.TwitterSnowflakeStrategy;
import com.pzy.zhliang.uid.leaf.SegmentServiceImpl;
import com.pzy.zhliang.uid.spring.ColumnMaxValueIncrementer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @创建人：zhiang
 * @创建时间：2020/6/28 17:18
 * @version：V1.0
 */
@Configuration
@EnableConfigurationProperties(UidProperties.class)
@ConditionalOnProperty(value = "pzy.uid.enable",havingValue = "true")
public class UidConfiguration {

    @Autowired
    private UidProperties uidProperties;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /***********************************************workerId提供策略**************************************************/
    @Bean
    @ConditionalOnProperty(value = "pzy.uid.strategy",havingValue = "baidu")
    public WorkerIdAssigner workerIdAssigner(){
        BaiduProperties baidu = uidProperties.getBaidu();
        String strategy = uidProperties.getStrategy();
        if(baidu == null && UidModel.baidu.getName().equals(strategy)){
            DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner();
            disposableWorkerIdAssigner.setWorkerNodeDAO(workerNodeDAO());
            return disposableWorkerIdAssigner;
        }else if(baidu != null && UidModel.baidu.getName().equals(strategy)){
            String assigner = baidu.getAssigner();
            switch (assigner) {
                case IdAssignerType.ZOOKEEPER_TYPE:
                    ZkWorkerIdAssigner workerIdAssigner = new ZkWorkerIdAssigner();
                    ZookeeperProperties zooPro = baidu.getZookeeper();
                    if (zooPro != null) {
                        if(StringUtils.isNotBlank(zooPro.getZkAddress())){
                            workerIdAssigner.setZkAddress(zooPro.getZkAddress());
                        }
                        if(zooPro.getInterval().longValue() != 0L){
                            workerIdAssigner.setInterval(zooPro.getInterval());
                        }
                        if(StringUtils.isNotBlank(zooPro.getPidHome())){
                            workerIdAssigner.setPidHome(zooPro.getPidHome());
                        }
                        if(zooPro.getPidPort().intValue() != 0){
                            workerIdAssigner.setPidPort(zooPro.getPidPort());
                        }
                    }
                    return workerIdAssigner;
                case IdAssignerType.REDIS_TYPE:
                    RedisWorkIdAssigner redisWorkIdAssigner = new RedisWorkIdAssigner();
                    RedisProperties redPro = baidu.getRedis();
                    if (redPro != null) {
                        if(redPro.getInterval().longValue() != 0L){
                            redisWorkIdAssigner.setInterval(redPro.getInterval());
                        }
                        if(StringUtils.isNotBlank(redPro.getPidHome())){
                            redisWorkIdAssigner.setPidHome(redPro.getPidHome());
                        }
                        if(redPro.getPidPort().intValue() != 0){
                            redisWorkIdAssigner.setPidPort(redPro.getPidPort());
                        }
                    }
                    return redisWorkIdAssigner;
                case IdAssignerType.SIMPLE_TYPE:
                    SimpleWorkerIdAssigner simpleWorkerIdAssigner = new SimpleWorkerIdAssigner();
                    return simpleWorkerIdAssigner;
                default:
                    DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner();
                    disposableWorkerIdAssigner.setWorkerNodeDAO(workerNodeDAO());
                    return disposableWorkerIdAssigner;
            }
        }else {
            return null;
        }
    }
    /***********************************************workerId提供策略**************************************************/

    /***********************************************uid生成策略**************************************************/
    @Bean
    @ConditionalOnProperty(value = "pzy.uid.strategy",havingValue = "baidu")
    public UidGenerator uidGenerator(){
        BaiduProperties baidu = uidProperties.getBaidu();
        String strategy = uidProperties.getStrategy();
        if(baidu == null && UidModel.baidu.getName().equals(strategy)) {
            DefaultUidGenerator defaultUidGenerator = new DefaultUidGenerator();
            defaultUidGenerator.setWorkerIdAssigner(workerIdAssigner());
            return defaultUidGenerator;
        }else if(baidu != null && UidModel.baidu.getName().equals(strategy)){
            String generator = baidu.getGenerator();
            if(UidGeneratorType.GENERATOR_CACHED.equals(generator)){
                CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
                cachedUidGenerator.setWorkerIdAssigner(workerIdAssigner());
                return cachedUidGenerator;
            }else {
                DefaultUidGenerator defaultUidGenerator = new DefaultUidGenerator();
                DefaultProperties defPro = baidu.getDef();
                if(defPro != null){
                    if(StringUtils.isNotBlank(defPro.getEpochStr())){
                        defaultUidGenerator.setEpochStr(defPro.getEpochStr());
                    }
                    if(defPro.getTimeBits() != 0){
                        defaultUidGenerator.setTimeBits(defPro.getTimeBits());
                    }
                    if(defPro.getWorkerBits() != 0){
                        defaultUidGenerator.setWorkerBits(defPro.getWorkerBits());
                    }
                    if(defPro.getSeqBits() != 0){
                        defaultUidGenerator.setSeqBits(defPro.getSeqBits());
                    }
                }
                defaultUidGenerator.setWorkerIdAssigner(workerIdAssigner());
                return defaultUidGenerator;
            }
        }else {
            return null;
        }
    }
    /***********************************************uid生成策略**************************************************/


    /***********************************************Uid策略上下文**************************************************/
    /**
     * 默认使用：baidu策略
     */
    @Bean
    public UidContext uidContext(){
        String strategy = uidProperties.getStrategy();
        if(UidModel.baidu.getName().equals(strategy)){
            BaiduUidStrategy baiduUidStrategy = new BaiduUidStrategy();
            baiduUidStrategy.setWorkerIdAssigner(workerIdAssigner());
            baiduUidStrategy.setUidGenerator(uidGenerator());
            baiduUidStrategy.setWorkerNodeDAO(workerNodeDAO());
            baiduUidStrategy.setUidProp(uidProperties);
            return new UidContext(baiduUidStrategy);
        }else if(UidModel.segment.getName().equals(strategy)){
            LeafSegmentStrategy leafSegmentStrategy = new LeafSegmentStrategy();
            leafSegmentStrategy.setJdbcTemplate(jdbcTemplate);
            return new UidContext(leafSegmentStrategy);
        }else if(UidModel.step.getName().equals(strategy)){
            SpringStrategy springStrategy = new SpringStrategy();
            springStrategy.setJdbcTemplate(jdbcTemplate);
            return new UidContext(springStrategy);
        }else {
            return new UidContext(new TwitterSnowflakeStrategy());
        }
    }
    /***********************************************Uid策略上下文**************************************************/

    @Bean
    public WorkerNodeDAO workerNodeDAO(){
        return new WorkerNodeDAO();
    }

}
