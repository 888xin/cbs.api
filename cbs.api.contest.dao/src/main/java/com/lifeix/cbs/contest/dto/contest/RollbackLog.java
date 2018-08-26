package com.lifeix.cbs.contest.dto.contest;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhx on 16-3-7 下午3:39
 *
 * @Description
 */
public class RollbackLog implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 比赛id
     */
    private String descr ;

    /**
     * 创建时间
     */
    private Date createTime ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
