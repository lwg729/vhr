package com.lwg.vhr.config;

import java.util.Collection;
import java.util.List;

import com.lwg.vhr.pojo.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.lwg.vhr.pojo.Menu;
import com.lwg.vhr.service.impl.MenuServiceImpl;

//该类的的主要作用是通过当前的请求地址,获得改地址需要的用户角色
@Component
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    MenuServiceImpl menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    //getAttributes方法返回的集合最终会到AccessDecisionMannger(授权管理器)中
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获得请求的url
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        //查询角色与url的关系
        List<Menu> menus = menuService.getAllMenuWithRole();
        for (Menu menu : menus) {
            if (antPathMatcher.match(menu.getUrl(), requestUrl)) {
                //获取菜单对应的角色 并返回一个角色的集合
                List<Role> roles = menu.getRoles();
                String[] str = new String[roles.size()];
                for (int i = 0; i < roles.size(); i++) {
                    str[i] = roles.get(i).getName();
                }
                return SecurityConfig.createList(str);
            }
        }
        //所有未匹配到的路径 都是登录后可访问  如果返回null 不登录也可访问
        return SecurityConfig.createList("ROLE_LOGIN");
    }


    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    //检验传入的安全对象是否和FilterInvocation类同一类型，或是他的子类 getAttributes(Object o)方法会调用这个方法
    //保证String requestUrl = ((FilterInvocation) o).getRequestUrl();的正确定
    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
