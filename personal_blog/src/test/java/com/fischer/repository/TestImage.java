package com.fischer.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.dao.ImageDao;
import com.fischer.pojo.Image;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestImage {
    @Autowired
    ImageDao imageDao;
    @Autowired
    ImageRepository repository;
    @Test
    void testDao(){
        String id="3c97adf7-48e9-4810-b50c-0696b5f933a6";
        LambdaQueryWrapper<Image> lqw=new LambdaQueryWrapper();
        lqw.eq(true,Image::getArticleId,id);
        List<Image> images = imageDao.selectList(lqw);
        System.out.println(images);
    }
    @Test
    void testReposity(){
        List<Image> byArticleId = repository.findByArticleId("69027adc-888a-4aa4-8bfe-fc5f6510ed92");
        System.out.println(byArticleId);
    }



}
