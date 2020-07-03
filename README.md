# uid-spring-boot-starter
> 基于 美团leaf、百度UidGenerator、原生snowflake 进行整合的 唯一ID生成器 starter

### 一介绍：

### 二、说明


### 三、使用
> 如果使用百度策略：DisposableWorkerIdAssigner，利用数据库来管理生成workId。
```sql
DROP TABLE IF EXISTS WORKER_NODE;
CREATE TABLE WORKER_NODE (
    ID BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增 id',
    HOST_NAME VARCHAR(64) NOT NULL COMMENT '主机名',
    PORT VARCHAR(64) NOT NULL COMMENT '端口',
    TYPE INT NOT NULL COMMENT '节点类型: ACTUAL or CONTAINER',
    LAUNCH_DATE DATE NOT NULL COMMENT '启动时间',
    MODIFIED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY(ID)
) COMMENT='DB WorkerID Assigner for UID Generator',ENGINE = INNODB;
```
> segment 是基于美团leaf-segment 的优化策略, 使用双Buffer实现。
> 
> 需要初始化数据：INSERT INTO id_segment (BIZ_TAG,STEP,MAX_ID) values ("order",20,100000);
```sql
DROP TABLE IF EXISTS id_segment;
CREATE TABLE `id_segment` (
  `BIZ_TAG` varchar(64) NOT NULL COMMENT '业务标识',
  `STEP` int(11) NOT NULL COMMENT '步长',
  `MAX_ID` bigint(20) NOT NULL COMMENT '最大值',
  `LAST_UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上次修改时间',
  `CURRENT_UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '当前修改时间',
  PRIMARY KEY (`BIZ_TAG`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='号段存储表';
```

> pom.xml 加入mysql依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>com.pzy.zhliang</groupId>
    <artifactId>uid-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.47</version>
    <optional>true</optional>
</dependency>
```
> application.yml 
```yaml
pzy:
  uid:
    # 默认开启 snowflake 策略
    enable: true
#    # 支持ID生产策略有四种：baidu snowflake segment step  默认使用：snowflake
#    # 使用默认策略，以下均可以不配置
#    strategy: baidu  # baidu snowflake segment step
#    # 仅在使用百度策略时，配置都有默认值。
#    # 如需修改默认值，则可以修改以下配置
#    baidu:
#      # baidu 是 基于百度UidGenerator上的的优化策略
#      # workerId提供策略 : 默认 default
#      #       default 利用数据库来管理生成workId 
#      #       simple 固定了workId的提供 值为0 
#      #       zookeeper 利用zookeeper来实现wordId的提供管理 
#      #       redis 利用redis来实现wordId的提供管理 
#      assigner: default # default simple zookeeper redis
#      # uid生成策略 : default
#      #   default: 是Snowflake算法的变种，取消datacenterId, 并扩展了支持自定义workerId位数和初始化策略
#      #   cached: 借用未来时间来解决sequence天然存在的并发限制; 采用RingBuffer来缓存已生成的UID, 并行化UID的生产和消费,
#      #           同时对CacheLine补齐，避免了由RingBuffer带来的硬件级「伪共享」问题. 最终单机QPS可达600万
#      generator: default # default cached
#      zookeeper:
#        interval: 3000
#        zk-address: localhost:2181
#        pid-port: 60982
#      redis:
#        interval: 3000
#        pid-port: 60982
#      def:
#        time-bits: 29
#        worker-bits: 21
#        seq-bits: 13
#        epoch-str: 2020-06-29
```

> 加入包扫描：@MapperScan(basePackages = "com.pzy.zhliang.uid.baidu.worker.dao")
```java
@MapperScan(basePackages = "com.pzy.zhliang.uid.baidu.worker.dao")
@SpringBootApplication
public class SpringbootUidSampleApplication {
```

> 测试
```java
@RestController
@RequestMapping("uid")
public class UidContextController {

    @Autowired
    private UidContext context;

    @GetMapping
    public void getUID() {
        System.out.println(context.getUID());
        System.out.println("get Uid group order :  " +context.getUID("order"));
        System.out.println(context.getUID("test"));
        System.out.println("get Uid group test: " +context.getUID("test"));
    }
```
