package com.fischer.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URLDecoder;
import java.net.URLEncoder;

@SpringBootTest
public class UrlCode {
    @Test
    void test1()throws Exception{
        /*String s="call-of-duty";
        String encode = URLEncoder.encode(s, "UTF-8");
        String decode = URLDecoder.decode(encode, "UTF-8");
        System.out.println(encode);
        System.out.println(decode);*/
        String s="中国人不骗中国人";
        String s1 = s.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+", "-");
        System.out.println(s1);
    }
}
