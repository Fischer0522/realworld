package com.fischer.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fischer.api.exception.BizException;
import com.fischer.assistant.MyPage;
import com.fischer.assistant.TimeCursor;
import com.fischer.dao.ArticleDao;
import com.fischer.dao.ArticleTagRelationDao;
import com.fischer.dao.ImageDao;
import com.fischer.dao.TagDao;
import com.fischer.data.ArticleData;
import com.fischer.pojo.Article;
import com.fischer.pojo.ArticleTagRelation;
import com.fischer.pojo.Image;
import com.fischer.pojo.Tag;
import com.fischer.repository.ArticleRepository;
import org.apache.logging.log4j.util.Strings;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Repository
public class ArticleRepositoryImpl implements ArticleRepository {
    private ArticleDao articleDao;
    private TagDao tagDao;
    private ArticleTagRelationDao articleTagRelationDao;
    private ImageDao imageDao;
    private static  final Logger logger=LoggerFactory.getLogger(ArticleRepositoryImpl.class);
    @Autowired
    public ArticleRepositoryImpl (
            ArticleDao articleDao,
            TagDao tagDao,
            ArticleTagRelationDao articleTagRelationDao,
            ImageDao imageDao) {
        this.articleDao=articleDao;
        this.articleTagRelationDao=articleTagRelationDao;
        this.tagDao=tagDao;
        this.imageDao=imageDao;
    }
    @Override
    @Transactional
    public void save(Article article) {
        if(!findById(article.getId()).isPresent())
        {
            createNew(article);
        }
        else
        {
            articleDao.updateById(article);
        }

    }
    private void createNew(Article article) {
        if (article.getTags()!=null) {
            for (Tag tag : article.getTags()) {
                Tag targetTag = Optional.ofNullable(findTag(tag.getName())).orElseGet(() -> {
                    tagDao.insert(tag);
                    return tag;
                });
                ArticleTagRelation articleTagRelation = new ArticleTagRelation();
                articleTagRelation.setArticleId(article.getId());
                articleTagRelation.setTagId(targetTag.getId());
                articleTagRelationDao.insert(articleTagRelation);
            }
        }
        if(article.getImages()!=null){
            for(Image image:article.getImages()){
                imageDao.insert(image);
            }
        }
        articleDao.insert(article);
    }

    @Override
    public Optional<Article> findById(String id) {
        Article article = articleDao.selectById(id);
        if(article==null){
            return Optional.empty();
        }
        ArticleTagRelation articleTagRelation=new ArticleTagRelation();
        articleTagRelation.setArticleId(id);
        QueryWrapper<ArticleTagRelation> wrapper=new QueryWrapper<>(articleTagRelation);
        List<String> tagIds=new LinkedList<>();
        for (ArticleTagRelation tagRelation : articleTagRelationDao.selectList(wrapper)) {
            tagIds.add(tagRelation.getTagId());
        }
        if(!tagIds.isEmpty())
        {
            List<Tag> tags = tagDao.selectBatchIds(tagIds);
            article.setTags(tags);
        }
        String slug = article.getSlug();
        LambdaQueryWrapper<Image> lqw=new LambdaQueryWrapper();
        lqw.eq(Strings.isNotEmpty(slug),Image::getArticleSlug,slug);
        List<Image> images = imageDao.selectList(lqw);
        article.setImages(images);
        return Optional.of(article);
    }

    @Override
    public Optional<Article> findBySlug(String slug) {
        Article article=new Article();
        article.setSlug(slug);
        QueryWrapper<Article> wrapper=new QueryWrapper<>(article);
        Article article1=articleDao.selectOne(wrapper);
        if(article1==null){
            return Optional.empty();
        }
        ArticleTagRelation articleTagRelation=new ArticleTagRelation();
        articleTagRelation.setArticleId(article1.getId());
        QueryWrapper<ArticleTagRelation>wrapper1=new QueryWrapper<>(articleTagRelation);
        List<String> tagIds=new LinkedList<>();
        for (ArticleTagRelation tagRelation : articleTagRelationDao.selectList(wrapper1)) {
            tagIds.add(tagRelation.getTagId());
        }
        if(!tagIds.isEmpty())
        {
            List<Tag> tags = tagDao.selectBatchIds(tagIds);
            article1.setTags(tags);
        }
        LambdaQueryWrapper<Image> lqw=new LambdaQueryWrapper();
        lqw.eq(Strings.isNotEmpty(slug),Image::getArticleSlug,slug);
        List<Image> images = imageDao.selectList(lqw);
        article1.setImages(images);

        return Optional.of(article1);
    }


    @Override
    public Optional<List<Article>> findArticles(List<String> articleIds) {
        List<Article> articles = articleDao.selectBatchIds(articleIds);
        for (Article article:articles)
        {

            ArticleTagRelation articleTagRelation=new ArticleTagRelation();
            articleTagRelation.setArticleId(article.getId());
            QueryWrapper<ArticleTagRelation> wrapper=new QueryWrapper<>(articleTagRelation);
            List<String> tagIds=new LinkedList<>();
            for (ArticleTagRelation tagRelation : articleTagRelationDao.selectList(wrapper))
            {
                tagIds.add(tagRelation.getTagId());
            }
            if(!tagIds.isEmpty())
            {
                List<Tag> tags=tagDao.selectBatchIds(tagIds);
                article.setTags(tags);
            }
            String slug = article.getSlug();
            LambdaQueryWrapper<Image> lqw=new LambdaQueryWrapper();
            lqw.eq(Strings.isNotEmpty(slug),Image::getArticleSlug,slug);
            List<Image> images = imageDao.selectList(lqw);
            article.setImages(images);

        }
        return Optional.ofNullable(articles);
    }

    @Override
    public void remove(Article article) {
        articleDao.deleteById(article.getId());
        if(!article.getTags().isEmpty()) {
            List<String> collect = article.getTags().stream().map(Tag::getId).collect(toList());
            for (String tag:collect){

                LambdaQueryWrapper<ArticleTagRelation> lqw=new LambdaQueryWrapper<>();
                lqw.eq(ArticleTagRelation::getTagId,tag);
                Integer integer = articleTagRelationDao.selectCount(lqw);
                if(integer==1){
                   tagDao.deleteById(tag);
                }
            }
            //删除与该文章有关的所有关系链
            LambdaQueryWrapper<ArticleTagRelation> lqw=new LambdaQueryWrapper<>();
            lqw.eq(ArticleTagRelation::getArticleId,article.getId());
            articleTagRelationDao.delete(lqw);


            /*ArticleTagRelation a=new ArticleTagRelation();
            a.setArticleId(article.getId());
            QueryWrapper<ArticleTagRelation> wrapper=new QueryWrapper<>(a);
            articleTagRelationDao.delete(wrapper);*/



        }
        if(!article.getImages().isEmpty()){
            String slug = article.getSlug();
            LambdaQueryWrapper<Image>lqw=new LambdaQueryWrapper<>();
            lqw.eq(Image::getArticleSlug,slug);
            imageDao.delete(lqw);
        }


    }

    @Override
    public Tag findTag(String tagName) {
        LambdaQueryWrapper<Tag> lqw=new LambdaQueryWrapper();
        lqw.eq(Strings.isNotEmpty(tagName),Tag::getName,tagName);
        Tag tag = tagDao.selectOne(lqw);
        return tag;
    }

    @Override
    public IPage<Article> getPage(MyPage myPage, Article article) {
        LambdaQueryWrapper<Article> lqw=new LambdaQueryWrapper<>();
        lqw.like(Strings.isNotEmpty(article.getTitle()),Article::getTitle,article.getTitle());
        lqw.like(Strings.isNotEmpty(article.getDescription()),Article::getDescription,article.getDescription());
        IPage<Article> p=new Page(myPage.getOffset(),myPage.getLimit());
         articleDao.selectPage(p, lqw);
        for (Article record : p.getRecords()) {

            ArticleTagRelation articleTagRelation=new ArticleTagRelation();
            articleTagRelation.setArticleId(record.getId());
            QueryWrapper<ArticleTagRelation> wrapper=new QueryWrapper<>(articleTagRelation);
            List<String> tagIds=new LinkedList<>();
            for (ArticleTagRelation tagRelation : articleTagRelationDao.selectList(wrapper))
            {
                tagIds.add(tagRelation.getTagId());
            }
            if(!tagIds.isEmpty())
            {
                List<Tag> tags=tagDao.selectBatchIds(tagIds);
                record.setTags(tags);
            }
            String slug = record.getSlug();
            LambdaQueryWrapper<Image> lqwImage=new LambdaQueryWrapper<>();
            lqwImage.eq(Image::getArticleSlug,slug);
            List<Image> images = imageDao.selectList(lqwImage);
            if(!images.isEmpty()){
                record.setImages(images);
            }
            else{
                record.setImages(new LinkedList<>());
            }

        }

        return p;
    }


}
