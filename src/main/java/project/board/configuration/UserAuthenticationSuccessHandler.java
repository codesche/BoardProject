package project.board.configuration;


import java.io.IOException;
import java.net.InetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import project.board.BoardApplication;
import project.board.member.model.LoginHistoryInput;
import project.board.member.service.MemberService;

@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication)
        throws IOException, ServletException {

        String userAgent = request.getHeader("user-agent");
        String clientIp = request.getHeader("X-FORWARDED-FOR");

        // clientIp 얻기
        if (clientIp == null) {
            clientIp = request.getHeader("X-FORWARDED-FOR");
        }

        if (clientIp == null) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }

        if (clientIp == null) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }

        if (clientIp == null) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }

        if (clientIp == null) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (clientIp == null) {
            clientIp = request.getRemoteAddr();
        }

        if (clientIp.equals("0:0:0:0:0:0:0:1") || clientIp.equals("127.0.0.1")) {
            InetAddress address = InetAddress.getLocalHost();
            clientIp = address.getHostAddress();
        }

        LoginHistoryInput loginHistoryInput = new LoginHistoryInput();
        loginHistoryInput.setUserId(authentication.getName());
        loginHistoryInput.setClientIp(clientIp);
        loginHistoryInput.setUserAgent(userAgent);

        memberService.recordLoginHistory(loginHistoryInput);

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
