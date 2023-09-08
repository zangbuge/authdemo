package com.zangbuge.authdemo.security;

import cn.hutool.core.lang.UUID;
import com.zangbuge.authdemo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author: lhm
 * @date: 2023/8/24
 */

@Slf4j
@RestController
public class UserAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/userLogin")
    public Result<String> login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            // 该方法会去调用 UserDetailsServiceImpl.loadUserByUsername
            Authentication authenticate = authenticationManager.authenticate(upToken);
            String token = UUID.randomUUID().toString().replace("-", "");
            // 认证后获取用户信息
            User user = (User) authenticate.getPrincipal();
            redisTemplate.opsForValue().set(token, user, 30, TimeUnit.MINUTES);
            return Result.createBySuccess("登录成功", token);
        } catch (Exception e) {
            log.error("认证失败", e);
            return Result.createByError("认证失败");
        }
    }

    /**
     * @param name
     * @return
     * @PreAuthorize("") hasAnyAuthority(), 支持多个参数, hasAnyAuthority方法也可用自定义接口实现. @PreAuthorize(“@ss.hasPermi(‘system:user:remove’)”)  ss 为注入的服务名
     * hasRole()方法需加前缀 "ROLE_", 源码中有写死前缀
     */
/*    @PostMapping("/testSecurity")
    @PreAuthorize("hasAnyAuthority('test')")
    public Result<String> testSecurity(String name) {
        log.info("testSecurity: {}", name);
        return Result.createBySuccess("登录成功", name);
    }*/
}
