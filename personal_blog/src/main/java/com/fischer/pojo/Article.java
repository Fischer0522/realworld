package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fischer.assistant.TimeCursor;
import com.fischer.assistant.Util;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.joda.time.DateTime;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.fischer.pojo.Tag;
import static java.util.stream.Collectors.toList;
@Data
@TableName(value = "articles")
@NoArgsConstructor
public class Article {
    private String id;
    private String userId;
    private String title;
    private String slug;
    private String description;
    private String body;
    @TableField(exist = false)
    private List<Tag> tags=new LinkedList<>();
    @TableField(exist = false)
    private List<Image> images=new LinkedList<>();
    @TableField(value = "created_at")
    private String createdAt;
    @TableField(value = "updated_at")
    private String updatedAt;

    public Article(
            String title,
            String description,
            String body,
            List<String> tagList,
            List<String> imagesName,
            String userId
            )
    {   String id=UUID.randomUUID().toString();
        this.id= id;
        this.slug=toSlug(title);
        this.title=title;
        this.description=description;
        this.body=body;
        if(tagList!=null) {
            this.tags = new HashSet<>(tagList).stream().map(Tag::new).collect(toList());
        }
        if(imagesName!= null){
            for(String name:imagesName){
                this.images.add(new Image(id,name));
            }
        }
        this.createdAt= TimeCursor.toTime(DateTime.now());
        this.updatedAt=TimeCursor.toTime(DateTime.now());
        this.userId=userId;

    }

    public Article(
            String title,
            String description,
            String body,
            List<String> tagList,
            String userId
    )
    {
        this.id= UUID.randomUUID().toString();
        this.slug=toSlug(title);
        this.title=title;
        this.description=description;
        this.body=body;
        if(tagList!=null) {
            this.tags = new HashSet<>(tagList).stream().map(Tag::new).collect(toList());
        }
        this.createdAt= TimeCursor.toTime(DateTime.now());
        this.updatedAt=TimeCursor.toTime(DateTime.now());
        this.userId=userId;

    }
    public void update(String title,String description,String body){
        if(!Util.isEmpty(title)){
            this.title=title;
            this.slug=toSlug(title);
            this.updatedAt=TimeCursor.toTime(DateTime.now());
        }
        if(!Util.isEmpty(description)){
            this.description=description;
            this.updatedAt=TimeCursor.toTime(DateTime.now());
        }
        if(!Util.isEmpty(body)){
            this.body=body;
            this.updatedAt=TimeCursor.toTime(DateTime.now());
        }
    }
    public static String toSlug(String title) {

        return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+", "-");
    }

}
