package com.ypm.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record PlaylistDto(@NotBlank String title,
                          Optional<String> description,
                          @NotBlank String status) {
}
