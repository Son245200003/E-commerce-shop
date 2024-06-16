package com.project.shopapp.component;

import com.project.shopapp.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class LocalizationUtils {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    public String getLocalizationMessage(String messageKey){
        HttpServletRequest request= WebUtils.getCurrentRequest();
        Locale locale=localeResolver.resolveLocale(request);
        return messageSource.getMessage(messageKey,null,locale);
    }
}
