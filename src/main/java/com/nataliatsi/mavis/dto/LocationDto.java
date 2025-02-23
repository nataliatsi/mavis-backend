package com.nataliatsi.mavis.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public record LocationDto(
        @DecimalMin(value = "-90.0", message = "A latitude deve ser maior ou igual a -90")
        @DecimalMax(value = "90.0", message = "A latitude deve ser menor ou igual a 90")
        double latitude,

        @DecimalMin(value = "-180.0", message = "A longitude deve ser maior ou igual a -180")
        @DecimalMax(value = "180.0", message = "A longitude deve ser menor ou igual a 180")
        double longitude
) {
}
