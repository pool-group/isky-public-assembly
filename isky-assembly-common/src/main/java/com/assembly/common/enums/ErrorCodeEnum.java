package com.assembly.common.enums;


import com.assembly.common.constants.ResultCodeLevel;
import com.assembly.common.constants.ResultCodeType;
import com.assembly.common.model.ExcMessageModel;

/**
 * 异常枚举定义
 *
 * @author ken.ny
 * @version Id: ErrorCodeEnum.java, v 0.1 2018年11月02日 下午15:30 ken.ny Exp $
 */
public enum ErrorCodeEnum {


    BIZ_DEFAULT_EXCP(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR,"50001","Biz Service Exception.","业务异常,具体原因,请查看第三方错误堆栈信息"),
    TOKEN_INVALID(ResultCodeLevel.WARN, ResultCodeType.BIZ_ERROR, "50002", "TOKEN INVALID", "token异常,请求无效"),

    SYS_EXCP(ResultCodeLevel.SYS_EXCP, ResultCodeType.SYS_EXCP,"90001","BizOpCenterServiceTemplate Unknown Exception.","系统未知异常");



    private String codeLevel;
    private String codeType;
    private String code;
    private String message;
    private String desc;

    ErrorCodeEnum(String codeLevel, String codeType, String code, String message, String desc) {
        this.codeLevel=codeLevel;
        this.codeType=codeType;
        this.code=code;
        this.message=message;
        this.desc=desc;
    }

    public String getCodeLevel() {
        return codeLevel;
    }

    public void setCodeLevel(String codeLevel) {
        this.codeLevel = codeLevel;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ExcMessageModel form(){
        ExcMessageModel em=new ExcMessageModel();
        em.setCode(this.code);
        em.setCodeLevel(this.codeLevel);
        em.setCodeType(this.codeType);
        em.setDesc(this.desc);
        em.setMessage(this.getMessage());
        return em;
    }
}
