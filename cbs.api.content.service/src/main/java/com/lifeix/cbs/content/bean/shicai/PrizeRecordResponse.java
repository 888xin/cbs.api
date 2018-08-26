package com.lifeix.cbs.content.bean.shicai;
/**
 * @author wenhuans
 * 2015年11月5日 下午6:33:15
 * 
 */
public class PrizeRecordResponse {

    /**
     * 前3名的用户id
     */
    private Long accountId;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 奖励床点
     */
    private Long bedpoint;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getBedpoint() {
        return bedpoint;
    }

    public void setBedpoint(Long bedpoint) {
        this.bedpoint = bedpoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}

