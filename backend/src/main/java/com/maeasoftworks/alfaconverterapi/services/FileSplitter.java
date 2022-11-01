package com.maeasoftworks.alfaconverterapi.services;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FileSplitter {
    public ArrayList<byte[]> split(byte[] bytes) {
        var isShielded = false;
        var zeros = 0;
        var files = new ArrayList<byte[]>();
        var file = new ArrayList<Byte>();
        for (var b : bytes) {
            if (isShielded) {
                if (b == 0x00) {
                    zeros = -1;
                }
                file.add(b);
                isShielded = false;
            } else {
                if (b == 0x7f) {
                    isShielded = true;
                    zeros = 0;
                } else if (b == 0x00) {
                    if (zeros == 1) {
                        file.remove(file.size() - 1);
                        files.add(ArrayUtils.toPrimitive(file.toArray(new Byte[0])));
                        zeros = 0;
                        file = new ArrayList<>();
                    } else {
                        file.add(b);
                        zeros++;
                    }
                } else {
                    file.add(b);
                    zeros = 0;
                }
            }
        }
        return files;
    }

    public byte[] merge(byte[][] files) {
        var bytes = new ArrayList<Byte>();
        for (var file : files) {
            var zeros = 0;
            for (var b : file) {
                if (b == 0x00) {
                    zeros++;
                    if (zeros == 2) {
                        zeros = 0;
                        bytes.add(bytes.size() - 1, (byte) 0x7f);
                    }
                } else if (b == 0x7f) {
                    // shielding by double 7f
                    bytes.add(b);
                    zeros = 0;
                } else {
                    zeros = 0;
                }
                bytes.add(b);
            }
            bytes.add((byte) 0x0);
            bytes.add((byte) 0x0);
        }
        return ArrayUtils.toPrimitive(bytes.toArray(new Byte[0]));
    }
}
