package com.zjj.dto.token;

import io.swagger.annotations.ApiModelProperty;

public class TokenDataDTO {
	@ApiModelProperty(value = "用户ID")
	private Integer userId;
	@ApiModelProperty(value = "用户OPENID")
	private String openId;
	@ApiModelProperty(value = "用户头像")
	private String img;
	@ApiModelProperty(value = "用户昵称")
	private String nick;
	@ApiModelProperty(value = "公众号appId")
	private String appId;
	@ApiModelProperty(value = "活动id")
	private Integer actId;
	@ApiModelProperty(value = "手机号")
	private String mobile;

	public TokenDataDTO() {
		super();
	}
	public TokenDataDTO(Integer userId) {
		super();
		this.userId = userId;
	}
	public TokenDataDTO(Integer userId, Integer actId, String mobile) {
		super();
		this.userId = userId;
		this.actId = actId;
		this.mobile = mobile;
	}
	public TokenDataDTO(Integer userId, Integer actId) {
		super();
		this.userId = userId;
		this.actId = actId;
	}

	public TokenDataDTO(Integer userId, String openId, String img, String nick, String appId) {
		this.userId = userId;
		this.openId = openId;
		this.img = img;
		this.nick = nick;
		this.appId = appId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	@Override
	public String toString() {
		return "TokenDataDTO{" +
				"userId=" + userId +
				", openId='" + openId + '\'' +
				", img='" + img + '\'' +
				", nick='" + nick + '\'' +
				", appId='" + appId + '\'' +
				", actId=" + actId +
				'}';
	}
}
