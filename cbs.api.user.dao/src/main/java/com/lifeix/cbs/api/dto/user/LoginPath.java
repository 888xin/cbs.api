package com.lifeix.cbs.api.dto.user;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lhx on 16-4-19 下午2:29
 *
 * @Description
 */
public class LoginPath implements Serializable {

    private static final long serialVersionUID = 4215374099303156181L;
    private Long id;

    private String days;

    private String amounts;

    private Date updateTime ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getAmounts() {
        return amounts;
    }

    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
