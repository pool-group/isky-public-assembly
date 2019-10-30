package com.assembly.template.engine.callback;

import com.assembly.template.engine.context.EngineContext;

/**
 * 系统中枢回调
 *
 * @author ken.ny
 * @version Id: AbstractOpCallback.java, v 0.1 2018年11月02日 下午14:43 ken.ny Exp $
 */
public abstract class AbstractOpCallback<P, R> {

    /**
     * 参数合法性校验[前置处理]
     */
    public void preCheck(){}

    /**
     * 初始化参数及上下文
     * @return 上下文
     */
    public void initContent(EngineContext<P, R> context) {}

    /**
     * 业务处理核心逻辑
     *
     * @param context 上下文
     */
    public abstract void doProcess(EngineContext<P, R> context);

    /**
     * 业务[后置处理]
     *
     * @param context 上下文
     */
    public void afterProcess(EngineContext<P, R> context){}


}
