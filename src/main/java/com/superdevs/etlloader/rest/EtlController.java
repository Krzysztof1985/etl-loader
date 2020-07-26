package com.superdevs.etlloader.rest;

import com.superdevs.etlloader.dto.CSVItemDto;
import com.superdevs.etlloader.dto.CsvToSaveDto;
import com.superdevs.etlloader.filters.DataSourceFilter;
import com.superdevs.etlloader.filters.MainFilter;
import com.superdevs.etlloader.model.CSVItem;
import com.superdevs.etlloader.service.CSVService;
import com.superdevs.etlloader.service.ConverterService;
import com.superdevs.etlloader.wrappers.DataResponseWrapper;
import com.superdevs.etlloader.wrappers.ResponseWrapper;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
public class EtlController {
    private CSVService csvService;
    private ConverterService converterService;

    @GetMapping
    public String works() {
        return "IT WORKS!";
    }

    @GetMapping("hello/{input}")
    public DataResponseWrapper<String> sayHello(@PathVariable String input) {
        DataResponseWrapper<String> response = EtlControllerResponseWrapper.ok("SAY HELLO " + input)
                .rootMessage(ResponseWrapper.Message.of("200", "Dummy test works", "You said hello!"))
                .build();
        return response;
    }

    @PostMapping("/upload")
    public DataResponseWrapper<String> uploadCSV(@RequestBody MultipartFile file) {
        List<CSVItem> output = converterService.convert(file);
        csvService.saveAllCSVItems(output);

        DataResponseWrapper<String> response = EtlControllerResponseWrapper.ok("OK")
                .rootMessage(ResponseWrapper.Message.of("200", "Successfully uploaded CSV fiel",
                        "Upload OK"))
                .build();
        return response;
    }

    @GetMapping("/uuid/{uuid}")
    public DataResponseWrapper<CSVItemDto> findAllItemsForWorkItem(String uuid) {
        List<CSVItemDto> allItemsForWorkId = csvService.getAllItemsForWorkId(uuid);

        DataResponseWrapper<CSVItemDto> response = EtlControllerResponseWrapper.ok(allItemsForWorkId)
                .rootMessage(ResponseWrapper.
                        Message.of("csvLoader-200", "Successfully fetched app",
                        "Successfully added application icon"))
                .build();
        return response;
    }

    @ApiOperation(value = "Calculate clicks based on input filters", notes = "from/to paramters takes input only in format MM/dd/yy")
    @PostMapping("/calculate/clicks")
    public DataResponseWrapper<CSVItemDto> findMatching(@RequestBody DataSourceFilter dataSourceFilter) {
        csvService.countTotalClicks( dataSourceFilter.getDataSource(), dataSourceFilter.getFrom(), dataSourceFilter.getTo());
        return null;
    }

    @ApiOperation(value = "Calculate clicks based on input filters", notes = "from/to paramters takes input only in format MM/dd/yy")
    @PostMapping("/calculate/clicks2")
    public DataResponseWrapper<CSVItemDto> findMatchingXXX(@RequestBody MainFilter mainFilter) {
        System.out.println(mainFilter);
        return null;
    }


    private class EtlControllerResponseWrapper extends DataResponseWrapper<CSVItemDto> {
    }
}
