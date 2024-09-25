package team4.footwithme;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

// WebMvcTest 어노테이션을 사용하면 스프링 MVC를 테스트하기 위한 필요한 빈들만 등록 Controller들만 등록
/*
 * Ex)
 * @WebMvcTest(controllers = {
 *   ProductController.class,
 */
@WebMvcTest(controllers = {
})
public abstract class ApiTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    // MockBean 통해서 실제 빈을 대체하는 가짜 빈을 주입
    // 사용하는 서비스들은 모두 MockBean으로 주입
    /*
    * ex)
    * @MockBean
    * private ProductService productService;
     */

}
