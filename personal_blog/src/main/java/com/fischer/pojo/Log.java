package com.fischer.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    private String name;
    private String time;
    private String user;
    private List<String> stackTrace;
    private Integer status;
    private String message;

}
