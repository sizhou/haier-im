package com.haier.im.dao;

import com.haier.im.po.IMMsgFile;
import com.haier.im.vo.IMMsgFileVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface IMMsgFileMapper {


    int insertRecord(IMMsgFile imMsgFile);

    List<IMMsgFile> findMsgFilesByTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<IMMsgFileVo> findMsgFilesForCommunity(@Param("communityId") String communityId, @Param("pageSize") int pageSize, @Param("startIndex") int startIndex);

    int findCommunityFileCount(@Param("communityId") String communityId);

    List<IMMsgFileVo> findMsgFilesForChatUserId(@Param("senderId") String senderId, @Param("recverId") String recverId, @Param("pageSize") int pageSize, @Param("startIndex") int startIndex);

    int findUserFileCount(@Param("senderId") String senderId, @Param("recverId") String recverId);

    IMMsgFile findFileByMessageId(@Param("messageId") String messageId);

    int updateFileByMessageId(IMMsgFile imMsgFile);

}
