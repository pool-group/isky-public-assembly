package com.assembly.template.engine.context;


import com.assembly.template.engine.result.ResultCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 错误上下文对象
 *
 * @author ken.ny
 * @version Id: ErrorContext.java, v 0.1 2018年11月02日 下午18:21 ken.ny Exp $
 */
public class ErrorContext implements java.io.Serializable {

    /** 序列ID */
    private static final long   serialVersionUID = 3939009139641299179L;

    /** 默认分隔符 */
    private static final String SPLIT            = "|";

    /** 错误堆栈集合 */
    private List<ResultCode> errorStack       = new ArrayList<ResultCode>();

    /** 第三方错误原始信息 */
    private String              thirdPartyError;

    /**
     * 默认构造方法。
     */
    public ErrorContext() {
        // 默认构造方法
    }

    /**
     * 构造方法。
     *
     * @param errorCode 错误码。
     */
    public ErrorContext(ResultCode errorCode) {
        this.addError(errorCode);
    }

    /**
     * 构造方法。
     *
     * @param thirdPartyError 第三方错误。
     */
    public ErrorContext(String thirdPartyError) {
        this.thirdPartyError = thirdPartyError;
    }

    /**
     * 获取当前错误对象。
     *
     * @return 当前错误码对象
     */
    public ResultCode fetchCurrentError() {
        if (errorStack != null && errorStack.size() > 0) {
            return errorStack.get(errorStack.size() - 1);
        }

        return null;
    }

    /**
     * 获取原始错误对象。
     *
     * @return 原始错误码对象
     */
    public ResultCode fetchRootError() {
        if (errorStack != null && errorStack.size() > 0) {
            return errorStack.get(0);
        }

        return null;
    }

    /**
     * 向堆栈中添加错误对象。
     *
     * @param error 错误码
     */
    public void addError(ResultCode error) {
        if (errorStack == null) {
            errorStack = new ArrayList<ResultCode>();
        }

        errorStack.add(error);
    }

    public List<ResultCode> getErrorStack() {
        return errorStack;
    }

    public void setErrorStack(List<ResultCode> errorStack) {
        this.errorStack = errorStack;
    }

    public String getThirdPartyError() {
        return thirdPartyError;
    }

    public void setThirdPartyError(String thirdPartyError) {
        this.thirdPartyError = thirdPartyError;
    }
}
