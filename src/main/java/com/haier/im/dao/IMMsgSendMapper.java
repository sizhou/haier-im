package com.haier.im.dao;

import com.haier.im.po.IMMsgSend;
import com.haier.im.vo.IMMessageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IMMsgSendMapper {


    int insertRecord(IMMsgSend imMsgSend);


    /**
     * 查找群组消息
     *
     * @param communityId
     * @return
     */
    List<IMMessageVo> findCommunityMsgs(@Param("communityId") String communityId, @Param("pageSize") int pageSize, @Param("startIndex") int startIndex,
                                        @Param("startDate") String startDate, @Param("endDate") String endDate);

    int findCommunityMsgsCount(@Param("communityId") String communityId,@Param("startDate") String startDate, @Param("endDate") String endDate);


    /**
     * 好友聊天消息
     *
     * @param senderId
     * @param recverId
     * @param pageSize
     * @param startIndex
     * @return
     */
    List<IMMsgSend> findMsgsByChatImId(@Param("senderId") String senderId, @Param("recverId") String recverId,
                                       @Param("pageSize") int pageSize, @Param("startIndex") int startIndex,
                                       @Param("startDate") String startDate, @Param("endDate") String endDate);

    int findMsgsCountForChatIm(@Param("senderId") String senderId, @Param("recverId") String recverId,@Param("startDate") String startDate, @Param("endDate") String endDate);

}
