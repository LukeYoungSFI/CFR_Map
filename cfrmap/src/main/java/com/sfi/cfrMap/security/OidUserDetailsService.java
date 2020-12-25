package com.sfi.cfrmap.security;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sfi.cfrmap.uims.UimsClientUserInfoImpl;
import com.sfi.cfrmap.uims.UimsClientUserProfileImpl;
import com.sfi.cfrmap.util.AppUtils;

@Service
public class OidUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(OidUserDetailsService.class);
    private static  String OID_USER_CHARS = "CFR_"; //deafault

    private UimsClientUserInfoImpl uimsClientUserInfo = null;

    private UimsClientUserProfileImpl uimsClientUserProfile = null;
    
    @Autowired
    private Environment environment;

    public OidUserDetailsService() {
      
    }

    public OidUserDetailsService(UimsClientUserInfoImpl uimsClientUserInfoImpl, UimsClientUserProfileImpl uimsClientUserProfileImpl) {

        this.uimsClientUserInfo = uimsClientUserInfoImpl;

        this.uimsClientUserProfile = uimsClientUserProfileImpl;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        
        if(userName.equalsIgnoreCase("INVALID-USER")){
            throw new UsernameNotFoundException("Invalid user - "  + userName);
            
        }
        OidUser oidUser = getOidUser(userName);
        if (oidUser == null || oidUser.getAuthorities().isEmpty()) {
            // TODO throw UsernameNotFoundException
             throw new UsernameNotFoundException("Invalid user - "  + userName);
           
        }else{
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) oidUser.getAuthorities();
            boolean isCFRRegionalUser = AppUtils.hasCFRRole("CFR_R",authorities);
            boolean isCFRAdminUser = AppUtils.hasCFRRole("CFR_ADMIN",authorities);
            if (!(isCFRRegionalUser||isCFRAdminUser) ) {
              throw new UsernameNotFoundException("No CFR Role for user - "  + userName);
            }
                
            authorities.add(new SimpleGrantedAuthority("ROLE_ALL_ACCESS"));
        }

        return oidUser;
    }

    /**
     *
     * @param groupName
     * @return
     */
    private List<String> getUsersFromGroup(String groupName) {
        List<String> groups = new ArrayList<>();
        try {
            List<gov.gsa.pbs.servicesgen.uimsclientv2.jaxb.User> oidUsers = uimsClientUserInfo.getGroups(groupName);
            if (null != oidUsers) {
                for (gov.gsa.pbs.servicesgen.uimsclientv2.jaxb.User oidUser : oidUsers) {
                    oidUser.getUserid();
                    groups.add(oidUser.getUserid());
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OidUserDetailsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return groups;
    }

    /**
     *
     * @param userName
     * @return
     */
    private OidUser getOidUser(String userName) {

        OidUser batUser = null;
        try {
            batUser = uimsClientUserProfile.getUserRoles(userName, OID_USER_CHARS);

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OidUserDetailsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return batUser;
    }
}

