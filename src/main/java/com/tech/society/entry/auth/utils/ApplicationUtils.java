package com.tech.society.entry.auth.utils;

import com.tech.society.entry.auth.dto.RequestContext;
import jakarta.servlet.http.HttpServletRequest;

public class ApplicationUtils {

    public static RequestContext getRequestContext(HttpServletRequest request) {
        RequestContext req = new RequestContext(request.getHeader("societyIdentifier"), request.getRemoteAddr(), request.getHeader("Request-Time"), request.getHeader("username"));
        return req;
    }
}
