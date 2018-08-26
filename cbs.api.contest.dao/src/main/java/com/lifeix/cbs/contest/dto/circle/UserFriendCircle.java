package com.lifeix.cbs.contest.dto.circle;

import java.io.Serializable;

public class UserFriendCircle implements Serializable {
    private static final long serialVersionUID = 3553819354623149334L;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 猜友圈id
     */
    private Long friendCircleId;

    /**
     * 类型（心情|推荐）
     */
    private Integer type;
    /**
     * 删除标志位
     */
    private Boolean deleteFlag;
    /**
     * 是否包含爆料内容(有音频，图片，文字三者其一即为true)
     */
    private Boolean hasContent;

    public UserFriendCircle() {
    }
    
    public UserFriendCircle(Long friendCircleId) {
	super();
	this.friendCircleId = friendCircleId;
    }
    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Long getFriendCircleId() {
	return friendCircleId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Boolean getHasContent() {
        return hasContent;
    }

    public void setHasContent(Boolean hasContent) {
        this.hasContent = hasContent;
    }

    public void setFriendCircleId(Long friendCircleId) {
	this.friendCircleId = friendCircleId;
    }

}
