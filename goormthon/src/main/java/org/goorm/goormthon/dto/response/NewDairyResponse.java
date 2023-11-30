package org.goorm.goormthon.dto.response;

import java.time.LocalDateTime;

public record NewDairyResponse(
        Long id,
        Double latitude,
        Double longitude,
        String roadAddress,
        String location,
        String keyword,
        String dairyContent,
        String date

) {
}
