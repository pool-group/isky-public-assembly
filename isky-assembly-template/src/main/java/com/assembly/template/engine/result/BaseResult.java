package com.assembly.template.engine.result;

import com.assembly.template.engine.context.ErrorContext;
import lombok.Data;

/**
 * 返回结果基类
 *
 * @author ken.ny
 * @version Id: BaseResult.java, v 0.1 2018年11月02日 下午18:08 ken.ny Exp $
 */
@Data
public abstract class BaseResult extends BaseSerializable {

    /** 是否业务处理成功 */
    protected boolean         success;

    /** 错误上下文 */
    protected ErrorContext errorContext;

    /**
     * 默认构造函数。
     */
    public BaseResult() {
        success = false;
        errorContext = new ErrorContext();
    }

    /**
     * 构造函数。
     *
     * @param success       业务处理结果
     * @param errorContext  错误上下文
     */
    public BaseResult(boolean success, ErrorContext errorContext) {
        this.success = success;
        this.errorContext = errorContext;
    }

}
