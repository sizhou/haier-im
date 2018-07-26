package com.haier.im.po;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class IMAccountInfo extends BasePo {

    private Long userId;//生成的唯一id

    private String realName;

    @JsonIgnore
    private Integer communityRole;

    private String portrait;

    @JsonIgnore
    private String phone;

    @JsonIgnore
    private Integer imType;//所在的Im平台

    private String imId;//平台id

    @JsonIgnore
    private String accountId;//账户id

    private boolean activated;


    @JsonIgnore
    private Integer status;
}
