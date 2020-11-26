package cn.tripman.wechat;

import cn.tripman.constant.Constants;
import cn.tripman.util.HttpUtil;
import cn.tripman.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author hero
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WxSeo implements Serializable {
    @JsonIgnore
    private String accessToken;

    @JsonProperty("path")
    private String path;

    @JsonProperty("query")
    private String query;

    @JsonProperty("data_list")
    private List<WxSeoDataDto> dataList;


    public void execute() throws Exception {
        if (StringUtils.isEmpty(accessToken) || dataList == null) {
            return;
        }
        String api = String.format(Constants.WX_SEO, accessToken);
        HttpUtil.post(api, JsonUtil.toJSON(this));
    }

    public String postString() throws Exception {
        return JsonUtil.toJSON(this);
    }

    public String postApi() throws Exception {
        return String.format(Constants.WX_SEO, accessToken);
    }
}
