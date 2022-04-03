package com.fischer.repository;

import com.fischer.pojo.Image;

import java.util.List;

public interface ImageRepository {

    List<Image> findByArticleId(String articleId);

    Image findById(String id);

    boolean addImage(List<Image> images);


    boolean removeByArticleId(String articleId);
}
