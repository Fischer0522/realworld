package com.fischer.jwt;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.dao.ArticleDao;
import com.fischer.pojo.Article;
import com.fischer.repository.ArticleRepository;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;
import java.util.Optional;

@SpringBootTest
public class Pinyin {
    @Autowired
    private ArticleDao articleDao;
    @Test
    void testPinYin()throws Exception{




    }

}
