package com.key.utils;

import com.github.pagehelper.Page;
import com.key.vo.resp.PageRespVO;
import com.key.vo.resp.PageVO;

import java.util.List;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-09-21 15:09
 */
public class PageUtil {
    private PageUtil(){}
    public static <T> PageRespVO<T> getPageVO(List<T> list){
        PageRespVO<T> result=new PageRespVO<>();
        if(list instanceof Page){
            Page<T> page= (Page<T>) list;
            result.setTotalRows(page.getTotal());
            result.setTotalPages(page.getPages());
            result.setPageNum(page.getPageNum());
            result.setCurPageSize(page.getPageSize());
            result.setPageSize(page.size());
            result.setList(page.getResult());
        }
        return result;
    }


    public static <T> PageVO<T> getPageVOLog(List<T> list){
        PageVO<T> result=new PageVO<>();
        if(list instanceof Page){
            Page<T> page= (Page<T>) list;
            result.setTotalRows(page.getTotal());
            result.setTotalPages(page.getPages());
            result.setPageNum(page.getPageNum());
            result.setCurPageSize(page.getPageSize());
            result.setPageSize(page.size());
            result.setList(page.getResult());
        }
        return result;
    }
}
