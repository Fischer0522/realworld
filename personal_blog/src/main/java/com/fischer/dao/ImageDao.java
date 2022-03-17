package com.fischer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fischer.pojo.Image;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageDao extends BaseMapper<Image> {
}
