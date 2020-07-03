package com.pzy.zhliang.uid.baidu.worker;

/**
 * 简单编号分配器(即不用workerId)
 * @作者 庄梦蝶殇
 * @创建时间 2018年9月5日 上午11:43:45
 * @版本 1.00
 */
public class SimpleWorkerIdAssigner implements WorkerIdAssigner {
    
    @Override
    public long assignWorkerId() {
        return 0;
    }
}
