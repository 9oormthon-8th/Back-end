package org.goorm.goormthon.dto.response;

public record AllDairyResponse(

        Double latitude,
        Double longitude,
        String location,
        String dairyContent,
        String keyword,
        String writeDate
) {
}
