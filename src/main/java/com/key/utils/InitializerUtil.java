package com.key.utils;

import org.springframework.stereotype.Component;

/**
 * @author 徐雨轩
 * @description 初始化token配置
 * @date 2020-09-21 14:59
 */
@Component
public class InitializerUtil {
    private TokenSettings tokenSettings;
    public InitializerUtil(TokenSettings tokenSettings){
        JwtTokenUtil.setTokenSettings(tokenSettings);
    }
}
