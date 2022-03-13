package com.echoxxzhang.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommunityUtil {

    // 生成UUID随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // 返回一个用MD5加密的字符串
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {  // 如果是空值，则返回null
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**  返回一个json格式的字符串
     * @param code 编码
     * @param msg 字符串消息
     * @param map 封装成一个map,封装业务数据
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    // 方法重载
    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

}
