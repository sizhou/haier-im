package com.haier.im.easemob.model;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IMUserModel {

    private boolean activated;//账户是否激活

    private String userName;//账户名(imId：md5（phone+2）)

//    TODO

}
