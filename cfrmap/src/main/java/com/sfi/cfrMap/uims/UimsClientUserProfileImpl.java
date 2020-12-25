package com.sfi.cfrmap.uims;

import gov.gsa.pbs.servicesgen.uims.ws.jaxb.UserAppRolesType;
import gov.gsa.pbs.servicesgen.uims.ws.jaxb.UserProfile;
import gov.gsa.pbs.servicesgen.uims.ws.GetUserProfile;
import gov.gsa.pbs.servicesgen.uims.ws.GetUserProfileResponse;
import gov.gsa.pbs.servicesgen.uims.ws.ObjectFactory;
import gov.gsa.pbs.servicesgen.uims.ws.UserProfileInput;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.sfi.cfrmap.security.OidUser;

public class UimsClientUserProfileImpl extends WebServiceGatewaySupport {

    private static final Logger logger = LoggerFactory.getLogger(UimsClientUserProfileImpl.class);

    private String applicationName;
    //private String userId;
    private String publicKey;
    private String privateKey;
    private String charFormat;
    private List<String> roles;

    public UimsClientUserProfileImpl(WebServiceMessageFactory messageFactory) {
        super(messageFactory);
        logger.info("Initialized UimsClientUserProfileImpl");
    }

    /**
     *
     * @param userId
     * @param roleContains
     * @return
     */
    public OidUser getUserRoles(String userId, CharSequence roleContains) {
        OidUser batUser=null;
        logger.info("Entered getUserProfile new class ");
        ObjectFactory objectFactory = new ObjectFactory();
        GetUserProfile profile = objectFactory.createGetUserProfile();
        List<GrantedAuthority> authorities = new ArrayList();
        roles = new ArrayList();
        UserProfile userProfile = null;
        SecurityUtil secUtil = new SecurityUtil(this.charFormat, this.privateKey, this.publicKey);
        try {
            UserProfileInput profileInput = objectFactory.createUserProfileInput();
            profileInput.setApplicationId(objectFactory.createUserProfileInputApplicationId(secUtil.encryptWithPublicKey(this.applicationName)));
            profileInput.setUserId(objectFactory.createUserProfileInputUserId(secUtil.encryptWithPrivateKey(userId)));
            profileInput.setRequestTime(objectFactory.createUserProfileInputRequestTime(secUtil.encryptWithPrivateKey(getDateAsString(null, null, "MM/dd/yy hh:mm a", null, TimeZone.getTimeZone("America/New_York")))));

            profile.setIn0(profileInput);
            logger.info("Request object is --" + profile.toString());
            GetUserProfileResponse response = (GetUserProfileResponse) getWebServiceTemplate().marshalSendAndReceive(profile);
            InputStream is = new ByteArrayInputStream(secUtil.decryptWithPrivateKey(response.getOut()).getBytes());
            userProfile = (UserProfile) JAXB.unmarshal(is, UserProfile.class);

            for (Iterator<UserAppRolesType> itr = userProfile.getUserRolesList().getUserAppRoles().iterator(); itr.hasNext();) {
                UserAppRolesType userRoleType = (UserAppRolesType) itr.next();
                for (String roleName : userRoleType.getRoleNames()) {
                    if (this.applicationName.equalsIgnoreCase(userRoleType.getApplicationName())) {
                        if (roleName.toUpperCase().contains(roleContains)) {
                            GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase());
                            roles.add(roleName.toUpperCase());
                            authorities.add(ga);
                            logger.info("Added authority " + ga.getAuthority());
                        }

                    }
                }
            }
        } catch (Exception e) {

            logger.error("Exception occured while fetching user roles");
            logger.error("Exception occured while fetching user roles " + e.getMessage());
            e.printStackTrace();
        }
        String middleName = null;
        try {
            middleName = userProfile.getBasicProfile().getOidInfo().getMiddleName().getValue();
        } catch (Exception e) {
        
        }
        if (userProfile!=null) {
         batUser = new OidUser(authorities, userProfile.getBasicProfile().getOidInfo().getEmailId(),
                userProfile.getBasicProfile().getOidInfo().getRegion(), userProfile.getBasicProfile().getOidInfo().getFirstName(), userProfile.getBasicProfile().getOidInfo().getLastName(),
                middleName
       
        ); }
        //return setDefUset();
        return batUser;
    }
  OidUser setDefUset() {
        List<GrantedAuthority> authorities = new ArrayList();
           GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_IRIS_BAT_SYSTEM_ADMIN");
                            authorities.add(ga);
       
        OidUser batUser = new OidUser(authorities, "tom.baker@gsa.gov","R03", "tom", "baker","");    
        
      return batUser ; 
  }
  public List<String> getroles() {
	  return roles;
  }
            
    public static String getDateAsString(String date, String currentFormat, String format, Locale locale, TimeZone tz) {
        DateFormat aDateFormat = DateFormat.getDateTimeInstance(3, 3, Locale.getDefault());

        TimeZone aDefaultTZ = TimeZone.getDefault();
        int nOffSet = 0;
        if (aDefaultTZ.inDaylightTime(new Date(System.currentTimeMillis()))) {
            nOffSet = aDefaultTZ.getRawOffset() + (aDefaultTZ.useDaylightTime() ? 3600000 : 0);
        } else {
            nOffSet = aDefaultTZ.getRawOffset();
        }
        Date aLocalDate = new Date(System.currentTimeMillis() - nOffSet);
        String timeStamp = aDateFormat.format(aLocalDate);
        return timeStamp;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setCharFormat(String charFormat) {
        this.charFormat = charFormat;
    }

    public String getCharFormat() {
        return this.charFormat;
    }
}
