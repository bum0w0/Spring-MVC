package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

// SLF4J는 인터페이스, 그 구현체로 Logback 같은 로그 라이브러리를 사용
@RestController
// @RestController 는 반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());

    // method 속성으로 HTTP 메서드를 지정하지 않으면 HTTP 메서드와 무관하게 호출 (모든 요청에 대해 허용)
    // 메서드 매핑 축약으로 (@GetMapping 어노테이션 등) 직관적이게 작성하자.
    @RequestMapping(value = "/hello-basic", method = RequestMethod.GET)
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }

    // URL 경로의 특정 값을 템플릿 형식으로 지정할 수 있으며, 이 값을 @PathVariable(경로 변수)을 사용하여 메서드 파라미터로 추출할 수 있다.
    // 변수명이 같으면 생략 가능 : @PathVariable("userId") String userId -> @PathVariable String userId
    @GetMapping("/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }

}
