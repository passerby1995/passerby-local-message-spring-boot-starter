package com.passerby.local.message.service.impl;

import com.passerby.local.message.event.LocalMessageEvent;
import com.passerby.local.message.po.TaskMessageEntityCommand;
import com.passerby.local.message.repository.LocalTaskRepository;
import com.passerby.local.message.service.IHandleService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @Author: sunjunyao
 * @Date: 2026/2/2 16:36
 * @Descrpition:
 */
public class HandleService implements IHandleService {

    private final LocalTaskRepository localTaskRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    public HandleService(LocalTaskRepository localTaskRepository,ApplicationEventPublisher applicationEventPublisher) {
        this.localTaskRepository = localTaskRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Object handle(TaskMessageEntityCommand taskMessageEntityCommand) {
        try {
            //1.落库
            localTaskRepository.save(taskMessageEntityCommand);
            LocalMessageEvent localMessageEvent =  new LocalMessageEvent(this,taskMessageEntityCommand);
            //2.发送event
            applicationEventPublisher.publishEvent(localMessageEvent);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
