package com.assembly.common.model;

import lombok.Data;
import lombok.ToString;

/**
 * 异常消息模型
 *
 * @author ken.ny
 * @version Id: ExcMessageModel.java, v 0.1 2019年01月28日 下午15:18 ken.ny Exp $
 */
@Data
@ToString
public class ExcMessageModel {

    /**错误码级别*/
    private String codeLevel;
    /**错误码类型*/
    private String codeType;
    /**错误码*/
    private String code;
    /**消息内容 EN */
    private String message;
    /**消息内容 CHS */
    private String desc;
}
