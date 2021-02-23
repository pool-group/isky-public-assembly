package com.assembly.common.persistence.page;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: PageInfo
 * @Description: 分页结果集
 * @author: k.y
 * @date: 2021年02月23日 2:24 下午
 */
@Data
public class PageResult<T> {

    /**
     * 总数
     */
    private int total;
    /**
     * 结果集
     */
    private List<T> data;

    public PageResult(int total, List<T> data) {
        this.total=total;
        this.data=data;
    }
}
