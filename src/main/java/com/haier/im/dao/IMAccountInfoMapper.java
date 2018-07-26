package com.haier.im.dao;

import com.haier.im.po.IMAccountInfo;
import com.haier.im.vo.IMAccountDetailVo;
import com.haier.im.vo.IMAccountInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMAccountInfoMapper {

    /**
     * 按照唯一标识(id 或者 phone+isDelete)
     * 查询单个用户
     * @param findEn
     * @return
     */
    IMAccountInfo selectSingleAccountBy(@Param("findEn") IMAccountInfo findEn);


    IMAccountDetailVo selectSingleAccountDetailBy(@Param("findEn") IMAccountInfo findEn);

    /**
     * 根据各种条件查询账户
     * @param findVo
     * @return
     */
    List<IMAccountInfo> selectAccountsBy(@Param("findVo") IMAccountInfoVo findVo);

    /**
     * 新增账户信息
     * @param record
     * @return
     */
    int insertNonil(IMAccountInfo record);

    int updateAccountBy(@Param("updateEn") IMAccountInfo updateEn);



}
