package com.assembly.template.engine.result;

import lombok.Data;

/**
 * 结果集
 *
 * @author ken.ny
 * @version Id: RobotBaseResult.java, v 0.1 2018年11月02日 下午15:13 ken.ny Exp $
 */
@Data
public class IBaseResult<R> extends BaseResult {

    private R resultObj;

}
