package com.haier.im.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class BasePo implements Serializable {

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private String creator;

    @JsonIgnore
    private Date createTime;

    @JsonIgnore
    private Boolean isDelete;

    @JsonIgnore
    private Date updateTime;

}
