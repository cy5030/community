package com.community.community.enums;

public enum CommentTypeErum {
    QUESTION(1),
    COMMENT(2);
    private Integer type;

    public static boolean isExist(Integer type) {
        for (CommentTypeErum commentTypeErum : CommentTypeErum.values()) {
            if(commentTypeErum.getType()==type){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }
    CommentTypeErum(Integer type) {
        this.type = type;
    }
}
