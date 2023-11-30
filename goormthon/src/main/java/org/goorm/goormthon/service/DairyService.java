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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String formattedDate = newDairy.getCreatedAt().format(formatter);

        return new NewDairyResponse(
                newDairy.getId(),
                newDairy.getLatitude(),
                newDairy.getLongitude(),
                newDairy.getLocation(),
                extractKeywords(newDairy.getKeyword()),
                newDairy.getDairyContent(),
                formattedDate
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd");

        return allDairyList.stream()
                .map(dairy -> new AllDairyResponse(
                        dairy.getId(),
                        dairy.getLatitude(),
                        dairy.getLongitude(),
                        dairy.getLocation(),
                        truncateDairyContent(dairy.getDairyContent()),
                        extractKeywords(dairy.getKeyword()),
                        dairy.getCreatedAt().format(formatter)
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String formattedDate = dairy.getCreatedAt().format(formatter);

        return new AllDairyResponse(
                dairy.getId(),
                dairy.getLatitude(),
                dairy.getLongitude(),
                dairy.getLocation(),
                dairy.getDairyContent(),
                extractKeywords(dairy.getKeyword()),
                formattedDate
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

    public String getChatResponse(String prompt) {
        return chatgptService.sendMessage(prompt);
    }
}
