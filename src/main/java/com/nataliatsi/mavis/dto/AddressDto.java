package com.nataliatsi.mavis.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressDto(
        @NotBlank(message = "Rua é obrigatória")
        @Size(max = 100, message = "Rua pode ter no máximo 100 caracteres")
        String street,

        @NotNull(message = "Número é obrigatório")
        @Size(max = 10, message = "Número pode ter no máximo 10 caracteres")
        String number,

        @NotBlank(message = "Bairro é obrigatório")
        @Size(max = 50, message = "Bairro pode ter no máximo 50 caracteres")
        String neighborhood,

        @NotBlank(message = "Cidade é obrigatória")
        @Size(max = 50, message = "Cidade pode ter no máximo 50 caracteres")
        String city,

        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
        String state,

        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "CEP deve estar no formato 12345-678")
        String postalCode,

        String referencePoint
) {
}
