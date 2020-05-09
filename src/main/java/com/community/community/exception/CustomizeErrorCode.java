package com.community.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND(2002 ,"你找的问题不存在！！！"),
    TARGET_PARAM_NOT_FOUND(2001 ,"未选中任何问题或回复进行回复！！！"),
    COMMENT_PARAM_NOT_FOUND(2006 ,"该回复已经不见了，无法回复！！！"),
    NO_LOGIN(2003 ,"当前操作需要登录！！！"),
    SYSTEM_ERROR(2004 ,"服务器不堪重负了！！！"),
    TYPE_PARAM_WRONG(2005 ,"回复类型出错或不存在！！！"),
    EMPTY_COMMENT(2007 ,"输入内容不能为空！！！"),
    READ_NOTIFICATION_FAILED(2008 ,"通知读取错误！！！"),
    NOTIFICATION_MISSED(2009 ,"通知不见了！！！");

    private Integer code;
    private String message;
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }
}
