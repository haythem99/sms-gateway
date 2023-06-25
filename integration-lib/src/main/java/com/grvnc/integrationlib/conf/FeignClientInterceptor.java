package com.grvnc.integrationlib.conf;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.grvnc.sharedlib.outh2.OAuthUserVO;
import com.grvnc.sharedlib.outh2.UtilOuth2;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate template) {
		log.debug("ClientInterceptor");

		String token = "";
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (null != authentication && !(authentication instanceof AnonymousAuthenticationToken)) {
				log.debug("trying to call internal microservice by Feign and user has AuthenticationToken");
				OAuthUserVO oAuthUserVO = UtilOuth2.getUserFromToken();
				oAuthUserVO.getRoles().add("ROLE_SYSTEM");
				token = UtilOuth2.generateAccessTokens(oAuthUserVO, 30, "system.issuer").get("access_token");
			} else {
				OAuthUserVO oAuthUserVO = new OAuthUserVO();
				oAuthUserVO.setId(-1);
				oAuthUserVO.getRoles().add("ROLE_SYSTEM");
				token = UtilOuth2.generateAccessTokens(oAuthUserVO, 30, "system.issuer").get("access_token");
				log.debug("trying to call internal microservice by Feign and user is AnonymousAuthenticationToken");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		template.header("Authorization", "Bearer " + token);

	}

}
