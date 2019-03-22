package com.service;

import com.model.SystemConfig;
import java.util.List;

public interface SystemConfigService {

    void addAllToCache();

    List<SystemConfig> fetchAll();

    SystemConfig findBySysTitle(String sysTitle);
}
