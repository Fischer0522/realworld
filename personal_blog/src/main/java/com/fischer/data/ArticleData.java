package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fischer.pojo.Article;
import com.fischer.pojo.Tag;
import com.fischer.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.List;
import java.util.stream.Collectors;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@Data
@NoArgsConstructor
public class ArticleData {

    private String id;
    private String slug;
    private String title;
    private String description;
    private String body;
    private boolean favorited;
    private int favoritesCount;
    private String createdAt;
    private String updatedAt;
    private List<String> tagList;
    @JsonProperty(value = "author")
    private ProfileData profileData;

    public ArticleData(Article article, User user)
    {
        this.id=article.getId();
        this.slug=article.getSlug();
        this.title=article.getTitle();
        this.description=article.getDescription();
        this.body=article.getBody();
        this.createdAt=article.getCreatedAt();
        this.updatedAt=article.getUpdatedAt();
        if(article.getTags()!=null) {
            this.tagList = article.getTags().stream().map(Tag::getName).collect(Collectors.toList());
        }
        this.profileData=new ProfileData(user);

    }



}
