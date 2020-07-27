package com.superdevs.etlloader.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Pattern;
import java.util.Map;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Value
@Builder
@JsonInclude(NON_EMPTY)
public class MainFilter {

    @ApiModelProperty(name = "Metric filters", required = true, example = "{\"clicks\": true,\"impressions\":true}")
    Map<String, Boolean> metrics = Map.of("clicks", true, "impressions", true);

    @ApiModelProperty(name = "dataSource", required = false, example = "[\"Google Ads\", \"Twitter Ads\"]")
    Set<String> dataSource = Set.of("Google Ads", "Twitter Ads");

    @ApiModelProperty(name = "campaign", required = false, example = "[\"Carfinder\",  \"PLA\"]")
    Set<String> campaigns = Set.of("Carfinder", "PLA");

    @ApiModelProperty(name = "Grouping dimension", example = "dataSource", value = "dataSource", required = true)
    GroupDimension groupDimension;

    @Pattern(regexp = "\\d{2}\\/\\d{2}\\/\\d{2}")
    @ApiModelProperty(name = "from", example = "01/01/19", required = true, notes = "Input starting date parameter must be in format <b> MM/dd/yy </b>")
    String from;

    @Pattern(regexp = "\\d{2}\\/\\d{2}\\/\\d{2}")
    @ApiModelProperty(name = "to", example = "12/30/20", required = true, notes = "End date  must be in format <b> MM/dd/yy </b>")
    String to;

}
