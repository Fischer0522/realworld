package com.fischer.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.api.exception.BizException;
import com.fischer.dao.ImageDao;
import com.fischer.pojo.Image;
import com.fischer.repository.ImageRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;

@Repository
public class ImageRepositoryImpl implements ImageRepository {
    @Autowired
    private ImageDao imageDao;
    @Value("${web.realpath}")
    private String rootpath;

    @Override
    public List<Image> findByArticleId(String articleId) {
        LambdaQueryWrapper<Image> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(articleId),Image::getArticleId,articleId);
        return imageDao.selectList(lqw);
    }

    @Override
    public Image findById(String id) {
        return imageDao.selectById(id);
    }

    @Override
    public boolean addImage(List<Image> images) {
        int count=0;
        if(!images.isEmpty()){
            for(Image image:images){

                int insert = imageDao.insert(image);
                count+=insert;
            }
        }
        return count>0;

    }


    private boolean removeImage(String path) {
        int pos=0;
        for(int i=0;i<path.length()-3;i++){

            String substring = path.substring(i, i + 3);
            if (substring.equals("img")){
                System.out.println(substring);
                System.out.println(i);
                pos=i;
            }

        }
        String subpath=path.substring(pos);
        String realpath=rootpath+subpath;
        File dest=new File(realpath);
        if(dest.exists()){
            dest.delete();
            return true;
        }else {
            return false;
        }

    }


    @Override
    public boolean removeByArticleId(String articleId) {
        LambdaQueryWrapper<Image> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(articleId),Image::getArticleId,articleId);
        for (Image image : imageDao.selectList(lqw)) {
            String imagePos = image.getImagePos();
            removeImage(imagePos);
        }

        int delete = imageDao.delete(lqw);

        return delete>0;

    }

    @Override
    public boolean removeByUserId(String path) {
        boolean b = removeImage(path);
        return b;
    }
}
