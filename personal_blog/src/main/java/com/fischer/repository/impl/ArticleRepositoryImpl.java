package com.fischer.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fischer.assistant.MyPage;
import com.fischer.assistant.TimeCursor;
import com.fischer.dao.ArticleDao;
import com.fischer.dao.ArticleTagRelationDao;
import com.fischer.dao.TagDao;
import com.fischer.data.ArticleData;
import com.fischer.pojo.Article;
import com.fischer.pojo.ArticleTagRelation;
import com.fischer.pojo.Tag;
import com.fischer.repository.ArticleRepository;
import org.apache.logging.log4j.util.Strings;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public ArticleRepositoryImpl (ArticleDao articleDao,TagDao tagDao,ArticleTagRelationDao articleTagRelationDao)
    {
        this.articleDao=articleDao;
        this.articleTagRelationDao=articleTagRelationDao;
        this.tagDao=tagDao;
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
            //article.setUpdatedAt(TimeCursor.toTime(DateTime.now()));
            articleDao.updateById(article);
        }

    }
    private void createNew(Article article) {
        for (Tag tag : article.getTags()) {
            Tag targetTag = Optional.ofNullable(findTag(tag.getName())).orElseGet(() -> {
                tagDao.insert(tag);
                return tag;
            });
            ArticleTagRelation articleTagRelation=new ArticleTagRelation();
            articleTagRelation.setArticleId(article.getId());
            articleTagRelation.setTagId(targetTag.getId());
            articleTagRelationDao.insert(articleTagRelation);
        }
        articleDao.insert(article);
    }

    @Override
    public Optional<Article> findById(String id) {
        Article article = articleDao.selectById(id);
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
        return Optional.ofNullable(article);
    }

    @Override
    public Optional<Article> findBySlug(String slug) {
        Article article=new Article();
        article.setSlug(slug);
        QueryWrapper<Article> wrapper=new QueryWrapper<>(article);
        Article article1=articleDao.selectOne(wrapper);
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

        return Optional.ofNullable(article1);
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
        }
        return Optional.ofNullable(articles);
    }

    @Override
    public void remove(Article article) {
        articleDao.deleteById(article.getId());

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
        System.out.println(myPage.getLimit());
        System.out.println(myPage.getOffset());
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
        }


        return p;
    }


}
