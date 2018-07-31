package com.haier.im.dao;


import com.haier.im.po.IMOper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMOperMapper {

    void insertOper (@Param("record") IMOper record);


}