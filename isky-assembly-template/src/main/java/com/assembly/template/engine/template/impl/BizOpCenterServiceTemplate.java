
package com.assembly.template.engine.template.impl;

import com.assembly.common.enums.ErrorCodeEnum;
import com.assembly.common.exception.BizRuntimeException;
import com.assembly.common.exception.SysRuntimeException;
import com.assembly.common.redis.redisson.DistributedLock;
import com.assembly.common.util.LogUtil;
import com.assembly.template.engine.callback.AbstractOpCallback;
import com.assembly.template.engine.context.EngineContext;
import com.assembly.template.engine.log.Log;
import com.assembly.template.engine.result.IBaseResult;
import com.assembly.template.engine.template.BaseOpCenterServiceTemplate;
import com.assembly.template.engine.template.BaseTemplate;
import com.google.common.base.Stopwatch;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 内部统一服务模板
 *
 * @author ken.ny
 * @version Id: BizOpCenterServiceTemplateImpl.java, v 0.1 2018年11月02日 下午14:59 ken.ny Exp $
 */
@Service
public class BizOpCenterServiceTemplate extends BaseTemplate implements BaseOpCenterServiceTemplate {

    private DistributedLock distributedLock;
    /**
     * @see BaseOpCenterServiceTemplate#doBizProcess(AbstractOpCallback)
     */
    @Override
    public <P, R> IBaseResult<R> doBizProcess(AbstractOpCallback<P, R> bizCallback) {
        IBaseResult<R> result = new IBaseResult<>();
        EngineContext<P, R> context = new EngineContext<>();
        Stopwatch watch = Stopwatch.createStarted();
        try {
            LogUtil.info(">>> biz service start..");

            //1.检查参数
            bizCallback.preCheck();

            //2.初始化参数及上下文
            bizCallback.initContent(context);

            //3.请求参数
            LogUtil.info(String.format(" Input ...Parameter [ %s ]  ",null==context.getInputModel()?null:context.getInputModel().toString()));

            //4.业务处理
            bizCallback.doProcess(context);

            //5.后置处理
            bizCallback.afterProcess(context);

            //6.设置结果，默认true
            result.setResultObj(context.getOutputModel());
            result.setSuccess(true);

        } catch (BizRuntimeException ex) {

            fillBizExceptionLogModel(ex);
            fillResultWithErrorCode(result, ex.getExcMessageModel(),ex.getMessage());

        } catch (SysRuntimeException se) {

            fillSysExceptionLogModel(se);
            fillResultWithErrorCode(result, se.getExcMessageModel(), se.getMessage());

        } catch (Throwable e) {

            StackTraceElement s= e.getStackTrace()[0];
            fillUnknownExceptionLogModel(e,s);
            fillErrorResultWithErrorCode(result, ErrorCodeEnum.SYS_EXCP, String.format(
                    "errorMsg = [ FileName=%s, ClassName=%s, MethodName=%s, LineNumber=%s, Message=%s ]   >>>>>>>>  ",s.getFileName(), s.getClassName(),s.getMethodName(),s.getLineNumber(),e.getMessage()));

        } finally {

            fillSummaryLogModel(result, context);

            //7.清理上下文

            LogUtil.info("<<< biz service end..");
        }
        Long tm=watch.elapsed(TimeUnit.MILLISECONDS);
        if(tm.compareTo(1500L)==1)
            LogUtil.warn(Log.MONITOR.LOG,String.format("BaseOpCenterServiceTemplate#doBizProcess：QuestParam=[ % ]  , Execution method time:[ %s ]ms",context.getInputModel(),tm));
        return result;
    }
}
