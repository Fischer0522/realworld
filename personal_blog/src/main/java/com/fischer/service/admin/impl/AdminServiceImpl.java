package com.fischer.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.dao.AdminDao;
import com.fischer.pojo.Admin;
import com.fischer.service.admin.AdminService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDao adminDao;

    @Override
    public Admin findByusername(String adminName) {
        LambdaQueryWrapper<Admin> lqw =new LambdaQueryWrapper<>();
        lqw.eq(Strings.isNotEmpty(adminName),Admin::getAdminName,adminName);
        Admin admin = adminDao.selectOne(lqw);
        return admin;

    }
}
