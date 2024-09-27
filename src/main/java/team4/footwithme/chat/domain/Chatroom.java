package team4.footwithme.chat.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import team4.footwithme.global.domain.BaseEntity;

import java.io.Serial;
import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE chatroom SET is_deleted = 'true' WHERE chatroom_id = ?")
public class Chatroom extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -6846388362402032476L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomId;

    private String name;

    @Builder
    private Chatroom(String name) {
        this.name = name;
    }

    public static Chatroom create(String name) {
        return Chatroom.builder()
            .name(name)
            .build();
    }
}
