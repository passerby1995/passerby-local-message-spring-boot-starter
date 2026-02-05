package com.passerby.local.message.service;

import com.passerby.local.message.po.TaskMessageEntityCommand;

/**
 * @Author: sunjunyao
 * @Date: 2026/2/2 16:36
 * @Descrpition:
 */
public interface IHandleService {
    Object handle(TaskMessageEntityCommand taskMessageEntityCommand);
}
