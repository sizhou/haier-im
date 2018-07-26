package com.haier.im.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class IMAccountDetail extends BasePo {

    private Long userId;

    private int sex;

    private Date birthday;


}
