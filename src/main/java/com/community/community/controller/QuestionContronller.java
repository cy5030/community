package com.community.community.controller;


import com.community.community.dto.CommentDTO;
import com.community.community.dto.QuestionDTO;
import com.community.community.enums.CommentTypeErum;
import com.community.community.service.CommentService;
import com.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionContronller {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String modifiedQuestion(@PathVariable(name="id") Long id,
            Model model){
        QuestionDTO questionDTO  = questionService.getById(id);
        List<QuestionDTO> relatedQuestions= questionService.selectRelated(questionDTO);
        List<CommentDTO> comments= commentService.listByTargetId(id, CommentTypeErum.QUESTION);
        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
