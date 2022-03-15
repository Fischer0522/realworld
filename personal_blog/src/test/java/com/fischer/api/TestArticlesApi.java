package com.fischer.api;

import com.fischer.assistant.AuthorizationService;
import com.fischer.dao.AdminDao;
import com.fischer.dao.ArticleDao;
import com.fischer.dao.UserDao;
import com.fischer.pojo.Admin;
import com.fischer.pojo.Article;
import com.fischer.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestArticlesApi {
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ArticleDao articleDao;

    @Test
    void test(@Autowired MockMvc mvc)throws Exception {
        RequestBuilder builder= MockMvcRequestBuilders.get("/articles");
        mvc.perform(builder);
    }
    @Test
    void test1(){
        int[] nums={1,2,3,4};
        Integer[] n=new Integer[nums.length];
        for(int i=0;i<nums.length;i++){
            n[i]=(Integer) nums[i];
        }
        System.out.println(n);
    }
    @Test
    void test2(){
        User user = userDao.selectById("3744");
        User user1 = userDao.selectById("1234");
        Admin admin = adminDao.selectById("1234");
        if(admin!=null){
            System.out.println(admin);
            System.out.println("1234是管理员");
        }
        Admin admin1 = adminDao.selectById("3744");
        if(admin1!=null){
            System.out.println(admin1);
            System.out.println("3744是管理员");
        }
        else{
            System.out.println("3744不是管理员");
        }
        if(authorizationService.canWriterArticle(user1,null)){
            System.out.println("user1可以");
        }
        Article article = articleDao.selectById("b51208da-88be-49df-b720-2afedac51861");
        if(!authorizationService.canWriterArticle(user,article)){
            System.out.println("3744bu可以");
        }


    }
}
