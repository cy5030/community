package com.community.community.mapper;

import com.community.community.model.Question;

import java.util.List;

public interface QuestionExtMapper {

    int incView(Question record);
    int incComment(Question record);
    List<Question> selectRelated(Question record);

}