package org.ygy.common.seckill.entity;

import java.io.Serializable;

public class UserEntity implements Serializable{
    /**
	 * 要序列化，redis  session共享
	 */
	private static final long serialVersionUID = 7572809110241194602L;

	private String userId;

    private String userAccount;

    private String userPwd;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount == null ? null : userAccount.trim();
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd == null ? null : userPwd.trim();
    }
}