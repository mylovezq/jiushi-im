package com.lld.im.service.utils;

import com.alibaba.fastjson.JSONObject;
import com.lld.im.common.constant.Constants;
import com.lld.im.common.enums.ImConnectStatusEnum;
import com.lld.im.common.model.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lld
 * @version: 1.0
 */
@Component
public class UserSessionUtils {

    public Object get;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //1.获取用户所有的session

    public List<UserSession> getUserSession(Integer appId, String userId) {

        String userSessionKey = appId + Constants.RedisConstants.UserSessionConstants + userId;
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(userSessionKey);

       return entries.values().parallelStream()
                .map(sessionStr -> {
                    UserSession session = JSONObject.parseObject((String) sessionStr, UserSession.class);
                    if (session.getConnectState() == ImConnectStatusEnum.ONLINE_STATUS.getCode()) {
                        return session;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    //2.获取用户除了本端的session

    //1.获取用户所有的session

    public UserSession getUserSession(Integer appId, String userId, Integer clientType, String imei) {

        String userSessionKey = appId + Constants.RedisConstants.UserSessionConstants + userId;
        String hashKey = clientType + ":" + imei;
        Object o = stringRedisTemplate.opsForHash().get(userSessionKey, hashKey);
        if (o != null){
            return JSONObject.parseObject(o.toString(), UserSession.class);
        }
        return null;
    }


}
