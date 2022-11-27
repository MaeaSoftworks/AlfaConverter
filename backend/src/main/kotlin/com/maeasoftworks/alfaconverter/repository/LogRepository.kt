package com.maeasoftworks.alfaconverter.repository

import com.maeasoftworks.alfaconverter.dao.Log
import org.springframework.data.jpa.repository.JpaRepository

interface LogRepository : JpaRepository<Log, Long>