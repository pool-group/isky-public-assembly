package com.assembly.common.exception;

import com.assembly.common.enums.ErrorCodeEnum;
import com.assembly.common.model.ExcMessageModel;
import lombok.Data;

/**
 * 业务异常
 *
 * @author ken.ny
 * @version Id: BizRuntimeException.java, v 0.1 2018年11月02日 下午15:30 ken.ny Exp $
 */
@Data
public class BizRuntimeException extends RuntimeException {

    private ExcMessageModel excMessageModel;

    /**自定义message内容*/
    private String definedMessage;

    public BizRuntimeException(String message) {
        super(message);
        this.excMessageModel=new ExcMessageModel();
        this.excMessageModel.setCode(ErrorCodeEnum.BIZ_DEFAULT_EXCP.getCode());
        this.excMessageModel.setCodeLevel(ErrorCodeEnum.BIZ_DEFAULT_EXCP.getCodeLevel());
        this.excMessageModel.setCodeType(ErrorCodeEnum.BIZ_DEFAULT_EXCP.getCodeType());
        this.excMessageModel.setMessage(ErrorCodeEnum.BIZ_DEFAULT_EXCP.getMessage());
        this.excMessageModel.setDesc(ErrorCodeEnum.BIZ_DEFAULT_EXCP.getDesc());
    }

    public BizRuntimeException(ExcMessageModel excMessageModel) {
        this.excMessageModel = excMessageModel;
    }

    public BizRuntimeException(String message, Throwable cause, ExcMessageModel excMessageModel) {
        super(message, cause);
        this.excMessageModel = excMessageModel;
    }

    public BizRuntimeException(ExcMessageModel excMessageModel,String definedMessage) {
        this.excMessageModel = excMessageModel;
        this.definedMessage = definedMessage;
    }

    public BizRuntimeException(String message, ExcMessageModel excMessageModel) {
        super(message);
        this.excMessageModel = excMessageModel;
    }

    public BizRuntimeException(String message, Throwable cause) {
        super(message,cause);
    }

    public String getDefinedMessage() {
        if(null==this.definedMessage){
            this.definedMessage="";
        }
        return definedMessage;
    }
}
