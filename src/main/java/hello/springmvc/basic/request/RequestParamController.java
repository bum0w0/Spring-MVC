package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String age = request.getParameter("age");
        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");
    }

    @ResponseBody // @ResponseBody 추가 : View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
    @RequestMapping("/request-param-v2")
    public String requestParamV2(@RequestParam("username") String memberName, @RequestParam("age") int memberAge) {
        log.info("username={}, age={}", memberName, memberAge);

        return "ok";
    }

    // HTTP 파라미터 이름이 변수 이름과 같으면 생략 가능 (Ex. @RequestParam String username)
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(@RequestParam String username, @RequestParam int age) {
        log.info("username={}, age={}", username, age);

        return "ok";
    }

    // String, int, Integer 등 단순 타입이면 @RequestParam 도 생략이 가능하지만 요청 파라미터에서 데이터를 읽는 것을 명시하는게 좋을 수 있다.
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username, age);

        return "ok";
    }

    // 파라미터 필수 여부 (required = true 가 기본값, 요청 파라미터가 필수)
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            // 기본형(primitive)에 null 입력이 불가능하니 Integer로 변경 후 테스트
            @RequestParam(required = false) Integer age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    // 파라미터에 값이 없는 경우 defaultValue 를 사용하여 기본 값을 적용
    // defaultValue는 빈 문자의 경우에도 적용 ( /request-param-default?username=  는 null 이 아닌 빈 문자로 처리되어 defaultValue 값이 적용된다.)
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            // 이미 기본 값이 있기 때문에 required 는 의미가 없다.
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }

    // 파라미터를 Map, MultiValueMap으로 조회할 수 있다.
    // 파라미터의 값이 1개가 확실하다면 Map 을 사용해도 되지만, 그렇지 않다면 MultiValueMap 을 사용하자
    /*      @RequestParam Map,
                     Map(key=value)

            @RequestParam MultiValueMap,
                     MultiValueMap(key=[value1, value2])
                     Ex. ?userIds=id1&userIds=id2 (이런 경우는 많지는 않다.)  */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"),
                paramMap.get("age"));
        return "ok";
    }


    // 실제 개발을 하면 요청 파라미터를 받아서 필요한 객체를 만들고 그 객체에 값을 넣어주어야 한다. 스프링은 이 과정을 자동화해주는 @ModelAttribute 기능을 제공
    // model.addAttribute(helloData) 코드가 함께 자동 적용됨
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    // HelloData 객체가 생성되고, 이 때 요청 파라미터의 값도 모두 들어간다.
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }

    // @ModelAttribute는 생략 가능하지만 @RequestParam도 생략이 가능하여 혼란이 올 수 있다.
    // 스프링은 위와 같은 생략 상황이 오면 다음과 같은 규칙을 적용한다.
    // String, int, Integer 같은 단순 타입 = @RequestParam
    // 나머지 = @ModelAttribute (argument resolver 로 지정해둔 타입 외)
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }
}
