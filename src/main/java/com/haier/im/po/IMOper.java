package com.haier.im.po;

import lombok.Data;

import java.util.Date;

@Data
public class IMOper {


    private Long id;
    private String operateName;
    private String  operateArgs;
    private Date operateTime;


}
