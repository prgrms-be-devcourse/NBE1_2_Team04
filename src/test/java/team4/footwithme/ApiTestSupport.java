package team4.footwithme;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import team4.footwithme.member.jwt.JwtTokenFilter;
import team4.footwithme.vote.api.VoteApi;
import team4.footwithme.vote.service.VoteService;

@WebMvcTest(controllers = {
    VoteApi.class
})
public abstract class ApiTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected VoteService voteService;

    @MockBean
    protected JwtTokenFilter jwtTokenFilter;

    // MockBean 통해서 실제 빈을 대체하는 가짜 빈을 주입
    // 사용하는 서비스들은 모두 MockBean으로 주입
    /*
    * ex)
    * @MockBean
    * private ProductService productService;
     */

}
