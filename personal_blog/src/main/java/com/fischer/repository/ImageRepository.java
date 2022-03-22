package com.fischer.repository;

import com.fischer.pojo.Image;

import java.util.List;

public interface ImageRepository {

    List<Image> findByArticleSlug(String articleSlug);

    Image findById(String id);

    boolean addImage(List<Image> images);


    boolean removeBySlug(String slug);
}
