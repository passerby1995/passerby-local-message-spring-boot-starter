package com.passerby.local.message.strategy;

import com.passerby.local.message.po.TaskMessageEntityCommand;

/**
 * @Author: sunjunyao
 * @Date: 2026/2/2 17:33
 * @Descrpition:
 */
public interface IStrategy {
    Object apply(TaskMessageEntityCommand taskMessageEntityCommand);
}
