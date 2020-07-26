package com.superdevs.etlloader.filters;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Pattern;

@Value
@Builder
public class DataSourceFilter {

    @ApiModelProperty(name = "dataSource", example = "Google Ads", required = true)
    String dataSource;

    @Pattern(regexp = "\\d{2}\\/\\d{2}\\/\\d{2}")
    @ApiModelProperty(name = "from", example = "12/18/20", required = true, notes = "Input starting date parameter must be in format <b> MM/dd/yy </b>")
    String from;

    @Pattern(regexp = "\\d{2}\\/\\d{2}\\/\\d{2}")
    @ApiModelProperty(name = "to", example ="12/23/20", required = true, notes = "End date  must be in format <b> MM/dd/yy </b>")
    String to;
}
