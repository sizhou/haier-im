package com.haier.im.service;

import com.haier.im.base.RespResult;
import com.haier.im.po.IMAccountInfo;

public interface IMAccountTokenService {


    RespResult authIMUserByPhone(String phone);




    String createToken(IMAccountInfo user);


    boolean checkToken(String token);


    Long getUserIdByToken(String token);



    boolean clearToken(String token);

}
