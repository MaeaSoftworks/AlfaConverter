package com.maeasoftworks.alfaconverter.services

import com.maeasoftworks.alfaconverter.dao.Log
import com.maeasoftworks.alfaconverter.repository.LogRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class Logger(private val logRepository: LogRepository) {
	@Transactional
	fun write(log: Log) {
		logRepository.save(log)
	}
}