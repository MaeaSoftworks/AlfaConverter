package com.maeasoftworks.alfaconverterapi.services;

import com.maeasoftworks.alfaconverterapi.dao.Log;
import com.maeasoftworks.alfaconverterapi.repository.LogRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class Logger {
    private final LogRepository logRepository;

    public Logger(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Transactional
    public void write(Log log) {
        logRepository.save(log);
    }
}
