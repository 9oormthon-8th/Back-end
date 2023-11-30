package org.goorm.goormthon.service;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.goorm.goormthon.domain.Dairy;
import org.goorm.goormthon.dto.request.CreateDairyRequest;
import org.goorm.goormthon.dto.request.MessageAIRequest;
import org.goorm.goormthon.dto.response.NewDairyResponse;
import org.goorm.goormthon.repository.DairyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DairyService {

    private final ChatgptService chatgptService;
    private final DairyRepository dairyRepository;


    public NewDairyResponse getNewDairy(CreateDairyRequest createDairyRequest) {

        String message = getChatResponse(MessageAIRequest.requestQuery(createDairyRequest.location(), createDairyRequest.keyword()));
        log.info("응답 message: {}", message);

        Dairy newDairy = createDairy(createDairyRequest, message);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String formattedDate = newDairy.getCreatedAt().format(formatter);

        return new NewDairyResponse(
                newDairy.getId(),
                newDairy.getLatitude(),
                newDairy.getLongitude(),
                newDairy.getLocation(),
                newDairy.getKeyword(),
                newDairy.getDairyContent(),
                formattedDate
        );
    }

    private Dairy createDairy(CreateDairyRequest createDairyRequest, String message) {

        Dairy newDairy = Dairy.builder()
                .createDairyRequest(createDairyRequest)
                .content(message)
                .build();

        dairyRepository.save(newDairy);

        return newDairy;
    }

    public String getChatResponse(String prompt) {
        return chatgptService.sendMessage(prompt);
    }


}
