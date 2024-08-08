package com.example.demo;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.dto.UserLoginRequest;
import com.example.demo.serviceImpl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = { "/*" })
@Order(1)
@Component
public class AuthFilter extends OncePerRequestFilter {

	Logger logger = LoggerFactory.getLogger(AuthFilter.class);

	@Autowired
	private UserServiceImpl userService;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		CachedBodyHttpServletRequest cachedBodyRequest = new CachedBodyHttpServletRequest(request);
		String requestURI = request.getRequestURI();

		if (requestURI.equals("/auth/login") && "POST".equalsIgnoreCase(request.getMethod())) {

			UserLoginRequest userLoginRequest = convertTo(cachedBodyRequest);

			boolean isAuth = userService.authenticateUser(userLoginRequest);

			if (isAuth) {
				String username = userLoginRequest.getUsername();
				session.setAttribute("uName", username);

			} else {
				// Navigate to 403 page
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Incorrect username or password.");
				logger.info("Incorrect username or password.");
				return;
			}
		}
		
		// pass
		filterChain.doFilter(cachedBodyRequest, response);
	}


	public UserLoginRequest convertTo(CachedBodyHttpServletRequest cachedBodyHttpServletRequest) throws JsonMappingException, JsonProcessingException {
		ObjectMapper obj = new ObjectMapper();

		UserLoginRequest userLoginRequest = new UserLoginRequest();
		String contentType = cachedBodyHttpServletRequest.getContentType();

		if (contentType.equals("application/json")) {
			userLoginRequest = obj.readValue(cachedBodyHttpServletRequest.getRequestBody(), UserLoginRequest.class);

		}

		else if (contentType.equals("application/x-www-form-urlencoded")) {
			String queryString = cachedBodyHttpServletRequest.getRequestBody();
			Map<String, String> params = parseFormParams(queryString);
			userLoginRequest.setUsername(params.get("formUsername"));
			userLoginRequest.setPassword(params.get("formPassword"));
		}

		return userLoginRequest;
	}

	private Map<String, String> parseFormParams(String body) {
		return body.lines()
				.flatMap(
						line -> Arrays.stream(line.split("&"))
				)
				.map(
						param -> param.split("=")
				)
				.collect(
						Collectors.toMap(param -> param[0], param -> param[1])
				);
	}


	private static class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

		private final String requestBody;

		public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
			super(request);

			requestBody = new BufferedReader(
					new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)
			)
				.lines()
				.collect(Collectors.joining("\n"));
		}

		public String getRequestBody() {
			return requestBody;
		}


		@Override
		public ServletInputStream getInputStream() throws IOException {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));

			return new ServletInputStream() {

				@Override
				public int read() throws IOException {
					return byteArrayInputStream.read();
				}

				@Override
				public boolean isFinished() {
					return byteArrayInputStream.available() == 0;
				}

				@Override
				public boolean isReady() {
					return true;
				}

				@Override
				public void setReadListener(ReadListener readListener) {}
			};
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return new BufferedReader(new InputStreamReader(this.getInputStream(), StandardCharsets.UTF_8));
		}

	}

}
