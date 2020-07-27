package com.superdevs.etlloader.filters;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Pattern;
import java.util.Map;
import java.util.Set;

@Value
@Builder
public class MainFilter {

    @ApiModelProperty(name = "Metric filters", required = true, example = "{\"clicks\":\"true\",\"impressions\":\"true\"}")
    Map<String, Boolean> metrics = Map.of("clicks", true, "impressions", true);


    @ApiModelProperty(name = "dataSource", required = true, example = "[\"Google Ads\", \"Twitter Ads\"]")
    Set<String> dataSource = Set.of("Google Ads", "Twitter Ads");

    @Pattern(regexp = "\\d{2}\\/\\d{2}\\/\\d{2}")
    @ApiModelProperty(name = "from", example = "12/18/20", required = true, notes = "Input starting date parameter must be in format <b> MM/dd/yy </b>")
    String from;

    @Pattern(regexp = "\\d{2}\\/\\d{2}\\/\\d{2}")
    @ApiModelProperty(name = "to", example = "12/23/20", required = true, notes = "End date  must be in format <b> MM/dd/yy </b>")
    String to;

}
