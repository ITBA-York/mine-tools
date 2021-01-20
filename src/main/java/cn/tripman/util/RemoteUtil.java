package cn.tripman.util;

public class RemoteUtil {

    public static final String GET_API = "https://api.tripman.cn/cache/get";
    public static final String SET_API = "https://api.tripman.cn/cache/set";

    public static String getValue(String key) {
        RemoteVo remoteVo = new RemoteVo();
        remoteVo.setKey(key);
        return HttpUtil.post(GET_API, JsonUtil.toJSONDefaultNull(remoteVo));
    }

    public static String setValue(String key, String value) {
        RemoteVo remoteVo = new RemoteVo();
        remoteVo.setKey(key);
        remoteVo.setContent(value);
        return HttpUtil.post(SET_API, JsonUtil.toJSONDefaultNull(remoteVo));
    }
}
