package org.goorm.goormthon.service;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.goorm.goormthon.domain.Dairy;
import org.goorm.goormthon.dto.request.CreateDairyRequest;
import org.goorm.goormthon.dto.request.DairyContentRequest;
import org.goorm.goormthon.dto.request.DairyKeywordRequest;
import org.goorm.goormthon.dto.request.MessageAIRequest;
import org.goorm.goormthon.dto.response.AllDairyResponse;
import org.goorm.goormthon.dto.response.NewDairyResponse;
import org.goorm.goormthon.dto.response.newContent;
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

        return new NewDairyResponse(
                newDairy.getId(),
                newDairy.getLatitude(),
                newDairy.getLongitude(),
                newDairy.getRoadAddress(),
                newDairy.getLocation(),
                newDairy.getKeyword(),
                newDairy.getDairyContent(),
                newDairy.getDate()
        );
    }

    public newContent updateDairyKeyword(Long dairyId, DairyKeywordRequest dairyKeywordRequest){
        Dairy updateDairy = dairyRepository.findByIdOrThrow(dairyId);

        String message = getChatResponse(MessageAIRequest.requestQuery(updateDairy.getLocation(), dairyKeywordRequest.keyword()));
        log.info("응답 message: {}", message);
        updateDairy.setKeyword(dairyKeywordRequest.keyword());
        updateDairy.setDairyContent(message);

        return new newContent(
                updateDairy.getId(),
                updateDairy.getDairyContent()
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

    public List<AllDairyResponse> getAllDairy(){
        List<Dairy> allDairyList = dairyRepository.findAll();

        return allDairyList.stream()
                .map(dairy -> new AllDairyResponse(
                        dairy.getId(),
                        dairy.getLatitude(),
                        dairy.getLongitude(),
                        dairy.getRoadAddress(),
                        dairy.getLocation(),
                        truncateDairyContent(dairy.getDairyContent()),
                        dairy.getKeyword(),
                        truncateDate(dairy.getDate())
                ))
                .collect(Collectors.toList());
    }

    public Long updateDairyContent(Long dairyId, DairyContentRequest dairyContentRequest){
        Dairy updateDairy = dairyRepository.findByIdOrThrow(dairyId);

        updateDairy.setDairyContent(dairyContentRequest.dairyContent());
        dairyRepository.save(updateDairy);

        return updateDairy.getId();
    }

    public AllDairyResponse detailDairy(Long dairyId){
        Dairy dairy = dairyRepository.findByIdOrThrow(dairyId);

        return new AllDairyResponse(
                dairy.getId(),
                dairy.getLatitude(),
                dairy.getLongitude(),
                dairy.getRoadAddress(),
                dairy.getLocation(),
                dairy.getDairyContent(),
                dairy.getKeyword(),
                dairy.getDate()
        );
    }


    private String extractKeywords(String keyword) {
        // 해쉬태그를 떼고 쉼표로 구분된 문자열로 반환
        return keyword.replaceAll("#", "").replace(" ", "");
    }

    private String truncateDairyContent(String content) {
        // 30자로 제한하고, 30자 이상이면 ...을 붙여 반환
        return content.length() > 30 ? content.substring(0, 30) + "..." : content;
    }

    private static String truncateDate(String date) {
        return date.substring(5, 10);
    }

    public String getChatResponse(String prompt) {
        return chatgptService.sendMessage(prompt);
    }
}
