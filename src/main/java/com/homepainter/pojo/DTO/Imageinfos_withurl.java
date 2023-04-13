package com.homepainter.pojo.DTO;

import com.tencentcloudapi.tiia.v20190529.models.ImageInfo;
import lombok.Data;


import java.util.Map;

@Data
public class Imageinfos_withurl extends ImageInfo {


    private String image_url;

    private Object tags_map;
}
