package ba.unsa.etf.nwt.pet_category_service.security;

import ba.unsa.etf.nwt.pet_category_service.response.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        logger.error("Responding with unauthorized error. Message - {}", e.getMessage());
        //httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        List<String> details = new ArrayList<>();
        details.add("This route is authorized and requires login!");

        final Map<String, Object> body = new HashMap<>();
        body.put("responseMessage", new ResponseMessage(false, HttpStatus.UNAUTHORIZED, "Unauthorized request!"));
        body.put("details", details);

        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(httpServletResponse.getOutputStream(), body);
    }
}
