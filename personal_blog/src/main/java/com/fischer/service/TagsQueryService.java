package com.fischer.service;

import com.fischer.dao.TagDao;
import com.fischer.pojo.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagsQueryService {
    @Autowired
    private TagDao tagDao;
    public List<String> allTags(){
        List<Tag> tags = tagDao.selectList(null);
        return tags.stream().map(Tag::getName).collect(Collectors.toList());
    }

}
