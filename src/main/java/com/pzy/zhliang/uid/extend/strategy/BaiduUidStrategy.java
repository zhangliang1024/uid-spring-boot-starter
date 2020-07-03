package com.pzy.zhliang.uid.extend.strategy;

import com.pzy.zhliang.uid.baidu.UidGenerator;
import com.pzy.zhliang.uid.baidu.exception.UidGenerateException;
import com.pzy.zhliang.uid.baidu.impl.CachedUidGenerator;
import com.pzy.zhliang.uid.baidu.impl.DefaultUidGenerator;
import com.pzy.zhliang.uid.baidu.worker.*;
import com.pzy.zhliang.uid.baidu.worker.dao.WorkerNodeDAO;
import com.pzy.zhliang.uid.config.UidProperties;
import com.pzy.zhliang.uid.config.constant.IdAssignerType;
import com.pzy.zhliang.uid.config.constant.UidGeneratorType;
import com.pzy.zhliang.uid.config.prop.BaiduProperties;
import com.pzy.zhliang.uid.extend.annotation.UidModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * baidu uid生成策略
 * @作者 庄梦蝶殇
 * @创建时间 2018年4月27日 下午8:47:27
 * @版本 1.00
 */
public class BaiduUidStrategy implements IUidStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaiduUidStrategy.class);

    private static Map<String, UidGenerator> generatorMap = new HashMap<>();

    protected WorkerIdAssigner workerIdAssigner;

    private WorkerNodeDAO workerNodeDAO;

    private UidGenerator uidGenerator;

    private UidProperties uidProp;

    @Override
    public UidModel getName() {
        return UidModel.baidu;
    }
    
    /**
     * 获取uid生成器
     * @param prefix 前缀
     */
    public UidGenerator getUidGenerator(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return uidGenerator;
        }
        UidGenerator generator = generatorMap.get(prefix);
        if (null == generator) {
            synchronized (generatorMap) {
                if (null == generator) {
                    generator = getGenerator();
                }
                generatorMap.put(prefix, generator);
            }
        }
        return generator;
    }
    
    @Override
    public long getUID(String group) {
        return getUidGenerator(group).getUID();
    }
    
    @Override
    public String parseUID(long uid, String group) {
        return getUidGenerator(group).parseUID(uid);
    }
    
    /**
     * 多实例返回uidGenerator(返回值不重要，动态注入)
     * TODO 这里获取的对象是多例实现的，但Lookup注解没有实现多例的效果。所以改为手动构建多实例对象
     */
    public UidGenerator getGenerator() {
        BaiduProperties baiPro = uidProp.getBaidu();
        if(baiPro != null){
            if(UidGeneratorType.GENERATOR_CACHED.equals(baiPro.getGenerator())){
                return getCachedUidGenerator(baiPro.getAssigner());
            }else {
                return getDefaultUidGenerator(baiPro.getAssigner());
            }
        }else {
            return getDefaultUidGenerator(IdAssignerType.DEFAULT_TYPE);
        }

    }

    private UidGenerator getDefaultUidGenerator(String assigner) {
        DefaultUidGenerator defaultUidGenerator = new DefaultUidGenerator();
        try {
            defaultUidGenerator.setWorkerIdAssigner(workerIdAssigner);
            defaultUidGenerator.afterPropertiesSet();
        } catch (Exception e) {
            LOGGER.error("Initialized workerID exception. ", e);
            throw new UidGenerateException(e);
        }
        return defaultUidGenerator;
    }

    private UidGenerator getCachedUidGenerator(String assigner) {
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        try {
            cachedUidGenerator.setWorkerIdAssigner(workerIdAssigner);
            cachedUidGenerator.afterPropertiesSet();
        } catch (Exception e) {
            LOGGER.error("Initialized RingBuffer exception. ", e);
            throw new UidGenerateException(e);
        }
        return cachedUidGenerator;
    }

    public UidGenerator getUidGenerator() {
        return uidGenerator;
    }

    public void setUidGenerator(UidGenerator uidGenerator) {
        this.uidGenerator = uidGenerator;
    }

    public UidProperties getUidProp() {
        return uidProp;
    }

    public void setUidProp(UidProperties uidProp) {
        this.uidProp = uidProp;
    }

    public WorkerNodeDAO getWorkerNodeDAO() {
        return workerNodeDAO;
    }

    public void setWorkerNodeDAO(WorkerNodeDAO workerNodeDAO) {
        this.workerNodeDAO = workerNodeDAO;
    }

    public WorkerIdAssigner getWorkerIdAssigner() {
        return workerIdAssigner;
    }

    public void setWorkerIdAssigner(WorkerIdAssigner workerIdAssigner) {
        this.workerIdAssigner = workerIdAssigner;
    }
}
