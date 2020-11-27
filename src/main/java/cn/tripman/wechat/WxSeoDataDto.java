package cn.tripman.wechat;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author hero
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WxSeoDataDto {

    @JsonProperty("@type")
    private String type;

    private int update = 1;
    @JsonProperty("content_id")

    private String contentId;

    @JsonProperty("category_id")
    private int categoryId;

    @JsonProperty("page_type")
    private int pageType;

    @JsonProperty("title")
    private String title;

    @JsonProperty("subtitle")
    private List<String> subtitle;

    @JsonProperty("abstract")
    private List<String> abstracts;

    @JsonProperty("mainbody")
    private String mainbody;

    @JsonProperty("h5_url")
    private String h5Url;

    @JsonProperty("weapp_url")
    private String weappUrl;

    @JsonProperty("tag")
    private List<String> tags;

    @JsonProperty("time_publish")
    private long publish;

    @JsonProperty("time_modify")
    private long modify;


}
