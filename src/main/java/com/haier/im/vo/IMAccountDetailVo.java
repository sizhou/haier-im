package com.haier.im.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class IMAccountDetailVo extends IMAccountInfoVo {

    private Long userId;

    private int sex;

    private Date birthday;


}
