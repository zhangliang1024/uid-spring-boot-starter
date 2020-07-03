package com.pzy.zhliang.uid.config.prop;


/**
 * @创建人：zhiang
 * @创建时间：2020/6/29 14:54
 * @version：V1.0
 */
public class RedisProperties {

    /**
     * 本地workid文件跟目录
     */
    public static final String PID_ROOT = "/data/redis/pids/";
    /**
     * 心跳间隔
     */
    private Long interval = 3000L;
    /**
     * workerID 文件存储路径
     */
    private String pidHome = PID_ROOT;
    /**
     * 使用端口(同机多uid应用时区分端口)
     */
    private Integer pidPort = 60982;

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }

    public String getPidHome() {
        return pidHome;
    }

    public void setPidHome(String pidHome) {
        this.pidHome = pidHome;
    }

    public Integer getPidPort() {
        return pidPort;
    }

    public void setPidPort(Integer pidPort) {
        this.pidPort = pidPort;
    }

}

