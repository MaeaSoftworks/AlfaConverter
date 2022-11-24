package com.maeasoftworks.alfaconverterapi.controllers;

import com.maeasoftworks.alfaconverter.Converter;
import com.maeasoftworks.alfaconverterapi.dao.Log;
import com.maeasoftworks.alfaconverterapi.dto.LiteDocument;
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
@CrossOrigin
public class FileController {
    private final Logger logger;

    public FileController(Logger logger) {
        this.logger = logger;
    }


    @PostMapping("headers")
    @ResponseBody
    public List<LiteDocument> getHeaders(
            @RequestParam("first-file") MultipartFile firstFile,
            @RequestParam("second-file") MultipartFile secondFile
    ) {
        val extension = checkFiles(firstFile, secondFile, null);
        byte[] bytes1;
        byte[] bytes2;
        try {
            bytes1 = firstFile.getBytes();
            bytes2 = secondFile.getBytes();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot process this document");
        }
        val result = Converter.Companion.ofFiles(bytes1, bytes2, extension).getHeaders();
        return List.of(new LiteDocument(result.get(0), result.get(1)), new LiteDocument(result.get(2), result.get(3)));
    }

    @PostMapping("convert")
    public ResponseEntity<ByteArrayResource> convert(
            @RequestParam("conversion") String conversion,
            @RequestParam("master-file") int masterFile,
            @RequestParam("first-file") MultipartFile firstFile,
            @RequestParam("second-file") MultipartFile secondFile
    ) {
        val extension = checkFiles(firstFile, secondFile, conversion);
        byte[] bytes1;
        byte[] bytes2;
        try {
            bytes1 = firstFile.getBytes();
            bytes2 = secondFile.getBytes();
        } catch (IOException e) {
            logger.write(new Log(LocalDateTime.now(), conversion, 422));
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot process this document");
        }
        var converter = Converter.Companion.ofFiles(bytes1, bytes2, extension);
        converter.setConversion(conversion);
        converter.initialize();
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

    private String checkFiles(MultipartFile firstFile, MultipartFile secondFile, String conversion) {
        if (firstFile.getOriginalFilename() == null || Objects.equals(firstFile.getOriginalFilename(), "") ||
                secondFile.getOriginalFilename() == null || Objects.equals(secondFile.getOriginalFilename(), "")
        ) {
            if (conversion != null) {
                logger.write(new Log(LocalDateTime.now(), conversion, 400));
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Required arguments were empty");
        }
        val extension1 = firstFile.getOriginalFilename().split("\\.")[1];
        val extension2 = secondFile.getOriginalFilename().split("\\.")[1];

        if (!Objects.equals(extension1, "xlsx") && !Objects.equals(extension2, "xlsx")) {
            if (conversion != null) {
                logger.write(new Log(LocalDateTime.now(), conversion, 422));
            }
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Unsupported file format");
        }
        if (!Objects.equals(extension1, extension2)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Files' extensions was not the same");
        }
        return extension1;
    }
}
