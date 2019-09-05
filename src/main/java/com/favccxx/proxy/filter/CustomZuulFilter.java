package com.favccxx.proxy.filter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import io.micrometer.core.instrument.util.StringUtils;

@Component
public class CustomZuulFilter extends ZuulFilter {

	Logger logger = LoggerFactory.getLogger(getClass());

	DateFormat df = new SimpleDateFormat("yyyyMMdd");

	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		String token = request.getParameter("token");
		logger.info("token: " + token);

		if (StringUtils.isBlank(token)) {
			// URL没有Token时，在SESSION中进行权限校验
			String authToken = (String) request.getSession().getAttribute("authToken");
			if (StringUtils.isBlank(authToken)) {
				// 认证失败
				context.setSendZuulResponse(false);
				context.setResponseStatusCode(400);
				context.setResponseBody("Auth failed, token is empty.");
				logger.info("Auth failed, token is empty.");
				return null;
			} else {
				String digetToken = DigestUtils.sha256Hex("kibana" + df.format(new Date()));
				if (digetToken.equals(authToken)) {
					context.setSendZuulResponse(true);
					context.setResponseStatusCode(200);
					logger.info("Auth authToken success.");
					return null;
				}
				context.setSendZuulResponse(false);
				context.setResponseStatusCode(400);
				context.setResponseBody("Auth failed, token is empty.");
				logger.info("Auth failed, token is empty.");
				return null;

			}
		} else {
			// 校验Token是否正确
			String digetToken = DigestUtils.sha256Hex("zuul" + df.format(new Date()));
			if (digetToken.equals(token)) {
				request.getSession().setAttribute("authToken", token);
				context.setSendZuulResponse(true);
				context.setResponseStatusCode(200);
				logger.info("Auth token success.");
				return null;
			} else {
				context.setSendZuulResponse(false);
				context.setResponseStatusCode(400);
				context.setResponseBody("Auth failed, token is error.");
				logger.info("Auth failed, token is empty.");
				return null;
			}

		}
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

}
