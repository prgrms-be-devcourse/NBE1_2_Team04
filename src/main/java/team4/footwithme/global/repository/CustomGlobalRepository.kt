package team4.footwithme.global.repository

import java.util.*

interface CustomGlobalRepository<T> {
    fun findActiveById(id: Long?): Optional<T>
}
