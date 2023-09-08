package com.zangbuge.authdemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: lhm
 * @date: 2023/8/24
 */
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private SimpleGrantedAuthority genSimpleGrantedAuthority(String role) {
        // 若使用hasAnyRole 如: @PreAuthorize("hasAnyRole('admin')")
        // 角色权限前面需要添加"ROLE_"前缀
        return new SimpleGrantedAuthority(role);
    }

    /**
     * @param userName
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        // 用户权限列表
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        // 当前用户权限, 数据库获取
        String role = "test";
        grantedAuthorities.add(genSimpleGrantedAuthority(role));
        grantedAuthorities.add(genSimpleGrantedAuthority("ROLE_admin"));
        // 返回用户权限信息
        String pwd = "123456";
        String encode = passwordEncoder.encode(pwd);
        return new User("lhm", encode, grantedAuthorities);
    }
}
