package cn.tripman.constant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hero
 */
public interface Constants {

    String UTF8 = "UTF-8";

    String TRUE = "TRUE";

    String FALSE = "FALSE";

    String WX_SEO = "https://api.weixin.qq.com/wxa/search/wxaapi_submitpages?access_token=%s";

    ExecutorService POOL = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());


}
