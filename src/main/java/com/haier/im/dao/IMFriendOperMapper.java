package com.haier.im.dao;

import com.haier.im.po.IMFriendOper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMFriendOperMapper {

    int addFriendOperLog(@Param("oper") IMFriendOper imFriendOper);


}
