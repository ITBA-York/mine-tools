import cn.tripman.util.DateUtil;
import okhttp3.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author liuyu
 */
public class HttpUtil {

    private static OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();
    private static OkHttpClient client2 = new OkHttpClient().newBuilder()
            .connectTimeout(1, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.SECONDS)
            .build();
    private static MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public static String get(String api) {
        Request request = new Request.Builder().url(api).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    public static String getWithRetry(String api, int times) {
        for (int i = 0; i < times; i++) {
            Request request = new Request.Builder().url(api).build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (Exception e) {
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        }
        return null;
    }

    public static String postJson(String api, String data) {
        RequestBody body = RequestBody.create(JSON, data);
        Request request = new Request.Builder().url(api).post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    public static String post(String api) {
        Request request = new Request.Builder()
                .post(okhttp3.internal.Util.EMPTY_REQUEST)
                .url(api)
                .build();
        Response response = null;
        try {
            response = client2.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }


    public static String postJsonWithRetry(String api, String data, int times) {
        for (int i = 0; i < times; i++) {
            RequestBody body = RequestBody.create(JSON, data);
            Request request = new Request.Builder().url(api).post(body).build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (Exception e) {
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("start" + DateUtil.toTime(new Date()));
        System.out.println(post("http://127.0.0.1/hello"));
        System.out.println("end" + DateUtil.toTime(new Date()));
    }
}
