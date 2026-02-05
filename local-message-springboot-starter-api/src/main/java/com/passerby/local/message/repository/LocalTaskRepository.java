package com.passerby.local.message.repository;

import com.passerby.local.message.po.TaskMessageEntityCommand;

/**
 * @Author: sunjunyao
 * @Date: 2026/1/13 10:11
 * @Descrpition:
 */
public interface LocalTaskRepository {
    void save(TaskMessageEntityCommand taskMessageEntityCommand) throws Exception ;


    int updateStatusByTaskId(String taskId,Integer status);
}
