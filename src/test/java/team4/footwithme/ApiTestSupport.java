package team4.footwithme;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import team4.footwithme.chat.api.ChatApi;
import team4.footwithme.chat.api.ChatMemberApi;
import team4.footwithme.chat.api.ChatroomApi;
import team4.footwithme.chat.service.ChatMemberService;
import team4.footwithme.chat.service.ChatService;
import team4.footwithme.chat.service.ChatroomService;
import team4.footwithme.member.api.MemberApi;
import team4.footwithme.member.jwt.JwtTokenFilter;
import team4.footwithme.member.service.CookieService;
import team4.footwithme.member.service.MemberService;
import team4.footwithme.resevation.api.GameApi;
import team4.footwithme.resevation.api.MercenaryApi;
import team4.footwithme.resevation.api.ParticipantApi;
import team4.footwithme.resevation.api.ReservationApi;
import team4.footwithme.resevation.service.GameService;
import team4.footwithme.resevation.service.MercenaryService;
import team4.footwithme.resevation.service.ParticipantService;
import team4.footwithme.resevation.service.ReservationService;
import team4.footwithme.stadium.api.CourtMerchantApi;
import team4.footwithme.stadium.api.StadiumMerchantApi;
import team4.footwithme.stadium.service.CourtService;
import team4.footwithme.stadium.service.StadiumService;
import team4.footwithme.vote.api.VoteApi;
import team4.footwithme.vote.service.VoteService;

@WebMvcTest(controllers = {
    VoteApi.class, ChatApi.class, ChatroomApi.class, ChatMemberApi.class, StadiumMerchantApi.class, CourtMerchantApi.class, GameApi.class, ReservationApi.class, MemberApi.class, MercenaryApi.class, ParticipantApi.class
})
@AutoConfigureMockMvc(addFilters = false)
public abstract class ApiTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected VoteService voteService;

    @MockBean
    protected ChatService chatService;

    @MockBean
    protected ChatroomService chatroomService;

    @MockBean
    protected ChatMemberService chatMemberService;

    @MockBean
    protected MercenaryService mercenaryService;

    @MockBean
    protected ParticipantService participantService;

    @MockBean
    protected JwtTokenFilter jwtTokenFilter;

    @MockBean
    protected CourtService courtService;

    @MockBean
    protected StadiumService stadiumService;

    @MockBean
    protected GameService gameService;

    @MockBean
    protected ReservationService reservationService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected CookieService cookieService;

}
