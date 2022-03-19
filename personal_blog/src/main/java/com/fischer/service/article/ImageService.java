package com.fischer.service.article;

import com.fischer.pojo.Image;

import java.util.List;

public interface ImageService {

    List<Image> findByArticleSlug(String articleSlug);

    Image findById(String id);

    boolean addImage(String articleId,String imagePos);

    boolean removeImage(String id);
}
