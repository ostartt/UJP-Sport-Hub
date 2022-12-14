package com.softserve.edu.sporthubujp.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ArticleListDTO {
    private String id;
    private String title;
    private String shortText;
    private Boolean isActive;
    private CategoryDTO category;
    private String caption;
    private LocalDateTime createDateTime;
    private String picture;

    private String getFirstSentence(String articleText){
        char[] textArray = articleText.toCharArray();
        List<Character> firstSentenceArray = new ArrayList<Character>();
        int i = 0;
        while(textArray[i] != '.' && i < textArray.length-1){
            firstSentenceArray.add(textArray[i]);
            ++i;
        }

        StringBuilder builder = new StringBuilder(firstSentenceArray.size());
        for(Character ch: firstSentenceArray)
        {
            builder.append(ch);
        }
        return builder.toString();
    }
    public ArticleListDTO(ArticleDTO articleDTO) {
        this.id = articleDTO.getId();
        this.title = articleDTO.getTitle();
        this.isActive = articleDTO.getIsActive();
        this.shortText = getFirstSentence(articleDTO.getText());
        this.category = articleDTO.getCategory();
        this.caption = articleDTO.getCaption();
        this.createDateTime = articleDTO.getCreateDateTime();
        this.picture = articleDTO.getPicture();
    }
}
