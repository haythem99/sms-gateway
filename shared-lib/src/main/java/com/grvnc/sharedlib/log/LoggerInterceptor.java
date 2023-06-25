package com.grvnc.sharedlib.log;

 

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.grvnc.sharedlib.outh2.UtilOuth2;

import lombok.extern.slf4j.Slf4j;


 
@Configuration
@Slf4j
public class LoggerInterceptor  extends WebMvcConfigurerAdapter  implements   HandlerInterceptor   {

 
    
 
 
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter() {
            @Override
            public void afterRequest(HttpServletRequest request, String message) {
                // No log after, we are just overriding the default behavior
            }
            @Override
            public void beforeRequest(HttpServletRequest request, String message) {
            	log.debug(message);
            }
        };
        filter.setIncludeQueryString(true);
        filter.setIncludeHeaders(true);;
        filter.setIncludePayload(true);
//        filter.setMaxPayloadLength(10000); not recommended by me
        filter.setBeforeMessagePrefix("REQUEST DATA : ");
        return filter;
    }
//    @Bean
//	   public FilterRegistrationBean<HttpLoggingFilter> loggingFilter(){
//	       FilterRegistrationBean<HttpLoggingFilter> registrationBean 
//	         = new FilterRegistrationBean<>();
//	           
//	       registrationBean.setFilter(new HttpLoggingFilter());
////	       registrationBean.addUrlPatterns("/users/*");
//	       registrationBean.setOrder(2);
//	           
//	       return registrationBean;    
//	   }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor());
    }


	  
	private static Logger log = LoggerFactory.getLogger(LoggerInterceptor.class);

    /**
     * Executed before actual handler is executed
     **/
     public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
    	 
//    	 HttpHeaders headers = request.header;
//    	 response.addHeader("client-name", "ss");
//    	 request.setAttribute(null, response);
//    	 System.out.println(response.getHeader("client-name")+" BBBBBBBBBB");
    	 long userId=-1;
    	 try {
    		 userId=UtilOuth2.getUserFromToken( ).getId() ;
    	 }catch (Exception e) {
			log.warn(e.getMessage());
		}
//    	if(userEmail.equals("-1")) {
//    		userEmail="GUEST";
//    	}
 
//		MDC.put("userEmail", userId+"");
//		MDC.put("uuid", getRandomName());
		MDC.put("startTime", System.currentTimeMillis()+"");
//		if( handler instanceof HandlerMethod ) {
			// there are cases where this handler isn't an instance of HandlerMethod, so the cast fails.
//			HandlerMethod handlerMethod = (HandlerMethod) handler;
//			String controllerName  = handlerMethod.getBeanType().getSimpleName();
//			String method = handlerMethod.getMethod().getName();
		
//		}else {
//			log.info("[preHandle][" + request.getMethod() + "][" + request.getRequestURI()+"]["+getRequestParameters(request)+"]");
//			
//		}
		log.info("[**call started**][API Called by userId:("+userId+")][" + request.getMethod() + ":" + request.getRequestURI()+"]["+getRequestParameters(request)+"]");
		  return true;
    }
 
     public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
    	String exceptionMessage="";
    	if (ex != null) {
    		ex.printStackTrace();
    		exceptionMessage="[ERROR:exception: " + ex.getMessage() + "]";
    	}
//    	if( handler instanceof HandlerMethod ) {
//            ex.printStackTrace();
//    	HandlerMethod handlerMethod = (HandlerMethod) handler;
//		String controllerName  = handlerMethod.getBeanType().getSimpleName();
//		String method = handlerMethod.getMethod().getName();
//        	log.info("[API called within["+(System.currentTimeMillis()-Long.parseLong(MDC.get("startTime")))+"ms]"+exceptionMessage);
//    	}else {
    		log.info("[**call ended**][API called within["+(System.currentTimeMillis()-Long.parseLong(MDC.get("startTime")))+"ms]"+exceptionMessage);
//    	}
    	MDC.clear();
    }

//	public  String getRandomName() {
//	return UUID.randomUUID().toString().replace("-", "").replace("_", "");
//}
	private  String getRequestParameters(final HttpServletRequest request) {
        final StringBuffer posted = new StringBuffer();
        final Enumeration<?> e = request.getParameterNames();
        if (e != null)
            posted.append("?");
        while (e != null && e.hasMoreElements()) {
            if (posted.length() > 1)
                posted.append("&");
            final String curr = (String) e.nextElement();
            posted.append(curr).append("=");
            if (curr.contains("password") || curr.contains("answer") || curr.contains("pwd")) {
                posted.append("*****");
            } else {
                posted.append(request.getParameter(curr));
            }
        }

        final String ip = request.getHeader("X-FORWARDED-FOR");
        final String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
        if (!StringUtils.isBlank(ipAddr))
            posted.append("&_psip=" + ipAddr);
        return posted.toString();
    }
    private  String getRemoteAddr(final HttpServletRequest request) {
        final String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }

 

}
 