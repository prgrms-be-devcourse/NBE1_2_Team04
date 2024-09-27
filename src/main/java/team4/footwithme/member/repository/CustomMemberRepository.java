package team4.footwithme.member.repository;

public interface CustomMemberRepository {

    Long findMemberIdByMemberEmail(String email);

}
