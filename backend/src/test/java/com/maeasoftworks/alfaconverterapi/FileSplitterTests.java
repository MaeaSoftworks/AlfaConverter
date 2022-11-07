package com.maeasoftworks.alfaconverterapi;

import com.maeasoftworks.alfaconverterapi.services.FileSplitter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FileSplitter.class)
public class FileSplitterTests {
    @Autowired
    private FileSplitter fileSplitter;

    static Stream<Pair<byte[][], byte[]>> shieldsProvider() {
        return Stream.of(
                new ImmutablePair<>(
                        new byte[][]{
                                new byte[]{0x11, 0x11, 0x11}
                        },
                        new byte[]{0x11, 0x11, 0x11, 0x00, 0x00}),
                new ImmutablePair<>(
                        new byte[][]{
                                new byte[]{0x11, 0x00, 0x00}
                        },
                        new byte[]{0x11, 0x7f, 0x00, 0x00, 0x00, 0x00}),
                new ImmutablePair<>(
                        new byte[][]{
                                new byte[]{0x11, 0x7f, 0x00, 0x00}
                        },
                        new byte[]{0x11, 0x7f, 0x7f, 0x7f, 0x00, 0x00, 0x00, 0x00})
        );
    }

    static Stream<Pair<byte[][], byte[]>> mergesProvider() {
        return Stream.of(
                new ImmutablePair<>(
                        new byte[][]{
                                new byte[]{0x11, 0x11, 0x11},
                                new byte[]{0x11, 0x11, 0x11}
                        },
                        new byte[]{0x11, 0x11, 0x11, 0x00, 0x00, 0x11, 0x11, 0x11, 0x00, 0x00}),
                new ImmutablePair<>(
                        new byte[][]{
                                new byte[]{0x11, 0x00, 0x11},
                                new byte[]{0x11, 0x00, 0x11}
                        },
                        new byte[]{0x11, 0x00, 0x11, 0x00, 0x00, 0x11, 0x00, 0x11, 0x00, 0x00}),
                new ImmutablePair<>(
                        new byte[][]{
                                new byte[]{0x11, 0x00, 0x00},
                                new byte[]{0x11, 0x00, 0x00}
                        },
                        new byte[]{0x11, 0x7f, 0x00, 0x00, 0x00, 0x00, 0x11, 0x7f, 0x00, 0x00, 0x00, 0x00})
        );
    }

    static Stream<ImmutablePair<byte[], List<byte[]>>> splitsProvider() {
        return Stream.of(
                new ImmutablePair<>(
                        new byte[]{0x11, 0x11, 0x11, 0x00, 0x00, 0x11, 0x11, 0x11, 0x00, 0x00},
                        new ArrayList<>(List.of(
                                new byte[]{0x11, 0x11, 0x11},
                                new byte[]{0x11, 0x11, 0x11}
                        ))
                ),
                new ImmutablePair<>(
                        new byte[]{0x11, 0x7f, 0x00, 0x00, 0x00, 0x00, 0x11, 0x7f, 0x00, 0x00, 0x00, 0x00},
                        new ArrayList<>(List.of(
                                new byte[]{0x11, 0x00, 0x00},
                                new byte[]{0x11, 0x00, 0x00}
                        ))
                )
        );
    }

    @ParameterizedTest
    @MethodSource({"shieldsProvider", "mergesProvider"})
    void shieldsCorrectly(Pair<byte[][], byte[]> source) {
        var files = source.getLeft();
        var expected = source.getRight();
        var actual = fileSplitter.merge(files);
        assertEquals(actual.length, expected.length);
        for (var i = 0; i < expected.length; i++) {
            assertEquals(actual[i], expected[i], "pos: " + i);
        }
    }

    @ParameterizedTest
    @MethodSource({"splitsProvider"})
    void splitsCorrectly(ImmutablePair<byte[], ArrayList<byte[]>> source) {
        var rawData = source.getLeft();
        var expected = source.getRight();
        var actual = fileSplitter.split(rawData);
        assertEquals(expected.size(), actual.size());
        for (var i = 0; i < expected.size(); i++) {
            assertEquals(actual.get(i).length, expected.get(i).length, "file: " + i);
            var actualFile = actual.get(i);
            var expectedFile = expected.get(i);
            for (var k = 0; k < expectedFile.length; k++) {
                assertEquals(actualFile[i], expectedFile[i], "file: " + i + " pos: " + k);
            }
        }
    }

    @Test
    void mergeAndConvertReturnSameFiles() throws IOException {
        var firstFile = Files.readAllBytes(Path.of("src/test/resources/first.xlsx"));
        var secondFile = Files.readAllBytes(Path.of("src/test/resources/second.xlsx"));
        var merged = fileSplitter.merge(new byte[][]{firstFile, secondFile});
        var actual = fileSplitter.split(merged);
        assertEquals(2, actual.size());
        var actualFirst = actual.get(0);
        var actualSecond = actual.get(1);
        assertEquals(firstFile.length, actualFirst.length);
        assertEquals(secondFile.length, actualSecond.length);
        for (var i = 0; i < firstFile.length; i++) {
            assertEquals(firstFile[i], actualFirst[i], "first: " + i);
        }
        for (var i = 0; i < secondFile.length; i++) {
            assertEquals(secondFile[i], actualSecond[i], "second: " + i);
        }
    }
}
