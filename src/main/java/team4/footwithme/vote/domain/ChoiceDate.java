package team4.footwithme.vote.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class ChoiceDate extends Choice {

    @NotNull
    private LocalDateTime endAt;

}
