package com.ron.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class ExcelListener extends AnalysisEventListener<Demodata> {
    @Override//每行读取excel内容
    public void invoke(Demodata demodata, AnalysisContext analysisContext) {

        System.out.println("**************"+demodata);
    }

    //读取表头内容
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头"+headMap);
    }

    @Override//读取完成后
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
