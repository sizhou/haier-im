package com.haier.im.dao;

import com.haier.im.po.IMAccountDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMAccountDetailMapper {

    int insertAccountDetail(@Param("addEn") IMAccountDetail addEn);

    int updDelAccountDetailByUserId(@Param("userId") Long userId);

    int updAccountDetailByUserId(@Param("updEn") IMAccountDetail updEn, @Param("userId") Long userId);

    IMAccountDetail findAccountDetailByUserId(Long userId);


//    TODO


}
