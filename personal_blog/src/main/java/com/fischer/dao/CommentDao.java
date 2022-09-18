package com.fischer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fischer.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentDao extends BaseMapper<Comment> {
}
