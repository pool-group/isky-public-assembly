/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.assembly.template.engine.template;


import com.assembly.template.engine.callback.AbstractOpCallback;
import com.assembly.template.engine.result.IBaseResult;

/**
 * 内部统一服务模板
 *
 * @author ken.ny
 * @version Id: BizOpCenterServiceTemplate.java, v 0.1 2018年11月02日 下午14:59 ken.ny Exp $
 */
public interface BaseOpCenterServiceTemplate {

    /**
     * 服务模板方法
     * 
     * @param bizCallback 回调
     * @return 通用结果
     */
    <P, R> IBaseResult<R> doBizProcess(AbstractOpCallback<P, R> bizCallback);

}
