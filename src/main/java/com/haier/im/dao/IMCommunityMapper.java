package com.haier.im.dao;

import com.haier.im.po.IMCommunity;
import com.haier.im.vo.IMCommunityVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMCommunityMapper {

    int addCommunity(@Param("community") IMCommunity community);

    IMCommunity findSingleByCommunityId(@Param("communityId") String communityId);

    int updateByPrimaryKeySelective(IMCommunity record);

    int updDelBy(@Param("community") IMCommunity community);

    List<IMCommunity> findCommunitiesByCondition(@Param("communityVo") IMCommunityVo communityVo);

    List<IMCommunity> findCommunitiesByIn(@Param("list") List<String> communityIdList);

}
