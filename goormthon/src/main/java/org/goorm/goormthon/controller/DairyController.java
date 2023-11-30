package org.goorm.goormthon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.goorm.goormthon.dto.request.CreateDairyRequest;
import org.goorm.goormthon.dto.response.NewDairyResponse;
import org.goorm.goormthon.global.common.BaseResponse;
import org.goorm.goormthon.global.common.SuccessCode;
import org.goorm.goormthon.service.DairyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dairy")
public class DairyController {

    private final DairyService dairyService;

    @PostMapping()
    public ResponseEntity<BaseResponse<?>> postDairy(@RequestBody CreateDairyRequest createDairyRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(SuccessCode.OK, dairyService.getNewDairy(createDairyRequest)));
    }

//    @GetMapping("/all")
//    public ResponseEntity<BaseResponse<?>> all

}
