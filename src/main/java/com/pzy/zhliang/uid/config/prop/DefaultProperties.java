package com.pzy.zhliang.uid.config.prop;


/**
 * @类描述：
 * @创建人：zhiang
 * @创建时间：2020/6/29 14:59
 * @version：V1.0
 */
public class DefaultProperties {

    /**
     * 当前时间: 相对于时间基点"2016-05-20"的增量值，单位：秒，最多可支持约8.7年
     */
    private int timeBits = 28;
    /**
     * 机器ID: 最多可支持约420w次机器启动
     */
    private int workerBits = 22;
    /**
     * 每秒下的并发序列: 13 bits可支持每秒8192个并发
     */
    private int seqBits = 13;
    /**
     * 时间基点
     */
    private String epochStr = "2016-05-20";

    public int getTimeBits() {
        return timeBits;
    }

    public void setTimeBits(int timeBits) {
        this.timeBits = timeBits;
    }

    public int getWorkerBits() {
        return workerBits;
    }

    public void setWorkerBits(int workerBits) {
        this.workerBits = workerBits;
    }

    public int getSeqBits() {
        return seqBits;
    }

    public void setSeqBits(int seqBits) {
        this.seqBits = seqBits;
    }

    public String getEpochStr() {
        return epochStr;
    }

    public void setEpochStr(String epochStr) {
        this.epochStr = epochStr;
    }

}
