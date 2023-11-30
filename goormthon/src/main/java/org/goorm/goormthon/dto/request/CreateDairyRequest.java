package org.goorm.goormthon.dto.request;

public record CreateDairyRequest(
        Double latitude,
        Double longitude,
        String location,
        String keyword
) {
}
