package team4.footwithme.member.oauth2

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User
import team4.footwithme.member.domain.Member

class CustomOAuth2UserDetails(val member: Member?, private val attributes: Map<String, Any>) : UserDetails, OAuth2User {
    override fun getAttributes(): Map<String, Any> {
        return attributes
    }

    override fun getName(): String {
        return member!!.name!!
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val collection: MutableCollection<GrantedAuthority> = ArrayList()
        collection.add(GrantedAuthority { member!!.memberRole!!.text })

        return collection
    }

    override fun getPassword(): String {
        return member!!.password!!
    }

    override fun getUsername(): String {
        return member!!.email!!
    }
}
