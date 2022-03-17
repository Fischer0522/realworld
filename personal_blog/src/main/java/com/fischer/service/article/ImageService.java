package com.fischer.service.article;

import com.fischer.pojo.Image;

import java.util.List;

public interface ImageService {

    List<Image> findByArticleId(String articleId);

    Image findById(String id);

    boolean addImage(String articleId,String imagePos);

    boolean removeImage(String id);
}
