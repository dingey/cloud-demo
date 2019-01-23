package com.d.common.service;

import com.di.kit.IdWorker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

public interface IdService {
    Long nextId();

    @Service
    class IdServiceImpl implements IdService {
        private IdWorker idWorker;
        @Value("${worker.id:1}")
        private long workerId;
        @Value("${datacenter.id:1}")
        private long datacenterId;

        @PostConstruct
        public void init() {
            idWorker = new IdWorker(workerId, datacenterId);
        }

        @Override
        public Long nextId() {
            return idWorker.nextIdSync();
        }
    }
}
