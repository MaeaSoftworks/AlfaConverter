package com.maeasoftworks.alfaconverterapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LiteDocument(List<String> headers, @JsonProperty("first-line") List<String> firstLine) {}