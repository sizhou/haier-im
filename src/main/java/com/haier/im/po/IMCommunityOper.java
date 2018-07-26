package com.haier.im.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class IMCommunityOper extends BasePo {


    private Long operUserId;//操作者
    private Long operedUserId;//被操作者
    @JsonIgnore
    private Integer actionType;//操作类型
    private String communityId;


}
