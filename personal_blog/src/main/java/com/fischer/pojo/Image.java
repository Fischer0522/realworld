package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@TableName("images")
@NoArgsConstructor
public class Image {
    private String imageId;
    private String articleId;
    private String imagePos;

    public Image(String articleId,String imagePos){
        this.imagePos=imagePos;
        this.articleId=articleId;
        this.imageId= UUID.randomUUID().toString();
    }
}
