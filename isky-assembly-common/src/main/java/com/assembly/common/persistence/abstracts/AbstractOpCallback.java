package com.assembly.common.persistence.abstracts;

/**
 * @ClassName: BaseCall
 * @Description: 通用回调处理
 * @author: k.y
 * @date: 2021年02月21日 9:22 下午
 */
public abstract class AbstractOpCallback<P, R> {

    /**
     * Call execute the process
     *
     * @param parameter
     * @return
     */
    public abstract R call(P parameter);

}
