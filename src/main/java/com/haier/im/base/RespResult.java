package com.haier.im.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class RespResult<T> implements Serializable{
    private static final long serialVersionUID = -1L;
    @ApiModelProperty(value = "返回状态码")
    private String code;

    @ApiModelProperty(value = "返回提示信息")
    private String msg;

    @ApiModelProperty(value = "返回体")
    private T data;
}
