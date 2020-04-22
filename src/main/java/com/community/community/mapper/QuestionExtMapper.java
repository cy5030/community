package com.community.community.mapper;

import com.community.community.model.Question;
import com.community.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {

    int inView(Question record);
}