package com.okay.testcenter.mapper.webUi;

import com.okay.testcenter.domain.ui.WebPlatform;
import com.okay.testcenter.domain.ui.WebReport;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WebPlatformMapper {
    public int addWebPlatform(WebPlatform webPlatform);

    public int updateWebPlatform(WebPlatform webPlatform);

    public List<WebPlatform> getPlatformList(WebPlatform webPlatform);
}
