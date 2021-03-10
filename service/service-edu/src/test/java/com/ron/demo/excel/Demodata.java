package com.ron.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Demodata {//头属性信息
    @ExcelProperty(value = "学生编号",index = 0)//对应第一列
    private Integer sno;//学生编号
    @ExcelProperty(value ="学生姓名", index = 1)//第二列
    private String sname;//学生姓名

}
