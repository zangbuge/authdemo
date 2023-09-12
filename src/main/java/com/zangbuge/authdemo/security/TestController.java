package com.zangbuge.authdemo.security;

import com.zangbuge.authdemo.common.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lhm
 * @date: 2023/9/8
 */
@RestController
public class TestController {

    @PostMapping("/testSecurity")
    @PreAuthorize("hasAnyAuthority('test')")
    public Result<String> testSecurity(String name) {
        return Result.createBySuccess("操作成功", name);
    }

    @PostMapping("/testRole")
    @PreAuthorize("hasAnyRole('admin')")
    public Result<String> testRole(String name) {
        return Result.createBySuccess("操作成功", name);
    }

    @PostMapping("/testAuth")
    @PreAuthorize("hasAnyAuthority('auth')")
    public Result<String> testAuth(String name) {
        return Result.createBySuccess("操作成功", name);
    }

}
