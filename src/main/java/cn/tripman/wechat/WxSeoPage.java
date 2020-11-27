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
public class WxSeoPage implements Serializable {


    @JsonProperty("path")
    private String path;

    @JsonProperty("query")
    private String query;

    @JsonProperty("data_list")
    private List<WxSeoDataDto> dataList;




}
