package com.haier.im.dao;

import com.haier.im.po.IMCommunityUser;
import com.haier.im.vo.ImCommunityUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMCommunityUserMapper {

    int addCommunityUser(IMCommunityUser communityUser);

    int delCommunityUserBy(@Param("delUser") IMCommunityUser communityUser);

    List<ImCommunityUserVo> findCommunityUserBy(@Param("findEn") IMCommunityUser findEn);

    int updUserNickNameBySelf(@Param("communityId") String communityId, @Param("userId") Long userId, @Param("nickName") String nickName);

}
