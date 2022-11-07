package com.maeasoftworks.alfaconverterapi.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class Log {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    public long id;

    @Getter
    @Setter
    public Date timestamp;

    @Getter
    @Setter
    public String conversion;

    @Getter
    @Setter
    public int resultCode;
}
