package com.maeasoftworks.alfaconverterapi.controllers;

import com.maeasoftworks.alfaconverter.Converter;
import com.maeasoftworks.alfaconverterapi.services.FileSplitter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class FileController {
    private final FileSplitter fileSplitter;

    public FileController(FileSplitter fileSplitter) {
        this.fileSplitter = fileSplitter;
    }

    @GetMapping("convert")
    public byte[] convertFile(@RequestBody byte[] bytes) {
        var files = fileSplitter.split(bytes);
        return new Converter(files.get(0), files.get(1)).convert();
    }
}
