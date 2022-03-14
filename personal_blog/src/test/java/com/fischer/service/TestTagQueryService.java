package com.fischer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTagQueryService {
    @Autowired
    private TagsQueryService tagsQueryService;
    @Test
    public void testAll(){
        for (String tag : tagsQueryService.allTags()) {
            System.out.println(tag);
        }

    }

}
