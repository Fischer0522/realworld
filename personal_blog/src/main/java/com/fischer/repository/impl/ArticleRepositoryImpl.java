package com.fischer.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fischer.api.exception.BizException;
import com.fischer.assistant.MyPage;
import com.fischer.dao.ArticleDao;
import com.fischer.dao.ArticleTagRelationDao;
import com.fischer.dao.TagDao;
import com.fischer.pojo.Article;
import com.fischer.pojo.ArticleTagRelation;
import com.fischer.pojo.Image;
import com.fischer.pojo.Tag;
import com.fischer.repository.ArticleRepository;
import com.fischer.repository.ImageRepository;
import org.apache.http.HttpHost;
import org.apache.logging.log4j.util.Strings;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Repository
public class ArticleRepositoryImpl implements ArticleRepository {
    private ArticleDao articleDao;
    private TagDao tagDao;
    private ArticleTagRelationDao articleTagRelationDao;
    private ImageRepository imageRepository;
    private String esUri;
    private RestHighLevelClient client;
    @Autowired
    public ArticleRepositoryImpl (
            ArticleDao articleDao,
            TagDao tagDao,
            ArticleTagRelationDao articleTagRelationDao,
            ImageRepository imageRepository,
            @Value("${es.host}") String esUri) {
        this.articleDao=articleDao;
        this.articleTagRelationDao=articleTagRelationDao;
        this.tagDao=tagDao;
        this.imageRepository=imageRepository;
        this.esUri=esUri;
    }
    @Override
    public void save(Article article) {
        esInit();
        ObjectMapper objectMapper=new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(article);
        } catch (JsonProcessingException e) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR,"es序列化失败");
        }

        if(!findById(article.getId()).isPresent())
        {
            createNew(article);

                IndexRequest request=new IndexRequest("articles").id(article.getId());
                request.source(json, XContentType.JSON);
                try {
                    client.index(request,RequestOptions.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR,"es出现异常");
                }

        }
        else
        {
            articleDao.updateById(article);
            UpdateRequest request=new UpdateRequest("articles",article.getId());
            request.doc(json,XContentType.JSON);
            try {
                UpdateResponse update = client.update(request, RequestOptions.DEFAULT);
            } catch (IOException e) {
                throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR,"es出现异常");
            }

        }
        try {
            client.close();
        } catch (IOException e) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR,"es客户端关闭失败");
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
        /*if(article.getImages()!=null){
            for(Image image:article.getImages()){
                imageDao.insert(image);
            }
        }*/
        if(!article.getImages().isEmpty()){
            imageRepository.addImage(article.getImages());
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
        List<Image> images = imageRepository.findByArticleId(id);
        //String slug = article.getSlug();
        /*LambdaQueryWrapper<Image> lqw=new LambdaQueryWrapper();
        lqw.eq(Strings.isNotEmpty(slug),Image::getArticleSlug,slug);
        List<Image> images = imageDao.selectList(lqw);*/
        //List<Image> images = imageRepository.findByArticleSlug(slug);
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
        /*LambdaQueryWrapper<Image> lqw=new LambdaQueryWrapper();
        lqw.eq(Strings.isNotEmpty(slug),Image::getArticleSlug,slug);
        List<Image> images = imageDao.selectList(lqw);*/
        List<Image> images = imageRepository.findByArticleId(article.getId());
        //List<Image> images = imageRepository.findByArticleSlug(slug);
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
            String id=article.getId();
            //String slug = article.getSlug();
            List<Image> images = imageRepository.findByArticleId(id);
            //List<Image> images = imageRepository.findByArticleSlug(slug);
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
            /*String slug = article.getSlug();
            LambdaQueryWrapper<Image>lqw=new LambdaQueryWrapper<>();
            lqw.eq(Image::getArticleSlug,slug);
            imageDao.delete(lqw);*/
            imageRepository.removeByArticleId(article.getId());
        }
        esInit();

        DeleteRequest request=new DeleteRequest("articles",article.getId());
        try {
            client.delete(request,RequestOptions.DEFAULT);
            client.close();
        } catch (IOException e) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR,"es删除索引失败");
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
    public List<Article> getPage(String value,MyPage myPage) {

//        LambdaQueryWrapper<Article> lqw=new LambdaQueryWrapper<>();
//        lqw.like(Strings.isNotEmpty(article.getTitle()),Article::getTitle,article.getTitle());
//        lqw.like(Strings.isNotEmpty(article.getDescription()),Article::getDescription,article.getDescription());
//        IPage<Article> p=new Page(myPage.getOffset(),myPage.getLimit());
//         articleDao.selectPage(p, lqw);
//        for (Article record : p.getRecords()) {
//
//            ArticleTagRelation articleTagRelation=new ArticleTagRelation();
//            articleTagRelation.setArticleId(record.getId());
//            QueryWrapper<ArticleTagRelation> wrapper=new QueryWrapper<>(articleTagRelation);
//            List<String> tagIds=new LinkedList<>();
//            for (ArticleTagRelation tagRelation : articleTagRelationDao.selectList(wrapper))
//            {
//                tagIds.add(tagRelation.getTagId());
//            }
//            if(!tagIds.isEmpty())
//            {
//                List<Tag> tags=tagDao.selectBatchIds(tagIds);
//                record.setTags(tags);
//            }
//            String id = record.getId();
//            /*LambdaQueryWrapper<Image> lqwImage=new LambdaQueryWrapper<>();
//            lqwImage.eq(Image::getArticleSlug,slug);
//            List<Image> images = imageDao.selectList(lqwImage);*/
//            List<Image> images = imageRepository.findByArticleId(id);
//            //List<Image> images = imageRepository.findByArticleSlug(slug);
//            if(!images.isEmpty()){
//                record.setImages(images);
//            }
//            else{
//                record.setImages(new LinkedList<>());
//            }
//
//        }
//
//        return p;
        esInit();
        SearchRequest request = new SearchRequest("articles");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.termQuery("all", value));
        builder.size(myPage.getLimit());
        builder.from(myPage.getOffset());
        request.source(builder);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Article> articles = new LinkedList<>();
        try {
            SearchResponse search = client.search(request, RequestOptions.DEFAULT);
            for (SearchHit hit : search.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                Article article = objectMapper.readValue(sourceAsString, Article.class);
                articles.add(article);
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Article record : articles) {

            ArticleTagRelation articleTagRelation = new ArticleTagRelation();
            articleTagRelation.setArticleId(record.getId());
            QueryWrapper<ArticleTagRelation> wrapper = new QueryWrapper<>(articleTagRelation);
            List<String> tagIds = new LinkedList<>();
            for (ArticleTagRelation tagRelation : articleTagRelationDao.selectList(wrapper)) {
                tagIds.add(tagRelation.getTagId());
            }
            if (!tagIds.isEmpty()) {
                List<Tag> tags = tagDao.selectBatchIds(tagIds);
                record.setTags(tags);
            }
            String id = record.getId();
            /*LambdaQueryWrapper<Image> lqwImage=new LambdaQueryWrapper<>();
            lqwImage.eq(Image::getArticleSlug,slug);
            List<Image> images = imageDao.selectList(lqwImage);*/
            List<Image> images = imageRepository.findByArticleId(id);
            //List<Image> images = imageRepository.findByArticleSlug(slug);
            if (!images.isEmpty()) {
                record.setImages(images);
            } else {
                record.setImages(new LinkedList<>());
            }


        }
        return articles;
    }

    void esInit(){
        HttpHost httpHost=HttpHost.create(esUri);
        RestClientBuilder builder= RestClient.builder(httpHost);
         client = new RestHighLevelClient(builder);
    }


}
