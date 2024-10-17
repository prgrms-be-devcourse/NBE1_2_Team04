package team4.footwithme.chat.domain

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.SQLDelete
import team4.footwithme.global.domain.BaseEntity
import java.io.Serial
import java.io.Serializable

@Entity
@SQLDelete(sql = "UPDATE chatroom SET is_deleted = 'true' WHERE chatroom_id = ?")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
open class Chatroom : BaseEntity, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val chatroomId: Long? = null

    var name: @NotNull String? = null
        

    constructor(name: String?) {
        this.name = name
    }

    protected constructor()

    fun updateName(name: String?) {
        this.name = name
    }

    companion object {
        @Serial
        private val serialVersionUID = -6846388362402032476L

        @JvmStatic
        fun create(name: String?): Chatroom {
            return Chatroom(name)
        }
    }
}
