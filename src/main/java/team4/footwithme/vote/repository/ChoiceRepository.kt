package team4.footwithme.vote.repository

import org.springframework.data.jpa.repository.JpaRepository
import team4.footwithme.vote.domain.Choice

interface ChoiceRepository : JpaRepository<Choice?, Long?>, CustomChoiceRepository
