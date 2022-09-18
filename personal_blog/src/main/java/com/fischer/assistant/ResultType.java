package com.fischer.assistant;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultType {
    private Integer code;
    private Object Data;
    private String msg;

}
