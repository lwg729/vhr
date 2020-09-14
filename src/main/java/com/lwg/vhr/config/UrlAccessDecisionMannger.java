package com.lwg.vhr.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

@Component
public class UrlAccessDecisionMannger implements AccessDecisionManager {

    /**
     * @param authentication   保存了当前登录用户的角色信息
     * @param o
     * @param configAttributes UrlFilterInvocationSecurityMetadataSource中的getAttributes方法传来的,表示当前请求需要的角色
     *                         可能有多个
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, AuthenticationException {

        //循环出传来所需要的角色
        for (ConfigAttribute configAttribute : configAttributes) {
            String needRole = configAttribute.getAttribute();

            //当前请求所需要的权限如果是ROLE_LOGIN则表示登录即可访问,和角色没有关系
            if ("ROLE_LOGIN".equals(needRole)) {
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new AccessDeniedException("尚未登录，请登录!");
                    //登陆了就直接返回,这个请求将被成功执行
                } else {
                    return;
                }
            }
            //当前用户所具有的权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            //用户包含一个请求的角色权限就算授权成功 遍历是查看当前的角色列表中是否准备需要的权限,具备就返回,不具备报异常
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足，请联系管理员!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
