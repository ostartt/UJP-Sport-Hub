package com.softserve.edu.sporthubujp.dto;

import lombok.Data;

@Data
public class ArticleSaveDTO {
    private String id;
    private String title;
    private String text;
    private String caption;
    private String alt;
    private String location;
    private String picture;
    private Boolean isActive;
    private Boolean commentsActive;
    private String categoryId;
    private String teamId;
}