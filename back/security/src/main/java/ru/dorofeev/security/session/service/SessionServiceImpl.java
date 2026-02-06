package ru.dorofeev.security.session.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;
import ru.dorofeev.security.authentication.exception.enums.ErrorType;
import ru.dorofeev.security.authentication.exception.SecurityException;
import ru.dorofeev.security.session.section.AbstractSection;
import ru.dorofeev.security.session.strategy.SessionLimitControlStrategy;
import ru.dorofeev.security.utils.HttpServletUtils;
import ru.dorofeev.security.utils.LoggerUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static ru.dorofeev.security.session.section.AbstractSection.SECTION_SESSION_PREFIX;


@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private static final String SPRING_SECURITY_CONTEXT_VALUE = "SPRING_SECURITY_CONTEXT";
    private static final String SECTION_NAMES_ATTRIBUTE_VALUE = "SECTION_NAMES";
    private static final boolean CREATE_NEW_SESSION_IF_NOT_EXIST = true;

    private final SessionLimitControlStrategy sessionLimitControlStrategy;

    private final FindByIndexNameSessionRepository<? extends Session> repository;
    private final ApplicationContext context;

    @Override
    public HttpSession getCurrentHttpSession() {
        HttpSession currentSession = HttpServletUtils.getCurrentHttpRequest()
                .getSession(!CREATE_NEW_SESSION_IF_NOT_EXIST);

        if (Objects.isNull(currentSession)) {
            log.warn("Попытка обратиться к несуществующей сессии!");
            throw new SecurityException(ErrorType.SESSION_NOT_FOUND);
        }

        return currentSession;
    }

    @Override
    public String getSessionId() {
        return getCurrentHttpSession().getId();
    }

    @Override
    public Collection<? extends Session> getAllSessions() {
        return repository.findByPrincipalName(HttpServletUtils.getCurrentPrincipalName()).values();
    }

    @Override
    public void removeAllSessions() {
        String userEmail = HttpServletUtils.getCurrentPrincipalName();

        repository.findByPrincipalName(userEmail)
                .keySet()
                .forEach(repository::deleteById);
        log.debug("Удаление всех сессий для пользователя: '{}'", userEmail);
    }

    @Override
    public void removeSession(String sessionIdToDelete) {
        String userEmail = HttpServletUtils.getCurrentPrincipalName();

        Set<String> usersSessionIds = repository.findByPrincipalName(userEmail).keySet();
        if (usersSessionIds.contains(sessionIdToDelete)) {
            repository.deleteById(sessionIdToDelete);
            log.debug("Удаление сессии с ID: '{}' для пользователя: '{}'",
                    sessionIdToDelete, userEmail
            );
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getAllSectionNames() {
        Map<String, Object> sessionAttributes = getSessionAttributes();

        if (MapUtils.isEmpty(sessionAttributes)) {
            return Collections.emptyList();
        }

        Object sections = sessionAttributes.get(SECTION_NAMES_ATTRIBUTE_VALUE);
        return Objects.nonNull(sections) ? (List<String>) sections : Collections.emptyList();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <T extends Enum<T>> Object getSectionAttribute(@NotNull AbstractSection section, @NotNull T attributeName) {
        Map<String, Object> sessionAttributes = getSessionAttributes();

        Map<String, Object> sectionData = (Map<String, Object>) sessionAttributes.get(SECTION_SESSION_PREFIX + section.getName());
        if (MapUtils.isEmpty(sectionData)) {
            log.debug("Получена пустая секция: '{}'!", section.getName());
            return null;
        }

        return sectionData.get(attributeName.name());
    }

    @Override
    public Map<String, Object> getSessionAttributes() {
        return Optional.ofNullable(repository.findById(getSessionId()))
                .map(this::extractAttributes)
                .orElseGet(Collections::emptyMap);
    }

    @Override
    public void initialSessionContext(@NotNull Authentication authentication, @NotNull Map<String, Object> rootAttributes) {
        HttpServletRequest currentRequest = HttpServletUtils.getCurrentHttpRequest();

        HttpSession oldSession = currentRequest.getSession(!CREATE_NEW_SESSION_IF_NOT_EXIST);
        if (Objects.nonNull(oldSession)) {
            oldSession.invalidate();
        }

        HttpSession newSession = currentRequest.getSession(CREATE_NEW_SESSION_IF_NOT_EXIST);
        newSession.setAttribute(SPRING_SECURITY_CONTEXT_VALUE, SecurityContextHolder.getContext());

        List<String> allSectionNames = new ArrayList<>();
        @SuppressWarnings("rawtypes")
        Map<String, AbstractSection> allSection = context.getBeansOfType(AbstractSection.class);
        allSection.values().forEach(section -> {
                    String sectionName = SECTION_SESSION_PREFIX + section.getName();
                    newSession.setAttribute(sectionName, section.getAttributes());
                    allSectionNames.add(sectionName);
                }
        );
        newSession.setAttribute(SECTION_NAMES_ATTRIBUTE_VALUE, allSectionNames);
        rootAttributes.forEach(newSession::setAttribute);

        sessionLimitControlStrategy.onAuthentication(
                authentication,
                HttpServletUtils.getCurrentHttpRequest(),
                HttpServletUtils.getCurrentHttpResponse()
        );
    }

    @Override
    public void invalidateSession() {
        HttpSession currentSession = getCurrentHttpSession();
        currentSession.invalidate();
        LoggerUtils.debugLazy(
                () -> "Уничтожение сессии с ID: '%s' по пользователю: %s"
                        .formatted(
                                currentSession.getId(),
                                HttpServletUtils.getCurrentPrincipalName()
                        )
        );
    }

    /**
     * Метод получения атрибутов сессии.
     *
     * @param session сессия пользователя.
     * @return [Ключ атрибута, Значение атрибута].
     */
    private Map<String, Object> extractAttributes(Session session) {
        Map<String, Object> attributes = new HashMap<>();
        session.getAttributeNames()
                .forEach(name -> attributes.put(name, session.getAttribute(name)));
        return attributes;
    }
}
