package team4.footwithme.chat.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import team4.footwithme.chat.domain.Chatroom
import java.util.*

@Repository
interface ChatroomRepository : JpaRepository<Chatroom?, Long?> {
    @Modifying
    @Query("update Chatroom c set c.isDeleted = 'true' where c.chatroomId = :id")
    override fun deleteById(@Param("id") chatroomId: Long)

    @Query("select c from Chatroom c where c.isDeleted = 'false' and c.chatroomId = :id")
    fun findByChatroomId(@Param("id") chatroomId: Long?): Optional<Chatroom?>

    @Query("select c from TeamChatroom c where c.isDeleted = 'false' and c.teamId = :id")
    fun findByTeamId(@Param("id") teamId: Long?): Optional<Chatroom?>

    @Query("select c from ReservationChatroom c where c.isDeleted = 'false' and c.reservationId = :id")
    fun findByReservationId(@Param("id") reservationId: Long?): Optional<Chatroom?>

    @Query("select c from Chatroom c where c.isDeleted = 'false'")
    override fun findAll(): List<Chatroom?>
}
