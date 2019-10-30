package com.assembly.common.exception;

import com.assembly.common.model.ExcMessageModel;
import lombok.Data;

/**
 * 系统异常
 *
 * @author ken.ny
 * @version Id: SysRuntimeException.java, v 0.1 2018年11月02日 下午15:36 ken.ny Exp $
 */
@Data
public class SysRuntimeException extends RuntimeException {

    private ExcMessageModel excMessageModel;

    /**自定义message内容*/
    private String definedMessage;

    public SysRuntimeException(ExcMessageModel excMessageModel) {
        this.excMessageModel = excMessageModel;
    }

    public SysRuntimeException(String message, Throwable cause, ExcMessageModel excMessageModel) {
        super(message, cause);
        this.excMessageModel = excMessageModel;
    }
    public SysRuntimeException(Throwable cause, ExcMessageModel excMessageModel) {
        super(cause);
        this.excMessageModel = excMessageModel;
    }

    public SysRuntimeException(ExcMessageModel excMessageModel,String definedMessage) {
        this.excMessageModel = excMessageModel;
        this.definedMessage = definedMessage;
    }

    public SysRuntimeException(String message, ExcMessageModel excMessageModel) {
        super(message);
        this.excMessageModel = excMessageModel;
    }

    public SysRuntimeException(String message, Throwable cause) {
        super(message,cause);
    }

    public SysRuntimeException(String message) {
        super(message);
    }

    public String getDefinedMessage() {
        if(null==this.definedMessage){
            this.definedMessage="";
        }
        return definedMessage;
    }
}
