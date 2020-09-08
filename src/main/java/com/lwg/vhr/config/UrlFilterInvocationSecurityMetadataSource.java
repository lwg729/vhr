package com.lwg.vhr.config;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.lwg.vhr.pojo.Menu;
import com.lwg.vhr.service.impl.MenuServiceImpl;

@Component
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private MenuServiceImpl menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        //获得请求的url
        String requestUrl = ((FilterInvocation) o).getRequestUrl();

        List<Menu> allMenu = menuService.getAllMenuWithRole();
        for (Menu menu : allMenu) {
            if (antPathMatcher.match(menu.getUrl(),requestUrl)&& menu.getRoles().size()>0){

            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
