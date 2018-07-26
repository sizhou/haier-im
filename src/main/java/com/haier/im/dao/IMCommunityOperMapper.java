package com.haier.im.dao;


import com.haier.im.po.IMCommunityOper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMCommunityOperMapper {
    int insertOper(@Param("record") IMCommunityOper record);
}