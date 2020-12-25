package com.sfi.cfrmap.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.WordUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class OidUser implements UserDetails {

    private final Collection<GrantedAuthority> authorities;
    private final String userName;
    private final String password = "";
    String regionCode = "";
    private boolean regionalRole = false;
    boolean secureBuilding = false;
    private String firstName;
    private String lastName;
    private String middleName;
    private String welcome;
    private String profile;
    private String role;

    private Map<String, List<String>> userRoleMap;

    public static final String ROLE_CFR_USER = "ROLE_CFR";
   

    public static final Map<String, String> ROLE_READABLE_MAP = new HashMap<>();

    static {
        ROLE_READABLE_MAP.put(ROLE_CFR_USER, "ROLE_CFR");
        
    }
    
    private Integer roleId;

    public OidUser(String userName, String pwd, List<GrantedAuthority> authorities) {
        this.userName = userName;

        this.authorities = authorities;
    }

    public OidUser(Collection<GrantedAuthority> authorities, String userName, String regionCode, String firstName, String lastName, String middleName) {
        this.authorities = authorities;
        this.userName = userName;
        this.regionCode = regionCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        setup();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    private void setup() {

        userRoleMap = getRoleRegionMap();
        role = getHighestRole();
        this.welcome = String.format("%s %s, %s, %s", WordUtils.capitalize(firstName), WordUtils.capitalize(lastName), regionCode, ROLE_READABLE_MAP.get(role));
        profile = String.format("%s %s, <br/> %s - %s", WordUtils.capitalize(firstName), WordUtils.capitalize(lastName), ROLE_READABLE_MAP.get(role), regionCode);
    }

    /**
     * check if the user has IRIS_BAT_SECURITY_BLD
     *
     * @return
     */
    public boolean isSecureBuilding() {
        return secureBuilding;
    }

    public void setSecureBuilding(boolean secureBuilding) {
        this.secureBuilding = secureBuilding;
    }

    public String getWelcome() {
        return welcome;
    }

    public String getProfile() {
        return profile;
    }

    public String getRole() {
        return role;
    }

    public boolean isRegionalRole() {
        return regionalRole;
    }

    private String getHighestRole() {
        String highestRole = "";
        if (userRoleMap.containsKey(ROLE_CFR_USER)) {
            highestRole = ROLE_CFR_USER;
            this.roleId = 4;
        }
        return highestRole;
    }

    public Map<String, List<String>> getUserRoleMap() {
        return userRoleMap;
    }

    private Map<String, List<String>> getRoleRegionMap() {

        Map<String, List<String>> roleRegionMap = new HashMap<>();
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (role.startsWith(ROLE_CFR_USER)) {
                if (!roleRegionMap.containsKey(ROLE_CFR_USER)) {
                    roleRegionMap.put(ROLE_CFR_USER, new ArrayList<String>());
                }
                roleRegionMap.get(ROLE_CFR_USER).add(role);
            

        }

        return roleRegionMap;
    }
        return roleRegionMap;
    }

    public Integer getRoleId() {
        return roleId;
    }
 
}
