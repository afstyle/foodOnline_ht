package com.hh.service.impl.center;

import com.github.pagehelper.PageInfo;
import com.hh.utils.PagedGridResult;

import java.util.List;

/**
 * @author HuangHao
 * @date 2021/2/5 13:55
 */
public class BaseService {

    public PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
