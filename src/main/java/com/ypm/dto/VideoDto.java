package com.ypm.dto;

import java.util.List;

public record VideoDto(
    String id,
    String title,
    String description,
    List<String> tags,
    String channelName) {
}
