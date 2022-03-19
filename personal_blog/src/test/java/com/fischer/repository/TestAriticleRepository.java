package com.fischer.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fischer.assistant.MyPage;
import com.fischer.dao.ArticleDao;
import com.fischer.dao.ArticleTagRelationDao;
import com.fischer.dao.TagDao;
import com.fischer.data.NewArticleParam;
import com.fischer.data.UpdateArticleParam;
import com.fischer.pojo.Article;
import com.fischer.pojo.ArticleTagRelation;
import com.fischer.pojo.Tag;
import com.fischer.repository.ArticleRepository;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class TestAriticleRepository {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private ArticleTagRelationDao articleTagRelationDao;
    @Test
    public void testCreateArticle(){
        String userId="1234";
        DateTime dateTime=DateTime.now();
        NewArticleParam newArticleParam=new NewArticleParam();
        newArticleParam.setTitle("how to learn java");
        newArticleParam.setDescription("I don't know");
        newArticleParam.setBody("this article don't have body");
    }
    @Test
    public void testselectAll(){
        for (Article article : articleDao.selectList(null)) {
            System.out.println(article);
        }
    }
    @Test
    public void testFindById(){
        String id="e9fb9c05-ca78-4c52-83b7-f6043f352bf9";
        Optional<Article> byId = articleRepository.findById(id);
        System.out.println(byId.get());

    }
    @Test
    public void testFindBySlug() {
        String slug="how-to-learn-java";
        Optional<Article> bySlug = articleRepository.findBySlug(slug);
        Article article = bySlug.get();
        System.out.println(article);
    }

    @Test
    public void testUpdate(){
        String slug="how-to-learn-java";
        Optional<Article> bySlug = articleRepository.findBySlug(slug);
        Article article = bySlug.get();
        UpdateArticleParam updateArticleParam=new UpdateArticleParam(
                "how to learn java",
                "new article,new body",
                "I still don't know"
        );
        article.update(
                updateArticleParam.getTitle(),
                updateArticleParam.getDescription(),
                updateArticleParam.getBody());
        articleRepository.save(article);
    }
    @Test
    public void TestInsertTag(){
        String id= UUID.randomUUID().toString();
        String name="java";
        Tag tag=new Tag();
        tag.setId(id);
        tag.setName(name);
        tagDao.insert(tag);

    }

    @Test
    public void TestSelectTag(){
        List<Tag> tags = tagDao.selectList(null);
        System.out.println(tags);
    }
    @Test
    public void TestInsertRelation(){
        String articleId=UUID.randomUUID().toString();
        String tagId=UUID.randomUUID().toString();
        ArticleTagRelation articleTagRelation=new ArticleTagRelation();
        articleTagRelation.setArticleId(articleId);
        articleTagRelation.setTagId(tagId);
        articleTagRelationDao.insert(articleTagRelation);
        for (ArticleTagRelation tagRelation : articleTagRelationDao.selectList(null)) {
            System.out.println(tagRelation);
        }

    }
    @Test
    public void TestCreateNew(){
        List<String> tags=new LinkedList<>();
        tags.add("javaee");
        tags.add("noob");
        tags.add("git");
        String userId="1234";
        NewArticleParam newArticleParam=new NewArticleParam();
        newArticleParam.setTitle("how can I learn");
        newArticleParam.setDescription("I have no idea");
        newArticleParam.setBody("this article don't have body");
        Article article=new Article
                (newArticleParam.getTitle(),
                        newArticleParam.getDescription(),
                        newArticleParam.getBody(),
                        tags,
                        userId
                );
        articleRepository.save(article);
        for (Article article1 : articleDao.selectList(null)) {
            System.out.println(article1);
        }
        for (ArticleTagRelation articleTagRelation : articleTagRelationDao.selectList(null)) {
            System.out.println(articleTagRelation);
        }
        for (Tag tag : tagDao.selectList(null)) {
            System.out.println(tag);
        }
    }

    @Test
    public void testGetPage(){
        Article article=new Article();
        article.setUserId("0522");
        article.setDescription("this is a description");
        MyPage myPage=new MyPage(1,1);
        IPage<Article> page = articleRepository.getPage(myPage, article);
        for (Article record : page.getRecords()) {
            System.out.println(record);
        }
    }
    @Test
    public void testCountArticle(){
        String tag="noob";
        //Integer integer = countArticle(tag);
        //System.out.println(integer);
    }
    @Test
    public void testFindArticles(){
        List<String> articleIds=new LinkedList<>();
        articleIds.add("e30e99ae-5bdf-4a3f-90fa-49cd18692eac");
        articleIds.add("e9fb9c05-ca78-4c52-83b7-f6043f352bf9");
        articleIds.add("f0daa5e9-99ed-4eb8-929a-ab326207b397");

        Optional<List<Article>> articles = articleRepository.findArticles(articleIds);
        System.out.println(articles.get());

    }
    @Test
    void testDelete(){
        Article article = articleRepository.findById("6b7dae4c-21fb-408d-a7b9-ccea491efb29").get();
        articleRepository.remove(article);
    }

}
