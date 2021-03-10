package com.ron.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        //文件位置
        String filename="F:\\write.xlsx";
        //实现excel写操作
        //写入文件的位置,头属性的类,指定sheet,doWrite写入数据
        //EasyExcel.write(filename,Demodata.class).sheet("学生列表").doWrite(getdata());

        //实现读操
        EasyExcel.read(filename,Demodata.class,new ExcelListener()).sheet().doRead();

    }


    //添加数据
    private static List<Demodata> getdata(){
        List data=new ArrayList();
        for (int i = 0; i < 5; i++) {
            Demodata demodata = new Demodata();
            demodata.setSname("zhu"+i);
            demodata.setSno(i);
            data.add(demodata);
        }
        return data;
    }
}
