package cn.tripman.wechat;

import cn.tripman.constant.Constants;
import cn.tripman.util.HttpUtil;
import cn.tripman.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WxSeo {

    @JsonProperty("access_token")
    private String access_token;

    @JsonProperty("pages")
    List<WxSeoPage> pages;


    public String execute() throws Exception {
        if (StringUtils.isEmpty(access_token) || pages == null || pages.size() == 0) {
            return null;
        }
        String api = String.format(Constants.WX_SEO, access_token);
        return HttpUtil.post(api, JsonUtil.toJSON(this));
    }

    public void printString() throws Exception {
        System.out.println(postString());
    }

    public String postString() throws Exception {
        return JsonUtil.toJSON(this);
    }

    public String postApi() throws Exception {
        return String.format(Constants.WX_SEO, access_token);
    }
}
