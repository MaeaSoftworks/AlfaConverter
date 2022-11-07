package com.maeasoftworks.alfaconverterapi;

import com.maeasoftworks.alfaconverter.ConverterContainer;
import com.maeasoftworks.alfaconverterapi.services.FileSplitter;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
public class ConverterIntegrationTests {
    @Autowired
    private FileSplitter fileSplitter;

    @Test
    void basicIntegrationTest() throws IOException {
        var container = new ConverterContainer().getHeaders(
                FileUtils.readFileToByteArray(new File("src/test/resources/first.xlsx")),
                FileUtils.readFileToByteArray(new File("src/test/resources/second.xlsx"))
        );
        for (var i = 0; i < container.size(); i++) {
            System.out.println(i + ":");
            for (var header : container.get(i)) {
                System.out.println(header);
            }
        }
    }

    @Test
    void extendedIntegrationTest() throws IOException {
        var firstFile = Files.readAllBytes(Path.of("src/test/resources/first.xlsx"));
        var secondFile = Files.readAllBytes(Path.of("src/test/resources/second.xlsx"));
        Files.write(Path.of("src/test/resources/merged.xlsx"), fileSplitter.merge(new byte[][]{firstFile, secondFile}));

        var files = fileSplitter.split(FileUtils.readFileToByteArray(new File("src/test/resources/merged.xlsx")));

        var container = new ConverterContainer().getHeaders(files.get(0), files.get(1));
        for (var i = 0; i < container.size(); i++) {
            System.out.println(i + ":");
            for (var header : container.get(i)) {
                System.out.println(header);
            }
        }
    }
}
