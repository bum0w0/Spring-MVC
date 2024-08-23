package hello.springmvc.basic.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        response.getWriter().write("ok");
    }

    // 스프링 MVC는 다음 파라미터를 지원한다.
    // InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
    // OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");
    }



/*
     스프링 MVC는 다음 파라미터를 지원한다.

     HttpEntity : HTTP header, body 정보를 편리하게 조회
     메시지 바디 정보를 직접 조회
     요청 파라미터를 조회하는 기능과 관계 없음 @RequestParam X, @ModelAttribute X

     HttpEntity는 응답에도 사용 가능
     메시지 바디 정보 직접 반환
     헤더 정보 포함 가능
     view 조회X
*/
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {
        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);
        return new HttpEntity<>("ok");
    }

    // @RequestBody를 사용하여 HTTP 메시지 본문을 편리하게 조회하고, 이를 String messageBody에 저장함
    // @ResponseBody를 사용하여 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달 (view 조회 x)
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) {
        log.info("messageBody={}", messageBody);
        return "ok";
    }
}
