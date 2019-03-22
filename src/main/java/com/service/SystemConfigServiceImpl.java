package com.service;

import com.model.SystemConfig;
import com.repository.SystemConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SystemConfigServiceImpl extends BaseService implements SystemConfigService {


    private static Logger logger = LoggerFactory.getLogger(SystemConfigServiceImpl.class);

    private static final Map<String, List<SystemConfig>> cacheSysConfig = new ConcurrentHashMap<>();

    private static final String CACHE_SYSCONFIG = "CACHE_SYSCONFIG";

    @Autowired
    private SystemConfigRepository systemConfigRepository;


    @Override
    @Scheduled(fixedRate = 3 * 60 * 1000)
    public void addAllToCache() {
        logger.info(" Call addAllToCache SystemConfig ");
        List<SystemConfig> lstSysConfig = systemConfigRepository.findAll();
        if (cacheSysConfig.containsKey(CACHE_SYSCONFIG)) {
            cacheSysConfig.remove(CACHE_SYSCONFIG);
            cacheSysConfig.put(CACHE_SYSCONFIG, lstSysConfig);
        } else {
            cacheSysConfig.put(CACHE_SYSCONFIG, lstSysConfig);
        }
    }

    @Override
    public Collection<List<SystemConfig>> fetchAll() {
        return cacheSysConfig.values();
    }

    @Override
    public SystemConfig findBySysTitle(String sysTitle) {
        List<SystemConfig> lst = new ArrayList<>();
        cacheSysConfig.forEach((key, value) -> lst.addAll(value));
        logger.info("size list sysConfig {}", lst.size());
        List<SystemConfig> lstFinal = lst.stream().filter((a) -> a.getSysTitle().equals(sysTitle)).collect(Collectors.toList());
        return lstFinal.get(0);
    }
}
