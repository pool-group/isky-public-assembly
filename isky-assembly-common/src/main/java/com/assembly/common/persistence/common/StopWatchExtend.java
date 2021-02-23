package com.assembly.common.persistence.common;

import lombok.Data;
import org.springframework.util.StopWatch;

/**
 * @ClassName: StopWatchExtend
 * @Description: 计时器扩展
 * @author: k.y
 * @date: 2021年02月21日 3:44 下午
 */
@Data
public class StopWatchExtend extends StopWatch {

    private String id;
}
