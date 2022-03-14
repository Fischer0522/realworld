package com.fischer.api;

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
}
