package com.lwg.vhr;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lwg.vhr.pojo.Menu;
import com.lwg.vhr.service.impl.MenuServiceImpl;

@SpringBootTest
class VhrApplicationTests {

    @Autowired
    private MenuServiceImpl menuService;

    @Test
    void contextLoads() {
    }

    @Test
    public void getAllmenu(){
        List<Menu> allMenu = menuService.getAllMenu();
        System.out.println(allMenu);
    }
}
