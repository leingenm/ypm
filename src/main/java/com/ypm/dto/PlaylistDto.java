package com.ypm.dto;

import java.util.Optional;

public record PlaylistDto(String title,
                          Optional<String> description,
                          String status) {
}
