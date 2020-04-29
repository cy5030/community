package com.community.community.mapper;

import com.community.community.model.Comment;

public interface CommentExtMapper {

    int incCommentCount(Comment comment);
}