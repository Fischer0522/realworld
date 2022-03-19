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
    private String articleSlug;
    private String imagePos;

    public Image(String articleSlug,String imagePos){
        this.imagePos=imagePos;
        this.articleSlug=articleSlug;
        this.imageId= UUID.randomUUID().toString();
    }
}
