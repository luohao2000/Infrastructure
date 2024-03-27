package com.itiaoling.starter.message;

import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author charles
 */
public class MessageSourceHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSourceHandler.class);

    /**
     * 消息源
     */
    private final MessageSource messageSource;
    /**
     * 默认语言
     */
    private final String defaultLocale;

    public MessageSourceHandler(MessageSource messageSource, String defaultLocale) {
        this.messageSource = messageSource;
        this.defaultLocale = defaultLocale;
    }

    /**
     * 正常 local zh_CN
     */
    public static final String LANGUAGE_REGEX = "_";

    /**
     * locale lookup 使用 zh-CN
     */
    public static final String LANGUAGE_REPLACEMENT = "-";

    /**
     * 多语言格式化
     *
     * @param key  语言 key
     * @param args 参数
     * @return 格式化后的语言
     */
    public String format(Integer key, Object... args) {
        Assert.notNull(key, "language key could not be null!");
        return format(key.toString(), args);
    }

    /**
     * 多语言格式化
     *
     * @param key  语言 key
     * @param args 参数
     * @return 格式化后的语言
     */
    public String format(String key, Object... args) {
        Assert.hasLength(key, "language key could not be null!");
        Locale contextLocale = getContextLocale();
        return format(key, contextLocale, args);
    }

    /**
     * 多语言格式化
     *
     * @param key    语言 key
     * @param locale 语言
     * @param args   参数
     * @return 格式化后的语言
     */
    public String format(String key, Locale locale, Object... args) {
        Assert.notNull(key, "language key could not be null!");
        Assert.notNull(locale, "locale could not be null!");
        try {
            return messageSource.getMessage(key, args, locale);
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error("get message error", e);
            } else {
                LOGGER.error("get message error {}", e.getMessage());
            }
            return key;
        }
    }

    /**
     * 获取上下文 locale
     */
    public Locale getContextLocale() {
        return LocaleContextHolder.getLocale();
    }

    /**
     * 获取上下文语言
     *
     * @return 标准语言
     */
    public String getContextLanguage() {
        return getContextLocale().toString();
    }

    /**
     * 获得并处理目前上下文地区语言信息
     *
     * @param language 原始语言串 en_US / en
     * @return 地区信息
     */
    public Locale getLocale(String language) {
        language = language.replaceAll(LANGUAGE_REGEX, LANGUAGE_REPLACEMENT);

        List<Locale> locales = toLocales(language);
        locales.sort(Comparator.comparing(Locale::getLanguage).reversed());
        Locale locale = locales.iterator().next();

        if (locale == Locale.CHINESE) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (locale == Locale.GERMAN) {
            return Locale.GERMANY;
        } else if (locale == Locale.ENGLISH) {
            return Locale.US;
        } else {
            return locale;
        }
    }

    /**
     * 将 en_US / en 转化为 locale 对象集合
     *
     * @param language 语言串 en_US / en
     * @return locale 对象集合
     */
    public List<Locale> toLocales(String language) {
        language = language.replaceAll(LANGUAGE_REGEX, LANGUAGE_REPLACEMENT);

        List<Locale> availableLocales = Arrays.asList(Locale.getAvailableLocales());
        List<Locale.LanguageRange> parse = Locale.LanguageRange.parse(language);
        List<Locale> locales = new ArrayList<>();
        for (Locale.LanguageRange item : parse) {
            Locale lookup = Locale.lookup(Collections.singletonList(item), availableLocales);
            if (lookup != null) {
                locales.add(lookup);
            }
        }

        if (CollectionUtils.isEmpty(locales) && StringUtils.isNotEmpty(defaultLocale)) {
            Locale locale = forLanguage(defaultLocale);
            if (locale != null) {
                locales.add(locale);
            }
        }
        return locales;
    }

    /**
     * 语言字符串转为语言地址
     * 将范围第一个有效信息
     *
     * @param language 原始原串
     * @return 语言地址
     */
    public static Locale forLanguage(String language) {
        language = language.replaceAll(LANGUAGE_REGEX, LANGUAGE_REPLACEMENT);
        List<Locale> availableLocales = Arrays.stream(Locale.getAvailableLocales()).collect(Collectors.toList());
        List<Locale.LanguageRange> parse = Locale.LanguageRange.parse(language);
        return Locale.lookup(parse, availableLocales);
    }
}
