package com.grvnc.sharedlib.outh2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.grvnc.sharedlib.ex.BusinessException;
import com.grvnc.sharedlib.util.AESEncryptor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UtilOuth2 {

	private static Algorithm algorithm = Algorithm.HMAC256("secretHAYTHRKL:K;l".getBytes());
	private static String secretKey_ = "!!sH>,xsxsecreXStHAYTHRKL:K;lfdas";

	public static void authrization(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

//		if (request.getServletPath().startsWith("/restricted") || request.getServletPath().startsWith("/public")) {

		log.debug("validating user token");
		String authrizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authrizationHeader != null && authrizationHeader.startsWith("Bearer ")) {
			try {
				String token = authrizationHeader.substring("Bearer ".length());

				OAuthUserVO user = UtilOuth2.validateToken(token);
				log.debug("user has valid token");
				Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

				for (String role : user.getRoles()) {
					authorities.add(new SimpleGrantedAuthority(role));
				}

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(token,
						null, authorities);
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			} catch (Exception e) {
				log.info("user does not have valid token:" + e.getMessage());
			}
		} else {
			log.debug("user trying to access API without JWT Token url[" + request.getServletPath() + "]");
		}
		filterChain.doFilter(request, response);

//		} else {
//			log.debug("url is not API  (not start with [public] or [restricted]) so this request will be not processessed by this filter");
//			filterChain.doFilter(request, response);
//		}

	}

	public static OAuthUserVO validateToken(String token) {
		try {
			JWTVerifier verifier = JWT.require(algorithm).build();
			DecodedJWT decodedJWT = verifier.verify(token);
			String decodedString = new String(Base64.getDecoder().decode(decodedJWT.getPayload()));
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> jwtPayload = mapper.readValue(decodedString, Map.class);
			;
			String userAsJson = AESEncryptor.decrypt((String) jwtPayload.get("payload"), secretKey_);
			OAuthUserVO user = mapper.readValue(userAsJson, OAuthUserVO.class);
			return user;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new BusinessException(null, "access_token_validation_error", e.getMessage());
		}
	}

	public static OAuthUserVO getUserFromToken() throws JsonMappingException, JsonProcessingException {

		OAuthUserVO user = validateToken(SecurityContextHolder.getContext().getAuthentication().getName());

		return user;
	}

	public static Map<String, String> generateAccessTokens(OAuthUserVO oAuthUserVO, int accessTokenValidityInMinutes,
			String issuer) {
		try {
			Map<String, String> payload = new HashMap<String, String>();
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(oAuthUserVO);
			payload.put("payload", AESEncryptor.encrypt(json, secretKey_));

			List<String> rolesCodes = oAuthUserVO.getRoles();

			String access_token = JWT.create().withSubject(oAuthUserVO.getId()+"").withPayload(payload)
//					.withExpiresAt(new Date(System.currentTimeMillis()+(60*1000)))
					.withExpiresAt(new Date(System.currentTimeMillis() + (accessTokenValidityInMinutes * 60 * 1000)))
					.withIssuer(issuer)

					.withClaim("roles", rolesCodes)
//					.withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority :: getAuthority).collect(Collectors.toList()))
					.sign(algorithm);
			String refresh_token = JWT.create().withSubject(oAuthUserVO.getId() + "")
//					.withExpiresAt(new Date(System.currentTimeMillis()+(2*60*1000)))
					.withExpiresAt(new Date(System.currentTimeMillis() + (356 * 24 * 60 * 60 * 1000)))
					.withIssuer(issuer).withClaim("roles", new ArrayList<>()).sign(algorithm);
//			response.setHeader("access_token", access_token);
//			response.setHeader("refresh_token", refresh_token);
			Map<String, String> tokens = new HashMap<>();
			tokens.put("access_token", access_token);
			tokens.put("refresh_token", refresh_token);
			log.info("a token has been generated for userId[" + oAuthUserVO.getId() + "], his roles are["
					+ rolesCodes.toString() + "] and ssiuer[" + issuer + "]");
			return tokens;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new BusinessException(null, "access_token_creation_error", e.getMessage());
		}
	}
// public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
//	
// String json="{\"iosPasscodes\":[\"1E7KCz3gRu8@\",\"nO*XJuG45*7w\"],\"androidPasscodes\":[\"8PuiuU9D9K$d\",\"2d37m2Pj!5@K\"],\"webDesktopPasscodes\":[\"M7l1v@Ry00^*\",\"@6M*pigysJ07\"],\"webMobilePasscodes\":[\"O7l1v$Ry00C(\",\"!6M9piNysJ0J\"]}";
//System.out.println("s");
//JSONObject jsonObject= new JSONObject(json );
//
//	    List<Object> email = jsonObject.getJSONArray("androidPasscodes").toList();
//	   System.out.println( email.contains("nO*XJuG45*7w"));
//	   for (Object string : email) {
//		System.err.println(string);
//	}
//}
//		public static void main(String[] args) throws JsonProcessingException {
//			
//			OAuthUserVO user=new OAuthUserVO();
//			user.setId(100);
//			user.setMobileNumber("0544440699");
//			user.getRoles().add("r1");
//			user.getRoles().add("r2");
//			Map<String, String> tokens=UtilOuth2.generateAccessTokens(user,2, "sss");
//			System.out.println(tokens.get("access_token"));
//			user=validateToken(tokens.get("access_token"));
//			System.out.println(user.getId());
//		}

}
