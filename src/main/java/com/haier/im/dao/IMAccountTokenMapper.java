package com.haier.im.dao;

import com.haier.im.po.IMAccountToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMAccountTokenMapper {

    int upsertToken(IMAccountToken imAccountToken);

    IMAccountToken findSingleTokenByUserId(Long userId);

    IMAccountToken findSingleTokenBytoken(String token);

    int delTokenByToken(@Param("token") String token);
}
