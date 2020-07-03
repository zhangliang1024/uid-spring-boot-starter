package com.pzy.zhliang.uid.extend.strategy;


import com.pzy.zhliang.uid.extend.annotation.UidModel;
import com.pzy.zhliang.uid.leaf.ISegmentService;
import com.pzy.zhliang.uid.spring.ColumnMaxValueIncrementer;

/**
 * spring 分段批量Id策略(可配置asynLoadingSegment-异步标识)
 * @作者  庄梦蝶殇
 * @创建时间 2019年3月15日 下午7:48:58
 * @版本 1.0.0
 */
public class SpringStrategy extends LeafSegmentStrategy {

    @Override
    public UidModel getName() {
        return UidModel.step;
    }
    
    @Override
    public ISegmentService getSegmentService(String prefix) {
        ISegmentService segmentService = generatorMap.get(prefix);
        if (null == segmentService) {
            synchronized (generatorMap) {
                if (null == segmentService) {
                    segmentService = new ColumnMaxValueIncrementer(jdbcTemplate, prefix);
                }
                generatorMap.put(prefix, segmentService);
            }
        }
        return segmentService;
    }
    
}
