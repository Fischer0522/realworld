package com.fischer.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fischer.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminDao extends BaseMapper<Admin> {

}
