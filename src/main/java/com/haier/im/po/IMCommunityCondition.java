package com.haier.im.po;

import lombok.Data;

@Data
public class IMCommunityCondition extends BasePo {

    private String communityId;
    private String conditionQuestion;
    private String conditionAnswer;
}
