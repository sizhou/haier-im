package com.haier.im.dao;

import com.haier.im.po.IMAccountToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMAccountTokenMapper {

    int upsertToken(IMAccountToken imAccountToken);

    IMAccountToken findSingleTokenByUserId(Long userId);

    IMAccountToken findSingleTokenBytoken(String token);


}
