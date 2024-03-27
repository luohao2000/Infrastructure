package com.itiaoling.starter.filter;

import com.itiaoling.log.date.DateUtil;
import com.itiaoling.metric.ExceptionUtils;
import com.itiaoling.metric.constants.TagName;
import com.itiaoling.metric.constants.TagValue;
import com.itiaoling.metric.spec.Monitor;
import com.itiaoling.starter.properties.AppProperties;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * monitor 过滤器
 *
 * @author charles
 * @since 2024/1/10
 */
public class MonitorFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(MonitorFilter.class);

    private final Monitor monitor;

    private final AppProperties appProperties;

    public MonitorFilter(Monitor monitor, AppProperties appProperties) {
        this.monitor = monitor;
        this.appProperties = appProperties;
    }

    /**
     * 是否记录请求日志
     */
    private boolean needLogRequest = true;

    /**
     * 是否记录响应日志
     */
    private boolean needLogResponse = true;

    /**
     * 是否记录header
     */
    private boolean needLogHeader = true;

    /**
     * 是否记录参数
     */
    private boolean needLogPayload = true;

    /**
     * 记录的最大payload大小
     */
    private int maxPayloadLength = 2 * 1024 * 1024;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 不进行过滤的请求pattern 例如：/actuator/health,/actuator/info
     * 后续可以走配置
     */
    private static final Set<String> EXCLUDE_URL = new HashSet<>();

    static {
        // 监控请求
        EXCLUDE_URL.add("/actuator/**");
        // swagger
        EXCLUDE_URL.add("/doc.html");
        EXCLUDE_URL.add("/v2/api-docs");
        EXCLUDE_URL.add("/swagger-resources/**");
        // 静态文件
        EXCLUDE_URL.add("/**/*.js");
        EXCLUDE_URL.add("/**/*.css");
        EXCLUDE_URL.add("/**/*.gif");
        EXCLUDE_URL.add("/**/*.jpg");
        EXCLUDE_URL.add("/**/*.png");
        EXCLUDE_URL.add("/**/*.ico");
        EXCLUDE_URL.add("/**/*.woff");
        EXCLUDE_URL.add("/**/*.woff2");
        EXCLUDE_URL.add("/**/*.ttf");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String url = request.getServletPath();
        boolean matched = false;
        // 静态资源与指定资源会直接放行
        for (String pattern : EXCLUDE_URL) {
            matched = antPathMatcher.match(pattern, url);
            if (matched) {
                break;
            }
        }

        return matched;
    }

    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request     current HTTP request
     * @param response    current HTTP response
     * @param filterChain current filter chain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Date requestDate = new Date();
        long current = System.currentTimeMillis();
        boolean isFirstRequest = !isAsyncDispatch(request);

        // 包装缓存requestBody信息
        HttpServletRequest requestToUse = request;
        if (isNeedLogPayload() && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request, getMaxPayloadLength());
        }

        // 包装缓存responseBody信息
        HttpServletResponse responseToUse = response;
        if (isNeedLogPayload() && !(response instanceof ContentCachingResponseWrapper)) {
            responseToUse = new ContentCachingResponseWrapper(response);
        }

        String requestId = UUID.randomUUID().toString();
        Throwable e = null;
        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } catch (Throwable throwable) {
            e = throwable;
            throw throwable;
        } finally {

            // 记录请求日志
            if (isNeedLogRequest()) {
                logRequest(requestToUse, requestDate, requestId);
            }

            // 上报监控
            monitorResponse(request, responseToUse, System.currentTimeMillis() - current, e);


            // 记录响应日志
            if (isNeedLogResponse()) {
                logResponse(responseToUse, requestDate, requestId, request.getRequestURI());
                // 把从response中读取过的内容重新放回response，否则客户端获取不到返回的数据
                resetResponse(responseToUse);
            }

        }
    }

    /**
     * 上报监控
     *
     * @param request       请求
     * @param responseToUse 响应
     * @param duration      消耗时间
     * @param e             异常
     */
    private void monitorResponse(HttpServletRequest request, HttpServletResponse responseToUse, long duration, Throwable e) {
        try {
            List<Tag> tagList = new ArrayList<>();
            tagList.add(new ImmutableTag(TagName.SOURCE, TagValue.SOURCE_WEB_FILTER));
            tagList.add(new ImmutableTag(TagName.HTTP_METHOD, Optional.ofNullable(request.getMethod()).orElse("unknownHttpMethod")));
            tagList.add(new ImmutableTag(TagName.HTTP_STATUS, String.valueOf(responseToUse.getStatus())));
            tagList.add(new ImmutableTag(TagName.URL, Optional.ofNullable(request.getRequestURI()).orElse("unknown")));
            if (e == null) {
                String payload = getResponsePayload(responseToUse);
                tagList.add(new ImmutableTag(TagName.BIZ_CODE, ExceptionUtils.getResultCode(payload)));
            } else {
                tagList.add(new ImmutableTag(TagName.BIZ_CODE, ExceptionUtils.getExceptionCode(e)));
            }

            tagList.add(new ImmutableTag(TagName.PRODUCT_LINE, Optional.ofNullable(appProperties.getProductLine()).orElse("unknownProductLine")));
            tagList.add(new ImmutableTag(TagName.DATA_CENTER, Optional.ofNullable(appProperties.getDataCenter()).orElse("unknownDataCenter")));
            monitor.record(tagList, duration, TimeUnit.MILLISECONDS);
        } catch (Exception ne) {
            LOG.warn("monitorResponse error", ne);
        }
    }

    /**
     * 记录请求日志
     *
     * @param request     请求
     * @param requestDate 请求时间
     */
    protected void logRequest(HttpServletRequest request, Date requestDate, String requestId) {
        String payload = isNeedLogPayload() ? getRequestPayload(request) : "";
        LOG.info(createRequestMessage(request, payload, requestDate, requestId));
    }

    /**
     * 记录响应日志
     *
     * @param response 响应
     */
    protected void logResponse(HttpServletResponse response, Date requestDate, String requestId, String requestUrl) {
        String payload = isNeedLogPayload() ? getResponsePayload(response) : "";
        LOG.info(createResponseMessage(response, payload, new Date(), requestDate, requestId, requestUrl));
    }

    /**
     * 重新将响应参数设置到response中
     *
     * @param response 响应
     * @throws IOException 异常
     */
    protected void resetResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            wrapper.copyBodyToResponse();
        }
    }

    /**
     * 获取请求体中参数
     *
     * @param request 请求
     * @return 请求体中参数
     */
    protected String getRequestPayload(HttpServletRequest request) {
        String payload = "";
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            payload = getPayloadFromBuf(buf, wrapper.getCharacterEncoding());
        }
        return payload;
    }

    /**
     * 获取响应体中参数
     *
     * @param response 响应
     * @return 响应体中参数
     */
    protected String getResponsePayload(HttpServletResponse response) {
        String payload = "";
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            payload = getPayloadFromBuf(buf, "UTF-8");
        }
        return payload;
    }

    /**
     * 创建请求日志实际需要打印的内容
     *
     * @param request     请求
     * @param payload     请求体中参数
     * @param requestDate 请求时间
     * @return 请求日志实际需要打印的内容
     */
    protected String createRequestMessage(HttpServletRequest request, String payload, Date requestDate, String requestId) {
        String traceId = MDC.get("X-B3-TraceId");
        StringBuilder msg = new StringBuilder();
        msg.append("Inbound Message\n----------------------------\n");
        msg.append("TraceId: ").append(traceId).append("\n");
        msg.append("Address: ").append(request.getRequestURL()).append("\n");
        msg.append("HttpMethod: ").append(request.getMethod()).append("\n");
        msg.append("QueryString: ").append(request.getQueryString()).append("\n");
        msg.append("RequestId: ").append(requestId).append("\n");
        msg.append("RequestDate: ").append(DateUtil.convertToString(requestDate)).append("\n");
        msg.append("Encoding: ").append(request.getCharacterEncoding()).append("\n");
        msg.append("Content-Type: ").append(request.getContentType()).append("\n");
        if (isNeedLogHeader()) {
            msg.append("Headers: ").append(new ServletServerHttpRequest(request).getHeaders()).append("\n");
        }
        if (isNeedLogPayload()) {
            int length = Math.min(payload.length(), getMaxPayloadLength());
            msg.append("Payload: ").append(payload, 0, length).append("\n");
        }
        msg.append("----------------------------------------------");
        return msg.toString();
    }

    /**
     * 创建响应日志实际需要打印的内容
     *
     * @param response     响应
     * @param payload      响应体中参数
     * @param responseDate 响应时间
     * @param requestId    请求ID
     * @param requestUrl   请求地址
     * @return 响应日志实际需要打印的内容
     */
    protected String createResponseMessage(HttpServletResponse response, String payload, Date responseDate, Date requestDate, String requestId, String requestUrl) {
        String traceId = MDC.get("X-B3-TraceId");
        StringBuilder msg = new StringBuilder();
        msg.append("Outbound Message\n----------------------------\n");
        msg.append("TraceId: ").append(traceId).append("\n");
        msg.append("RequestId: ").append(requestId).append("\n");
        msg.append("Address: ").append(requestUrl).append("\n");
        msg.append("ResponseDate: ").append(DateUtil.convertToString(responseDate)).append("\n");
        msg.append("ProcessingTime: ").append("本次处理消耗:")
                .append(DateUtil.getMinuteDiff(requestDate, responseDate)).append("min-")
                .append(DateUtil.getSecondDiff(requestDate, responseDate)).append("s-")
                .append(DateUtil.getSecondMilli(requestDate, responseDate)).append("ms").append("\n");
        msg.append("Encoding: ").append("UTF-8").append("\n");
        msg.append("Content-Type: ").append(response.getContentType()).append("\n");
        if (isNeedLogHeader()) {
            try (ServletServerHttpResponse servletServerHttpResponse = new ServletServerHttpResponse(response)) {
                msg.append("Headers: ").append(servletServerHttpResponse.getHeaders()).append("\n");
            }
        }

        boolean needLogContentType = true;
        String contentType = response.getContentType();
        // 是JSON格式的才输出
        needLogContentType = StringUtils.hasLength(contentType) || contentType.toUpperCase().contains("JSON") || contentType.contains("text");
        if (isNeedLogPayload() && needLogContentType) {
            int length = Math.min(payload.length(), getMaxPayloadLength());
            msg.append("Payload: ").append(payload, 0, length).append("\n");
        }
        msg.append("----------------------------------------------");
        return msg.toString();
    }

    /**
     * 将byte[]参数转换为字符串用于输出
     *
     * @param buf               字节数组
     * @param characterEncoding 编码
     * @return 字符串
     */
    protected String getPayloadFromBuf(byte[] buf, String characterEncoding) {
        String payload = "";
        if (buf.length > 0) {
            int length = Math.min(buf.length, getMaxPayloadLength());
            try {
                payload = new String(buf, 0, length, characterEncoding);
            } catch (UnsupportedEncodingException ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
        return payload;
    }

    public boolean isNeedLogRequest() {
        return needLogRequest;
    }

    public void setNeedLogRequest(boolean needLogRequest) {
        this.needLogRequest = needLogRequest;
    }

    public boolean isNeedLogResponse() {
        return needLogResponse;
    }

    public void setNeedLogResponse(boolean needLogResponse) {
        this.needLogResponse = needLogResponse;
    }

    public boolean isNeedLogHeader() {
        return needLogHeader;
    }

    public void setNeedLogHeader(boolean needLogHeader) {
        this.needLogHeader = needLogHeader;
    }

    public boolean isNeedLogPayload() {
        return needLogPayload;
    }

    public void setNeedLogPayload(boolean needLogPayload) {
        this.needLogPayload = needLogPayload;
    }

    public int getMaxPayloadLength() {
        return maxPayloadLength;
    }

    public void setMaxPayloadLength(int maxPayloadLength) {
        this.maxPayloadLength = maxPayloadLength;
    }

    public Monitor getMonitor() {
        return monitor;
    }
}
