package ru.dorofeev.security.session.strategy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Component;
import ru.dorofeev.SecurityProperties;
import ru.dorofeev.security.session.utils.SessionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SessionLimitControlStrategy implements SessionAuthenticationStrategy {

    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
    private final SecurityProperties securityProperties;

    @Override
    public void onAuthentication(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        String username = authentication.getName();
        String rqChannel = SessionUtils.getChannelByUserAgent(request).name();

        String rqChannelAttributeName = securityProperties.getSession()
                .getLimitPerChannel()
                .getAttributeName();

        Map<String, Integer> limitMap = securityProperties.getSession()
                .getLimitPerChannel()
                .getLimitMap();

        Map<String, ? extends Session> userSessions = sessionRepository
                .findByPrincipalName(username);

        List<Session> channelSessions = userSessions.values().stream()
                .filter(s -> rqChannel.equals(s.getAttribute(rqChannelAttributeName)))
                .sorted(Comparator.comparing(Session::getCreationTime).reversed())
                .collect(Collectors.toList());

        if (channelSessions.size() >= limitMap.get(rqChannel)) {
            channelSessions.forEach(session -> sessionRepository.deleteById(session.getId()));
        }

        request.getSession()
                .setAttribute(rqChannelAttributeName, rqChannel);
    }
}
