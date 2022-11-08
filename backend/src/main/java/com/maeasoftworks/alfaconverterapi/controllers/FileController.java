package com.maeasoftworks.alfaconverterapi.controllers;

import com.maeasoftworks.alfaconverter.ConverterContainer;
import com.maeasoftworks.alfaconverterapi.dao.Log;
import com.maeasoftworks.alfaconverterapi.services.Logger;
import lombok.val;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api")
public class FileController {
    private final Logger logger;

    public FileController(Logger logger) {
        this.logger = logger;
    }

    @GetMapping("headers")
    @ResponseBody
    public List<List<String>> getHeaders(
            @RequestParam("first-file") MultipartFile firstFile,
            @RequestParam("second-file") MultipartFile secondFile
    ) {
        checkFiles(firstFile, secondFile, null);
        byte[] bytes1;
        byte[] bytes2;
        try {
            bytes1 = firstFile.getBytes();
            bytes2 = secondFile.getBytes();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot process this document");
        }
        return new ConverterContainer().getHeaders(bytes1, bytes2);
    }

    @PostMapping("convert")
    public ResponseEntity<ByteArrayResource> convert(
            @RequestParam("conversion") String conversion,
            @RequestParam("master-file") int masterFile,
            @RequestParam("first-file") MultipartFile firstFile,
            @RequestParam("second-file") MultipartFile secondFile
    ) {
        checkFiles(firstFile, secondFile, conversion);
        byte[] bytes1;
        byte[] bytes2;
        try {
            bytes1 = firstFile.getBytes();
            bytes2 = secondFile.getBytes();
        } catch (IOException e) {
            logger.write(new Log(LocalDateTime.now(), conversion, 422));
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot process this document");
        }
        var converter = new ConverterContainer();
        converter.initialize(bytes1, bytes2);
        converter.setConversion(conversion);
        converter.setHeadship(masterFile);
        val result = converter.convert();
        logger.write(new Log(LocalDateTime.now(), conversion, 0));
        val headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename="
                        + firstFile.getOriginalFilename()
                        + " + "
                        + secondFile.getOriginalFilename()
                        + ".xlsx"
        );
        return ResponseEntity.ok().headers(headers).body(new ByteArrayResource(result));
    }

    private void checkFiles(MultipartFile firstFile, MultipartFile secondFile, String conversion) {
        if (firstFile.getOriginalFilename() == null || Objects.equals(firstFile.getOriginalFilename(), "") ||
                secondFile.getOriginalFilename() == null || Objects.equals(secondFile.getOriginalFilename(), "")
        ) {
            if (conversion != null) {
                logger.write(new Log(LocalDateTime.now(), conversion, 400));
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Required arguments were empty");
        }
        if (!Objects.equals(firstFile.getOriginalFilename().split("\\.")[1], "xlsx") &&
                !Objects.equals(secondFile.getOriginalFilename().split("\\.")[1], "xlsx")) {
            if (conversion != null) {
                logger.write(new Log(LocalDateTime.now(), conversion, 422));
            }
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot process this documents");
        }
    }
}
