package com.haier.im.po;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;

@Data
public class IMFriendOper extends BasePo {


    private Long operUserId;
    private Long operedUserId;
    @Ignore
    private Integer actionType;


}
