package project.board.configuration;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {

        String msg = "로그인 실패. 다시 로그인해주세요.";

        if (exception instanceof InternalAuthenticationServiceException) {
            msg = exception.getMessage();
        }

        setUseForward(true);
        setDefaultFailureUrl("/member/login?error=true");
        request.setAttribute("errorMessage", msg);

        super.onAuthenticationFailure(request, response, exception);

    }

}
