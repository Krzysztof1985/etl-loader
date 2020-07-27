package com.superdevs.etlloader.rest;

import com.superdevs.etlloader.dto.CSVItemDto;
import com.superdevs.etlloader.filters.MainFilter;
import com.superdevs.etlloader.model.CSVItem;
import com.superdevs.etlloader.service.CSVService;
import com.superdevs.etlloader.service.ConverterService;
import com.superdevs.etlloader.wrappers.DataResponseWrapper;
import com.superdevs.etlloader.wrappers.ResponseWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.superdevs.etlloader.api.Constants.Descriptions.ETL_SERVICE_DESC;
import static com.superdevs.etlloader.api.Constants.Responses.CODE_200;
import static com.superdevs.etlloader.api.Constants.Responses.CODE_400;
import static com.superdevs.etlloader.api.Constants.Responses.CODE_500;
import static com.superdevs.etlloader.api.Constants.Tags.ETL_SERVICE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@Api(description = ETL_SERVICE_DESC, consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE,
        tags = {ETL_SERVICE})
public class EtlController {

    private CSVService csvService;
    private ConverterService converterService;

    @ApiOperation(value = "Performs upload csv file", notes = "<b>Values in CSV file must be unique, if in db exists entry which is in file, application will crash</b>")
    @ApiResponses({
            @ApiResponse(code = 200, message = CODE_200),
            @ApiResponse(code = 400, message = CODE_400, response = ResponseWrapper.class),
            @ApiResponse(code = 500, message = CODE_500, response = ResponseWrapper.class)
    })
    @PostMapping("/csv/upload")
    public DataResponseWrapper<String> uploadCSV(@RequestBody MultipartFile file) {
        List<CSVItem> output = converterService.convert(file);
        csvService.saveAllCSVItems(output);

        DataResponseWrapper<String> response = EtlControllerResponseWrapper.ok("OK")
                .rootMessage(ResponseWrapper.Message.of("200", "Successfully uploaded CSV file",
                        "Upload OK"))
                .build();
        return response;
    }

    @ApiOperation(value = "Performs delete operation. All saved into DB entries will be deleted", notes = "<b>This operation cannot be undone</b>")
    @ApiResponses({
            @ApiResponse(code = 200, message = CODE_200),
            @ApiResponse(code = 400, message = CODE_400, response = ResponseWrapper.class),
            @ApiResponse(code = 500, message = CODE_500, response = ResponseWrapper.class)
    })
    @PutMapping("/delete")
    public DataResponseWrapper<String> deleteAllItems() {
        csvService.deleteAllItems();
        DataResponseWrapper<String> response = EtlControllerResponseWrapper.ok("OK")
                .rootMessage(ResponseWrapper.
                        Message.of("csvLoader-200", "Successfully deleted all items in db app",
                        "Successfully deleted all items"))
                .build();
        return response;
    }

    @ApiOperation(value = "Calculate statistics based on input filters", notes = "<b>from/to parameters takes input only in format MM/dd/yy</b>")
    @ApiResponses({
            @ApiResponse(code = 200, message = CODE_200),
            @ApiResponse(code = 400, message = CODE_400, response = ResponseWrapper.class),
            @ApiResponse(code = 500, message = CODE_500, response = ResponseWrapper.class)
    })
    @PostMapping("/calculate/stats")
    public DataResponseWrapper<Document> calculateStatistics(@RequestBody MainFilter mainFilter) {
        List<Document> documents = csvService.generateAggregatedData(mainFilter);

        DataResponseWrapper<Document> response = EtlControllerResponseWrapper.ok(documents)
                .rootMessage(ResponseWrapper.
                        Message.of("csvLoader-200", "Successfully performed aggregation",
                        "Aggregation finished successfully"))
                .build();
        return response;
    }

    private class EtlControllerResponseWrapper extends DataResponseWrapper<CSVItemDto> {
    }
}
