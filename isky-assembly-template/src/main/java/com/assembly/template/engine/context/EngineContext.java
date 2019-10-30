package com.assembly.template.engine.context;

import lombok.Data;
import org.slf4j.Logger;

/**
 * 业务上下文
 *
 * @author ken.ny
 * @version Id: EngineContext.java, v 0.1 2018年11月02日 下午14:59 ken.ny Exp $
 */
@Data
public class EngineContext<P, R> {

    private P inputModel;

    private R outputModel;

    private Logger log;
}
