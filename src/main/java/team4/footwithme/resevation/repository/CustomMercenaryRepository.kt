package team4.footwithme.resevation.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import team4.footwithme.resevation.domain.Mercenary

interface CustomMercenaryRepository {
    fun findAllToPage(pageable: Pageable): Page<Mercenary>
}
