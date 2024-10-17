package team4.footwithme.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team4.footwithme.chat.domain.Chatroom;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {

    @Modifying
    @Query("update Chatroom c set c.isDeleted = 'true' where c.chatroomId = :id")
    void deleteById(@Param("id") Long chatroomId);

    @Query("select c from Chatroom c where c.isDeleted = 'false' and c.chatroomId = :id")
    Optional<Chatroom> findByChatroomId(@Param("id") Long chatroomId);

    @Query("select c from TeamChatroom c where c.isDeleted = 'false' and c.teamId = :id")
    Optional<Chatroom> findByTeamId(@Param("id") Long teamId);

    @Query("select c from ReservationChatroom c where c.isDeleted = 'false' and c.reservationId = :id")
    Optional<Chatroom> findByReservationId(@Param("id") Long reservationId);

    @Query("select c from Chatroom c where c.isDeleted = 'false'")
    List<Chatroom> findAll();
}
