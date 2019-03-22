package com.Controller;

import com.model.SystemConfig;
import com.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("sys")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @RequestMapping(value = "/addalltocache", method = RequestMethod.GET)
    public String addAllToCache() {
        systemConfigService.addAllToCache();
        return "true";
    }

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public List<SystemConfig> getSysConfig(){
        return systemConfigService.fetchAll();
    }
}
