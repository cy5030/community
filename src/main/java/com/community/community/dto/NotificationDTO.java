package com.community.community.dto;

        import com.community.community.model.User;
        import lombok.Data;

@Data
public class NotificationDTO {

    private Long id;
    private String outerTitle;
    private Long gmtCreate;
    private Integer status;
    private String typeName;
    private Long notifier;
    private String notifierName;
    private Long outerId;
    private Integer type;
}
