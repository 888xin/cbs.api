package com.lifeix.cbs.content.dto.inform;

import java.io.Serializable;
import java.util.Date;

/**
 * 举报信息
 * 
 * @author lifeix
 * 
 */
public class Inform implements Serializable {

    private static final long serialVersionUID = 6783231618298183720L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 举报类型 1：评论，2：吐槽，3：im
     */
    private Integer type;

    /**
     * 被举报内容id
     */
    private Long containId;
    /**
     * 被举报内容
     */
    private String contain;
    /**
     * 被举报图片
     */
    private String image;

    /**
     * 被举报者id
     */
    private Long userId;

    /**
     * 举报人id
     */
    private Long informerId;

    /**
     * 举报理由类型
     */
    private Integer informType;

    /**
     * 举报理由
     */
    private String informReason;

    /**
     * 状态 0：待处理，1：屏蔽，2：忽略
     */
    private Integer status;

    /**
     * 处理信息
     */
    private String disposeInfo;

    /**
     * 被举报次数
     */
    private Integer total;

    /**
     * 举报时间
     */
    private Date updateTime;

    /**
     * 解禁时间
     */
    private Date removeTime;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
    }

    public Long getContainId() {
	return containId;
    }

    public void setContainId(Long containId) {
	this.containId = containId;
    }

    public String getContain() {
	return contain;
    }

    public void setContain(String contain) {
	this.contain = contain;
    }

    public String getImage() {
	return image;
    }

    public void setImage(String image) {
	this.image = image;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Long getInformerId() {
	return informerId;
    }

    public void setInformerId(Long informerId) {
	this.informerId = informerId;
    }

    public Integer getInformType() {
	return informType;
    }

    public void setInformType(Integer informType) {
	this.informType = informType;
    }

    public String getInformReason() {
	return informReason;
    }

    public void setInformReason(String informReason) {
	this.informReason = informReason;
    }

    public Integer getStatus() {
	return status;
    }

    public void setStatus(Integer status) {
	this.status = status;
    }

    public String getDisposeInfo() {
	return disposeInfo;
    }

    public void setDisposeInfo(String disposeInfo) {
	this.disposeInfo = disposeInfo;
    }

    public Integer getTotal() {
	return total;
    }

    public void setTotal(Integer total) {
	this.total = total;
    }

    public Date getUpdateTime() {
	return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
	this.updateTime = updateTime;
    }

    public Date getRemoveTime() {
	return removeTime;
    }

    public void setRemoveTime(Date removeTime) {
	this.removeTime = removeTime;
    }

}
