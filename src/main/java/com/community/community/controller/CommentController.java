package com.community.community.controller;

import com.community.community.dto.CommentCreateDTO;
import com.community.community.dto.CommentDTO;
import com.community.community.dto.ResultDTO;
import com.community.community.enums.CommentTypeErum;
import com.community.community.exception.CustomizeErrorCode;
import com.community.community.model.Comment;
import com.community.community.model.User;
import com.community.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method= RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentDTO,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if(commentDTO==null || StringUtils.isBlank(commentDTO.getComment())){
            return ResultDTO.errorOf(CustomizeErrorCode.EMPTY_COMMENT);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setCommentText(commentDTO.getComment());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0);
        comment.setCommentCount(0);
        commentService.createOrUpdate(comment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method= RequestMethod.GET)
    public ResultDTO<List> comments(@PathVariable("id") Long id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeErum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
