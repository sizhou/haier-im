package com.haier.im.controller.reqvo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SelfAccountInfoReq implements Serializable {



    @ApiModelProperty(value = "昵称（realName）")
    private String nickName;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "生日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDay;




}
