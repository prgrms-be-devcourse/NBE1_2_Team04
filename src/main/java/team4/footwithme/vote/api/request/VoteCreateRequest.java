package team4.footwithme.vote.api.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import team4.footwithme.vote.api.request.annotation.Duplicate;
import team4.footwithme.vote.service.request.VoteCreateServiceRequest;

import java.time.LocalDateTime;
import java.util.List;

public record VoteCreateRequest(
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 50, message = "제목은 50자 이하여야 합니다.")
    String title,
    @Future(message = "투표 종료 시간은 현재 시간보다 미래의 시간으로 지정해야합니다.")
    LocalDateTime endAt,
    @Size(min = 1, message = "구장 선택은 필수입니다.")
    @Duplicate(message = "중복된 구장은 포함 할 수 없습니다.")
    List<StadiumChoices> choices
) {
    public VoteCreateServiceRequest toServiceRequest() {
        return new VoteCreateServiceRequest(
            title,
            endAt,
            choices.stream()
                .map(StadiumChoices::stadiumId)
                .toList()
        );
    }
}
