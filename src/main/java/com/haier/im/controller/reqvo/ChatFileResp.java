package com.haier.im.controller.reqvo;

import com.haier.im.vo.IMMsgFileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ChatFileResp implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 总条数
     */
    @ApiModelProperty(value = "总条数")
    private Integer total;

    /**
     * 页码.
     */
    @ApiModelProperty(value = "页码")
    private Integer pageNo;
    /**
     * 每页长度.
     */
    @ApiModelProperty(value = "每页长度")
    private Integer pageSize;

//    private List<ChatFileData> files;

    private List<IMMsgFileVo> files;
}
