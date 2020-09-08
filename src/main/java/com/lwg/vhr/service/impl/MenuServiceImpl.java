package com.lwg.vhr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lwg.vhr.mapper.MenuMapper;
import com.lwg.vhr.pojo.Menu;
import com.lwg.vhr.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    public List<Menu> getAllMenu(){
        return menuMapper.selectByExample(null);

    }
}
