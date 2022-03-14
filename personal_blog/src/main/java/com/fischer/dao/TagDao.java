package com.fischer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fischer.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagDao extends BaseMapper<Tag> {
}
