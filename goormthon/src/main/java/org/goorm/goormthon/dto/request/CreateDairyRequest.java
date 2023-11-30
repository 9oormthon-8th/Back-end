package org.goorm.goormthon.dto.request;

public record CreateDairyRequest(
        Double latitude,
        Double longitude,
        String roadAddress,
        String location,
        String keyword,
        String date
) {
}
