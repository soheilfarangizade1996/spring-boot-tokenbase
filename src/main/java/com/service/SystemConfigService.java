package com.service;

import com.model.SystemConfig;

import java.util.Collection;
import java.util.List;

public interface SystemConfigService {

    void addAllToCache();

    Collection<List<SystemConfig>> fetchAll();

    SystemConfig findBySysTitle(String sysTitle);
}
