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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.fischer.pojo.Tag;
import static java.util.stream.Collectors.toList;
@Data
@TableName(value = "Articles")
@NoArgsConstructor
public class Article {
    private String id;
    private String userId;
    private String title;
    private String slug;
    private String description;
    private String body;
    @TableField(exist = false)
    private List<Tag> tags;
    @TableField(value = "created_at")
    private String createdAt;
    @TableField(value = "updated_at")
    private String updatedAt;
    //日后补充tagList
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
        this.tags = new HashSet<>(tagList).stream().map(Tag::new).collect(toList());
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
    public static String toSlug(String title)  {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String s = null;
        try {
            s = PinyinHelper.toHanYuPinyinString(title, format, "-", true);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return s;
    }

}
