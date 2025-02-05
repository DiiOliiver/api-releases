package br.com.bank.api_releases.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = extractTokenFromRequest(request);

			if (token != null && jwtTokenProvider.validateToken(token)) {
				String username = jwtTokenProvider.getUsernameFromToken(token);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			System.out.println("Não foi possível autenticar o token: " + e.getMessage());
		}

		filterChain.doFilter(request, response);
	}

	private String extractTokenFromRequest(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}
}
