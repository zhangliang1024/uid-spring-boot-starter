package com.pzy.zhliang.uid.spring;

import com.pzy.zhliang.uid.leaf.SegmentServiceImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

/**
 * Spring 增量id实现(基于Segment策略)
 *
 * @作者 庄梦蝶殇
 * @创建时间 2018年9月12日 下午3:55:17
 * @版本 1.0.0
 */
public class ColumnMaxValueIncrementer extends SegmentServiceImpl implements DataFieldMaxValueIncrementer {

    /**
     * 填充长度
     */
    protected int paddingLength = 8;

    public ColumnMaxValueIncrementer(JdbcTemplate jdbcTemplate, String bizTag) {
        super(jdbcTemplate,bizTag);
    }

    @Override
    public int nextIntValue() throws DataAccessException {
        return getId().intValue();
    }

    @Override
    public long nextLongValue() throws DataAccessException {
        return getId();
    }

    @Override
    public String nextStringValue() throws DataAccessException {
        String s = Long.toString(getId());
        int len = s.length();
        if (len < this.paddingLength) {
            StringBuilder sb = new StringBuilder(this.paddingLength);
            for (int i = 0; i < this.paddingLength - len; i++) {
                sb.append('0');
            }
            sb.append(s);
            s = sb.toString();
        }
        return s;
    }

    public void setPaddingLength(int paddingLength) {
        this.paddingLength = paddingLength;
    }

    public int getPaddingLength() {
        return this.paddingLength;
    }
}
