package com.community.community.service;

import com.community.community.dto.CommentDTO;
import com.community.community.enums.CommentTypeEnum;
import com.community.community.enums.NotificationStatusEnum;
import com.community.community.enums.NotificationTypeEnum;
import com.community.community.exception.CustomizeErrorCode;
import com.community.community.exception.CustomizeException;
import com.community.community.mapper.*;
import com.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;


    @Transactional
    public void createOrUpdate(Comment comment, User commentator) {
        if(comment.getParentId()==null||comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType()==null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType()== CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment= commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_PARAM_NOT_FOUND);
            }else{
                commentMapper.insert(comment);
                //增加评论数
                Comment parentComment = new Comment();
                parentComment.setId(comment.getParentId());
                parentComment.setCommentCount(1);
                commentExtMapper.incCommentCount(parentComment);
                Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
                if(question == null){
                    throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
                }
                //创建通知
                createNotification(comment, dbComment.getCommentator(),commentator.getName(),question.getTitle(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
            }
        }else{
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }else {

                commentMapper.insert(comment);
                question.setCommentCount(1);
                questionExtMapper.incComment(question);
                //创建通知
                createNotification(comment, commentator.getId(),commentator.getName(),question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
            }
        }

    }

    private void createNotification(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if(receiver == comment.getCommentator())
            return ;
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterId(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size()==0){
            return new ArrayList<>();
        }

        //获取去重的评论人（commentator）
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        //将评论人转为Map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment为commentDTO
        List<CommentDTO> commentDTOList = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            //拷贝comment中相同属性的值到commentDTO
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOList;
    }
}
