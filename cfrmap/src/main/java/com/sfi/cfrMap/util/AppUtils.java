package com.sfi.cfrmap.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfi.cfrmap.bean.GeneralLookupBean;
import com.sfi.cfrmap.security.OidUser;

public class AppUtils {

    private static final Logger logger = LoggerFactory.getLogger(AppUtils.class);

    public static final String SURVEY_STATUS_NOT_COMPLETED = "Not Started";
    public static final String SURVEY_STATUS_PARTIALLY_COMPLETED = "Partially Completed";
    public static final String SURVEY_STATUS_ALL_COMPLETED = "All Completed";
    public static final String SURVEY_STATUS_IN_REVIEW = "In Review";
    public static final String SURVEY_STATUS_APPROVED = "Approved";
    public static final String SURVEY_STATUS_REJECTED = "Rejected";
    public static final String SURVEY_STATUS_ARCHIVED = "Archived";

    public static final String BAT_ELIGIBLE_STATUS_NOT_REQUIRED = "0";
    public static final String CURRENT_FY_YEAR = "2016";
    public static final String SURVEY_TYPE_BAT = "BAT";
    public static final String SURVEY_TYPE_BER = "BER";

    public static final String SECTION_STATUS_DRAFT = "Draft";
    public static final String SECTION_STATUS_COMPLETED = "Complete";
    public static final String SECTION_STATUS_APPROVED = "Approved";
    public static final String SECTION_STATUS_REJECTED = "Rejected";
    public static final String SECTION_STATUS_NO_CHANGE = "Open";

    public static final String SESSION_BLD_SEARCH_LIST = "bldSearchList";
    public static final String SESSION_SURVEY_SEARCH_LIST = "surveySearchList";
    public static final String SESSION_DASHBOARD_LIST ="dashboardList";

    public static final String SESSION_REQUIREMENT_SEARCH_LIST = "requirementSearchList";
    
    public static final String REQUIREMENT_OPEN = "Open";
    public static final String REQUIREMENT_CLOSED = "Close";

    public static final Map<Long, String> SEVERITY_MAP = new HashMap<>();

    static {
        SEVERITY_MAP.put(new Long(0), "Immediately");
        SEVERITY_MAP.put(new Long(1), "Within the next 1-2 years");
        SEVERITY_MAP.put(new Long(2), "Within the next 3-5 years");
        SEVERITY_MAP.put(new Long(3), "Within the next 6 or more years");

    }


    public static final int MAX_PAGE_ITEMS = 500;

  

    public static final Map<String, String> ROLE_READABLE_MAP = new HashMap<>();


    
    public static final String USER_ACTIVE_IND = "Y";
    public static final String USER_INACTIVE_IND = "N";
            

    public static String getUserName() {
        return getAuth().getName();
    }

    public static Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * get logged in user details
     *
     * @return
     */
    public static OidUser getAppUser() {
        return (OidUser) getAuth().getPrincipal();
    }

    /**
     * convert object to JSON
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        String json = "";
        ObjectMapper om = new ObjectMapper();
        try {
            json = om.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.warn("could not serialize object to json {}", object);
        }
        return json;
    }

    /**
     * get current date
     *
     * @return
     */
    public static String now() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = formatter.format(new Date());
        return s;

    }

    /**
     * get section status icon
     *
     * @param sectionStatus
     * @return
     */
    public static String getSectionStatusIcon(String sectionStatus) {
        String iconClass = "fa-circle-o text-gray";
        if (StringUtils.equals(sectionStatus, AppUtils.SECTION_STATUS_DRAFT)) {
            iconClass = "fa-edit";
        } if (StringUtils.equals(sectionStatus, AppUtils.SECTION_STATUS_COMPLETED)) {
            iconClass = "fa-check";
        } if (StringUtils.equals(sectionStatus, AppUtils.SECTION_STATUS_APPROVED)) {
            iconClass = "fa-check-square-o text-green";
        } if (StringUtils.equals(sectionStatus, AppUtils.SECTION_STATUS_REJECTED)) {
            iconClass = "fa-close text-red";
        }
        return iconClass;
    }

    /**
     * get section status icon
     *
     * @param sectionStatus
     * @return
     */
    public static String getSectionStatusBadge(String sectionStatus) {
        String statusClass = "bg-gray-light";
        if (StringUtils.equals(sectionStatus, AppUtils.SECTION_STATUS_DRAFT)) {
            statusClass = "bg-gray";
        } if (StringUtils.equals(sectionStatus, AppUtils.SECTION_STATUS_COMPLETED)) {
            statusClass = "bg-yellow";
        } if (StringUtils.equals(sectionStatus, AppUtils.SECTION_STATUS_APPROVED)) {
            statusClass = "bg-green";
        } if (StringUtils.equals(sectionStatus, AppUtils.SECTION_STATUS_REJECTED)) {
            statusClass = "bg-red";
        }
        return statusClass;
    }

    /**
     * format date
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        if (null != date) {
            Format formatter = new SimpleDateFormat("MM/dd/yyyy");
            String s = formatter.format(date);
            return s;
        }
        return "";
    }

    /**
     * format double to US$ format
     *
     * @param amount
     * @return
     */
    public static String formatCurrency(Double amount) {
        if (null != amount) {
            java.util.Currency usd = java.util.Currency.getInstance("USD");
            java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
            format.setCurrency(usd);
            return format.format(amount);
        }
        return "";

    }

   
    public static String formatSqlParameter(String parameter) {
        String value = "";
        if (StringUtils.isNotEmpty(parameter)) {
            value = StringEscapeUtils.escapeSql(parameter);
        }
        return StringUtils.trimToEmpty(value);
    }

    public static void printSesstionAttributes(HttpSession session) {

        Enumeration enames = session.getAttributeNames();
        while (enames.hasMoreElements()) {
            String name = (String) enames.nextElement();
            String value = "" + session.getAttribute(name);
            logger.info("Session Attribute Name {} and {}", name, value);
        }
    }

    
  
   

    /**
     * format double to US$ format
     *
     * @param amount
     * @return
     */
    public static String formatCurrency(String amount) {
        if (null != amount && StringUtils.isNumeric(amount)) {
            //  logger.info("Amount is {}", amount);
            java.util.Currency usd = java.util.Currency.getInstance("USD");
            java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale.US);
            format.setCurrency(usd);
            BigDecimal value = new BigDecimal(amount);
            value = value.setScale(0, RoundingMode.FLOOR);
            //logger.info("Amount is {}", value.toString());
            return format.format(value);
        }
        return "";

    }

    /**
     *
     * @param date
     * @param dateFormat valid java date format ex: MM/dd/yyyy
     * @return
     */
    public static String formatDate(Date date, String dateFormat) {
        if (null != date) {
            Format formatter = new SimpleDateFormat(dateFormat);
            String s = formatter.format(date);
            return s;
        }
        return "";
    }

    /**
     *
     * @param roleName
     * @return
     */
    public static boolean hasRole(String roleName) {
        boolean isAuthorized = Boolean.FALSE;
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) getAppUser().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equalsIgnoreCase(roleName.trim())) {
                isAuthorized = Boolean.TRUE;
                break;
            }
        }
        return isAuthorized;
    }
/**
     *
     * @param roleName
     * @return
     */
    public static boolean hasCFRRole(String rolePostFixName,Collection<GrantedAuthority> authorities) {
        boolean isAuthorized = Boolean.FALSE;
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().contains(rolePostFixName.trim())) {
                isAuthorized = Boolean.TRUE;
                break;
            }
        }
        return isAuthorized;
    }
    /**
     *
     * @param roleNames comma separated role names
     * @return
     */
    public static boolean hasAnyRole(String roleNames) {
        boolean isAuthorized = Boolean.FALSE;
        for (String role : StringUtils.split(roleNames, ",")) {
            if (hasRole(role)) {
                isAuthorized = Boolean.TRUE;
                break;
            }
        }

        return isAuthorized;
    }

  

    

  

   

    
    public static  Boolean isUserHasRole(String aRole) {

    Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) getAppUser().getAuthorities();
                     logger.info("User aRole is {}", aRole);
    for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
                    logger.info("User role is {}", role);
              if (role.startsWith(aRole)) {
                 return  Boolean.TRUE;
                  
              }
        }
         return  Boolean.FALSE;
                
      }   


    /**
     *
     * @return
     */
    public static String getUserRegion() {
        OidUser batUser = AppUtils.getAppUser();
        String userRegion = "";
        if (null != AppUtils.getAppUser() && batUser.isRegionalRole()) {
            // get regions and set the rgion;
            String role = batUser.getRole();
            userRegion = AppUtils.getAppUser().getRegionCode();
            if (StringUtils.isNotEmpty(role)) {
                List<String> roles = AppUtils.getAppUser().getUserRoleMap().get(role);
                if (!roles.isEmpty()) {
                    String rgn = roles.get(0);
                    String[] roleSplit = StringUtils.split(rgn, "_");
                    if (null != roleSplit) {
                        userRegion = roleSplit[roleSplit.length - 1];
                    }
                }
            }

        }
        logger.info("User Region is {}", userRegion);
        return userRegion;
    }
        /**
     *
     * @return
     */
    public static boolean isUserBerInRegion(String regionCd ) {
        
         Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) getAppUser().getAuthorities();
       
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.startsWith("ROLE_IRIS_BAT_INSPECTOR_R"+formatRegion(regionCd))) {
            return true;    
            
            }
        }
        
        return false;
        
    }
         /**
     *
     * @return
     */
    public static String formatRegion(String regionCd) {
        DecimalFormat df= new DecimalFormat ("00"); 
        return df.format(new Long(regionCd));
                
    }
    public static Set<GeneralLookupBean> getUserRegions() {

        
        
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) getAppUser().getAuthorities();
        Set<GeneralLookupBean> unq = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority().toUpperCase();
            if (role.startsWith("ROLE_CFR_R1")) {
               unq.add(new GeneralLookupBean ("R1","R1"));
            } 
            
            if (role.startsWith("ROLE_CFR_R2")) {
                
                unq.add(new GeneralLookupBean ("R2","R2"));
            } 
            if (role.startsWith("ROLE_CFR_R3")) {
                unq.add(new GeneralLookupBean ("R3","R3"));
            } 
            if (role.startsWith("ROLE_CFR_R4")) {
                unq.add(new GeneralLookupBean ("R4","R4"));
            } 
            if (role.startsWith("ROLE_CFR_R5")) {
                unq.add(new GeneralLookupBean ("R5","R5"));
            }
            if (role.startsWith("ROLE_CFR_R6")) {
                unq.add(new GeneralLookupBean ("R6","R6"));
            }
            if (role.startsWith("ROLE_CFR_R7") || role.startsWith("ROLE_CFR_R07")) {
                unq.add(new GeneralLookupBean ("R7","R7"));
            }            
            if (role.startsWith("ROLE_CFR_R8")) {
                unq.add(new GeneralLookupBean ("R8","R8"));
            }
            if (role.startsWith("ROLE_CFR_R9")) {
                unq.add(new GeneralLookupBean ("R9","R9"));
            }
            if (role.startsWith("ROLE_CFR_R10")) {
                unq.add(new GeneralLookupBean ("R10","R10"));
            }
            
            if (role.startsWith("ROLE_CFR_R11")) {
                unq.add(new GeneralLookupBean ("R11","R11"));
            }
            if (role.equalsIgnoreCase(("ROLE_CFR_ADMIN"))) {
              unq.add(new GeneralLookupBean ("R1","R1"));
              unq.add(new GeneralLookupBean ("R2","R2"));
              unq.add(new GeneralLookupBean ("R3","R3"));
              unq.add(new GeneralLookupBean ("R4","R4"));
              unq.add(new GeneralLookupBean ("R5","R5"));
              unq.add(new GeneralLookupBean ("R6","R6"));
              unq.add(new GeneralLookupBean ("R07","R7"));
              unq.add(new GeneralLookupBean ("R8","R8"));
              unq.add(new GeneralLookupBean ("R9","R9"));
              unq.add(new GeneralLookupBean ("R10","R10"));
              unq.add(new GeneralLookupBean ("R11","R11"));
              
            }
            
        }
        
        return unq;
    }

}

   
