package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@TableName("admin")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    private String id;
    private String adminName;
}
