package com.maeasoftworks.alfaconverterapi.controllers;

import com.maeasoftworks.alfaconverter.ConverterContainer;
import com.maeasoftworks.alfaconverterapi.services.FileSplitter;
import lombok.val;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class FileController {
    private final FileSplitter fileSplitter;

    public FileController(FileSplitter fileSplitter) {
        this.fileSplitter = fileSplitter;
    }

    @GetMapping("headers")
    @ResponseBody
    public List<List<String>> getHeaders(@RequestBody byte[] bytes) {
        val files = fileSplitter.split(bytes);
        return new ConverterContainer().getHeaders(files.get(0), files.get(1));
    }

    @GetMapping("convert")
    public byte[] convertFile(@RequestBody byte[] bytes) {
        var files = fileSplitter.split(bytes);
        return null;
    }
}
