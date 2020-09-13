package com.lwg.vhr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lwg.vhr.mapper.HrMapper;
import com.lwg.vhr.pojo.Hr;
import com.lwg.vhr.service.HrService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HrServiceImpl implements HrService, UserDetailsService {

    @Autowired
    private HrMapper hrMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Hr hr = hrMapper.loadUserByUsername(username);
        if (hr==null){
            throw new UsernameNotFoundException("用户不存在");
        }
        return hr;
    }
}
