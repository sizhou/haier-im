package com.haier.im.service;

import com.haier.im.base.RespResult;

public interface IMAccountTokenService {


    RespResult authIMUserByPhone(String phone);




    String createToken(long userId);


    boolean checkToken(String token);


    Long getUserIdByToken(String token);


    boolean clearToken(String token);

}
