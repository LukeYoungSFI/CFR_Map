package com.sfi.cfrmap.filter;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.util.StringUtils;

/**
 * Extends {@link RequestHeaderAuthenticationFilter} to suit the needs of SSO
 * Portal integration.
 *
 * <p> This implementation extracts the userName from the userDn, as shown in
 * the format <li> userDn ="cn=jane.senk@gsa.gov,cn=users, dc=3ht,dc=com" <li>
 * Extracted principal is peggy.way@gsa.gov
 *
 *
 * @author Venu Duggireddy
 */
public class SSORequestHeaderAuthenticationFilter extends RequestHeaderAuthenticationFilter{
    
    private static final Logger logger = LoggerFactory.getLogger(SSORequestHeaderAuthenticationFilter.class);
    
    private boolean allowPreAuthenticatedPrincipals = true;
    
    @Autowired
    private Environment environment;


    public SSORequestHeaderAuthenticationFilter() {
        super();
        this.setPrincipalRequestHeader("Osso-User-Dn");
    }
    
    

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String principal = null;
        
        // TODO: Spring shuould ignore by default. Might be a bug 1.3.3 release have to revisit
        String url = request.getRequestURL().toString();
        if(url.endsWith(".js") || url.endsWith(".css") || url.contains("/fonts/")){
             logger.info("url is {}", request.getRequestURL());
            return null;
        }
        //Get the userDn of the logged in user
        String userDn = (String) super.getPreAuthenticatedPrincipal(request);

        Set<String> userDnSet;

        if (StringUtils.hasText(userDn)) {
            userDnSet = StringUtils.commaDelimitedListToSet(userDn);

             for (String info : userDnSet) {
                 if (info.startsWith("cn=") && info.indexOf("@") > 0) {
                     principal = StringUtils.delete(info, "cn=");
                     principal = standardizeUsername(principal);
                     break;
                 }
             }
            logger.info("User Dn is {}", principal);
        }

        if (StringUtils.hasText(principal) == false) {
            // this for dev only use spring profiles if possible
            // if(Arrays.asList(environment.getActiveProfiles()).contains("dev")){
            //      principal = "peggy.way@gsa.gov";
                 //   principal = "amy.allen@gsa.gov";
            // }else{
                 principal = "INVALID-USER";
            // }
             
            logger.info("===== DEV only default user is  {} === and active profile is {}", principal, environment.getActiveProfiles());
            //throw new PreAuthenticatedCredentialsNotFoundException(" UserName is not present.");
        }
        
        return principal;
    }
    
    /**
     * Helper Method which removes blanks, ' and make it lower case
     *
     * @param userName
     * @return
     */
    private String standardizeUsername(String userName) {
        String newUserName = userName.toLowerCase();
        newUserName = newUserName.replaceAll("'", "");
        newUserName = newUserName.replaceAll(" ", "");
        return newUserName;
    }

    public boolean isAllowPreAuthenticatedPrincipals() {
        return allowPreAuthenticatedPrincipals;
    }
    
    
    
}

