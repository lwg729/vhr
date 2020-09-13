package com.lwg.vhr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.lwg.vhr.mapper.MenuMapper;
import com.lwg.vhr.pojo.Menu;
import com.lwg.vhr.service.MenuService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@CacheConfig(cacheNames = "menus_cache")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Cacheable(key = "#root.methodName")
    public List<Menu> getAllMenuWithRole(){
        return menuMapper.getAllMenuWithRole();

    }
}
