package cn.tripman.util;

import cn.tripman.helper.HttpClientHelper;
import okhttp3.*;

import java.util.Map;
import java.util.Objects;

/**
 * @author hero
 */
public class HttpUtil {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = HttpClientHelper.getUnsafeOkHttpClient();

    public static String get(String api) {
        return get(api, null, 1);
    }

    public static String get(String api, int retry) {
        return get(api, null, retry);
    }

    public static String get(String api, Map<String, String> header) {
        return get(api, header, 1);
    }

    public static String get(String api, Map<String, String> header, int retry) {
        Request.Builder request = new Request.Builder().url(api);
        if (header != null) {
            header.keySet().forEach(key -> request.addHeader(key, header.get(key)));
        }
        Response response = null;
        for (int i = 0; i < retry; i++) {
            try {
                response = client.newCall(request.build()).execute();
                if (response.isSuccessful()) {
                    return Objects.requireNonNull(response.body()).string();
                } else {
                    throw new Exception(Objects.requireNonNull(response.body()).string());
                }
            } catch (Exception ignored) {
            } finally {
                close(response);
            }
        }
        return null;
    }

    public static String post(String api, String content) {
        return post(api, content, null, 1);
    }

    public static String post(String api, String content, int retry) {
        return post(api, content, null, retry);
    }

    public static String post(String api, String content, Map<String, String> header) {
        return post(api, content, header, 1);
    }

    public static String post(String api, String content, Map<String, String> header, int retry) {
        RequestBody body = RequestBody.create(JSON, content);
        Request.Builder request = new Request.Builder().url(api).post(body);
        if (header != null) {
            header.keySet().forEach(key -> request.addHeader(key, header.get(key)));
        }
        Response response = null;
        for (int i = 0; i < retry; i++) {
            try {
                response = client.newCall(request.build()).execute();
                if (response.isSuccessful()) {
                    return Objects.requireNonNull(response.body()).string();
                }
            } catch (Exception ignored) {
            } finally {
                close(response);
            }
        }
        return null;
    }

    public static void close(Response response) {
        if (response == null) {
            return;
        }
        try {
            response.close();
        } catch (Exception ignored) {
        }
    }
}
