package team4.footwithme.chat.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public class Chatroom extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -6846388362402032476L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomId;

    @NotNull
    private String name;

    public Chatroom(String name) {
        this.name = name;
    }

    public static Chatroom create(String name) {
        return new Chatroom(name);
    }

    public void updateName(String name) {
        this.name = name;
    }
}
