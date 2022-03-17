package com.fischer.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.dao.ImageDao;
import com.fischer.pojo.Image;
import com.fischer.service.article.ImageService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageDao imageDao;
    @Override
    public List<Image> findByArticleId(String articleId) {
        LambdaQueryWrapper<Image> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(articleId),Image::getArticleId,articleId);
        List<Image> images = imageDao.selectList(lqw);
        if(images!=null){
            return images;
        }
        else{
            return new LinkedList<>();
        }

    }

    @Override
    public Image findById(String id) {
        return imageDao.selectById(id);
    }

    @Override
    public boolean addImage(String articleId,String inagePos) {
        Image image=new Image(articleId,inagePos);
        int insert = imageDao.insert(image);
        return insert>0;

    }

    @Override
    public boolean removeImage(String id) {
        int i = imageDao.deleteById(id);
        return i>0;
    }
}
