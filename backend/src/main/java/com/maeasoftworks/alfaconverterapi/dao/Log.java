package com.maeasoftworks.alfaconverterapi.dao;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "logs")
@Getter
@Setter
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;
    private final LocalDateTime timestamp;
    @Lob
    private final String conversion;
    private final int resultCode;

    public Log(LocalDateTime timestamp, String conversion, int resultCode) {
        this.conversion = conversion;
        this.resultCode = resultCode;
        this.timestamp = timestamp;
    }

    protected Log() {
        this.conversion = "";
        this.resultCode = -1;
        this.timestamp = LocalDateTime.now();
    }
}
