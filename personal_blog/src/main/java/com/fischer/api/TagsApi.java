package com.fischer.api;

import com.fischer.assistant.ResultType;
import com.fischer.service.TagsQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(path = "tags")
public class TagsApi {
    private TagsQueryService tagsQueryService;
    @Autowired
    public TagsApi(TagsQueryService tagsQueryService){
        this.tagsQueryService=tagsQueryService;
    }

    @GetMapping
    public ResponseEntity getTags(){
        List<String> strings = tagsQueryService.allTags();
        /*return ResponseEntity.ok(
                new HashMap<String,Object>(){
                    {
                        put("tags",strings);
                    }
                }
        );*/
        return ResponseEntity.ok(new ResultType(200,strings,"ok"));
    }
}
