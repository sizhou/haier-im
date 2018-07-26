package com.haier.im.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class IMCommunityUser extends BasePo {

    private String communityId;
    private Integer accountType;//账户类型（1：群主，2：管理员，3：普通群成员）
    private String nickName;
    private Long userId;//IMAccountInfo中id
    @JsonIgnore
    private Integer state;//状态
    private Boolean inviteAuth;//是否有权邀请别人


}
