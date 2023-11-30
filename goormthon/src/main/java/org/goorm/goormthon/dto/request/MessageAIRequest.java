package org.goorm.goormthon.dto.request;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class MessageAIRequest {

    public static String requestQuery(String location, String keyword) {
        return String.format(QueryTemplate.WRITING_DIARY.value, location, keyword);
    }

    enum QueryTemplate {
        WRITING_DIARY("너는 간편한 여행 기록을 도와줄 서비스야. 아래 키워드를 기반으로 150자 정도로 일기 적어줘.\n"
                + "\n\n%s" + " " + "%s");

        private final String value;

        QueryTemplate(String value) {
            this.value = value;
        }
    }
}
