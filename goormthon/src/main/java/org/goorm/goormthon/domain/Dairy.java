package org.goorm.goormthon.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.goorm.goormthon.dto.request.CreateDairyRequest;
import org.goorm.goormthon.global.common.BaseTimeEntity;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Dairy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;

    private Double longitude;

    private String location;

    private String keyword;

    private String dairyContent;

    @Builder
    public Dairy(CreateDairyRequest createDairyRequest, String content){
        this.latitude = createDairyRequest.latitude();
        this.longitude = createDairyRequest.longitude();
        this.location = createDairyRequest.location();
        this.keyword = createDairyRequest.keyword();
        this.dairyContent = content;
    }

    public void setDairyContent(String dairyContent) {
        this.dairyContent = dairyContent;
    }

    public void setKeyword(String keyword){
        this.keyword = keyword;
    }


}
