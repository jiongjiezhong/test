package com.zjj.dto.token;

import io.swagger.annotations.ApiModelProperty;

/**
 * 通用tokenDto
 */
public class TokenDto {
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /** 冗余 */
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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
        return "TokenDto{" +
                "userId=" + userId +
                ", openId='" + openId + '\'' +
                ", img='" + img + '\'' +
                ", nick='" + nick + '\'' +
                ", appId='" + appId + '\'' +
                ", actId=" + actId +
                '}';
    }
}
