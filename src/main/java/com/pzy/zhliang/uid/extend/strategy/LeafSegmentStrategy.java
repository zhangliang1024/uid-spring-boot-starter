package com.pzy.zhliang.uid.extend.strategy;

import com.pzy.zhliang.uid.baidu.exception.UidGenerateException;
import com.pzy.zhliang.uid.extend.annotation.UidModel;
import com.pzy.zhliang.uid.leaf.ISegmentService;
import com.pzy.zhliang.uid.leaf.SegmentServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Leaf分段批量Id策略(可配置asynLoadingSegment-异步标识)
 * @作者 庄梦蝶殇
 * @创建时间 2018年9月5日 上午11:35:53
 * @版本 1.00
 */
public class LeafSegmentStrategy implements IUidStrategy {

    private final static String MSG_UID_PARSE = "{\"UID\":\"%s\"}";

    private boolean asynLoadingSegment = true;

    protected JdbcTemplate jdbcTemplate;

    /**
     * 生成器集合
     */
    protected static Map<String, ISegmentService> generatorMap = new ConcurrentHashMap<>();

    /**
     * 获取uid生成器
     * @param prefix 前缀
     */
    public ISegmentService getSegmentService(String prefix) {
        ISegmentService segmentService = generatorMap.get(prefix);
        if (null == segmentService) {
            synchronized (generatorMap) {
                if (null == segmentService) {
                    segmentService = new SegmentServiceImpl(jdbcTemplate, prefix);
                }
                generatorMap.put(prefix, segmentService);
            }
        }
        return segmentService;
    }
    
    @Override
    public UidModel getName() {
        return UidModel.segment;
    }
    
    @Override
    public long getUID(String group) {
        if(StringUtils.isBlank(group)){
            throw new UidGenerateException("leaf strategy prefix is empty");
        }
        return getSegmentService(group).getId();
    }
    
    @Override
    public String parseUID(long uid, String group) {
        return String.format(MSG_UID_PARSE, uid);
    }

    public boolean isAsynLoadingSegment() {
        return asynLoadingSegment;
    }

    public void setAsynLoadingSegment(boolean asynLoadingSegment) {
        this.asynLoadingSegment = asynLoadingSegment;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
