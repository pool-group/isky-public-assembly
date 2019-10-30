package com.assembly.template.engine.template;


import com.assembly.common.enums.ErrorCodeEnum;
import com.assembly.common.exception.BizRuntimeException;
import com.assembly.common.exception.SysRuntimeException;
import com.assembly.common.model.ExcMessageModel;
import com.assembly.common.util.LogUtil;
import com.assembly.template.engine.context.EngineContext;
import com.assembly.template.engine.context.ErrorContext;
import com.assembly.template.engine.result.IBaseResult;
import com.assembly.template.engine.result.ResultCode;

/**
 * 模板基类
 *
 * @author ken.ny
 * @version Id: BaseTemplate.java, v 0.1 2018年11月02日 下午18:01 ken.ny Exp $
 */
public abstract class BaseTemplate {

    /**
     * 填充错误堆栈
     *
     * @param result
     * @param ece
     */
    public void fillResultWithErrorCode(IBaseResult result, ExcMessageModel ece, String msg) {

        ErrorContext errorContext=new ErrorContext(msg);
        errorContext.addError(new ResultCode(ece.getCodeLevel(),ece.getCodeType(),ece.getCode(),ece.getMessage(),ece.getDesc()));
        result.setSuccess(false);
        result.setErrorContext(errorContext);
    }

    /**
     * 填充未知异常堆栈
     *
     * @param result
     * @param ece
     */
    public void fillErrorResultWithErrorCode(IBaseResult result, ErrorCodeEnum ece, String msg) {

        ErrorContext errorContext=new ErrorContext(msg);
        errorContext.addError(new ResultCode(ece.getCodeLevel(),ece.getCodeType(),ece.getCode(),ece.getMessage(),ece.getDesc()));
        result.setSuccess(false);
        result.setErrorContext(errorContext);
    }

    /**
     * 业务异常日志
     *
     * @param ex
     */
    public void fillBizExceptionLogModel(BizRuntimeException ex){
        LogUtil.warn(String.format(
                " BizOpCenterServiceTemplate execute execute biz exception[ codeLevel=%s, codeType=%s, errorCode=%s, errorMsg=%s, desc=%s, definedMessage=%s ]",
                ex.getExcMessageModel().getCodeLevel(), ex.getExcMessageModel().getCodeType(),ex.getExcMessageModel().getCode(),ex.getExcMessageModel().getMessage(),ex.getExcMessageModel().getDesc(),ex.getDefinedMessage()));
    }


    /**
     * 系统异常日志
     *
     * @param se
     */
    public void fillSysExceptionLogModel(SysRuntimeException se){
        LogUtil.error(se,String.format(
                " BizOpCenterServiceTemplate execute execute system exception[ codeLevel=%s, codeType=%s, errorCode=%s, errorMsg=%s, desc=%s ]",
                se.getExcMessageModel().getCodeLevel(), se.getExcMessageModel().getCodeType(),se.getExcMessageModel().getCode(),se.getExcMessageModel().getMessage(),se.getExcMessageModel().getDesc()),se.getDefinedMessage());
    }


    /**
     * 未知异常日志
     *
     * @param e
     * @param s
     */
    public void fillUnknownExceptionLogModel(Throwable e, StackTraceElement s){
        LogUtil.error(e, " BizOpCenterServiceTemplate Unknown Exception.  >>>>>>>>  ",String.format(
                "errorMsg = [ FileName=%s, ClassName=%s, MethodName=%s, LineNumber=%s, Message=%s ]   >>>>>>>>  ",s.getFileName(), s.getClassName(),s.getMethodName(),s.getLineNumber(),e.getMessage()));
    }


    /**
     * 摘要日志
     *
     * @param result
     * @param context
     */
    public void fillSummaryLogModel(IBaseResult result, EngineContext context){
        LogUtil.info(String.format(" Output ...Result [ success=%s ,resultObj=%s ]",result.isSuccess(),result.getResultObj()));
    }
}
