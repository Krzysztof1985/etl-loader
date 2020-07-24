package com.superdevs.etlloader.filters;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DataSourceFilter {

    @ApiModelProperty(name = "dataSource", example = "Google Ads", required = true)
    String dataSource;

    @ApiModelProperty(name = "from", example = "1", required = true)
    Long from;

    @ApiModelProperty(name = "to", example = "1", required = true)
    Long to;
}
