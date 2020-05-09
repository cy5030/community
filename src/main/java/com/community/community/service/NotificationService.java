package com.community.community.service;

import com.community.community.dto.NotificationDTO;
import com.community.community.dto.PageDTO;
import com.community.community.enums.NotificationStatusEnum;
import com.community.community.enums.NotificationTypeEnum;
import com.community.community.exception.CustomizeErrorCode;
import com.community.community.exception.CustomizeException;
import com.community.community.mapper.NotificationMapper;
import com.community.community.mapper.UserMapper;
import com.community.community.model.Notification;
import com.community.community.model.NotificationExample;
import com.community.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    public PageDTO<NotificationDTO> list(Long userId, Integer page, Integer size) {
        PageDTO<NotificationDTO> pageDTO = new PageDTO<>();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int)notificationMapper.countByExample(notificationExample);
        Integer totalPage;
        if(totalCount%size==0){
            totalPage = totalCount/size;
        }else{
            totalPage = totalCount/size + 1;
        }
        if(page<1){
            page = 1;
        }
        if(page>totalPage){
            page = totalPage;
        }
        pageDTO.setPage(totalPage, page);
        Integer offset = size * (page - 1);
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notificationList = notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));
        if(notificationList.size()==0){
            return pageDTO;
        }
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notificationList) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        pageDTO.setData(notificationDTOS);
        System.out.println(notificationDTOS.size());
        return pageDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId)
                .andStatusEqualTo(0);
        Long totalCount = (Long)notificationMapper.countByExample(notificationExample);
       return totalCount;
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification==null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_MISSED);
        }
        if(!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAILED);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
