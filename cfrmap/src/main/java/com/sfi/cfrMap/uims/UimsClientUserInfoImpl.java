package com.sfi.cfrmap.uims;

import gov.gsa.pbs.servicesgen.uimsclientv2.GetUsersByGroups;
import gov.gsa.pbs.servicesgen.uimsclientv2.GetUsersByGroupsInput;
import gov.gsa.pbs.servicesgen.uimsclientv2.GetUsersByGroupsResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import gov.gsa.pbs.servicesgen.uimsclientv2.ObjectFactory;
import gov.gsa.pbs.servicesgen.uimsclientv2.jaxb.User;
import gov.gsa.pbs.servicesgen.uimsclientv2.jaxb.UsersInGroup;

public class UimsClientUserInfoImpl extends WebServiceGatewaySupport {
    //static final Logger logger = Logger.getLogger(UimsClientUserInfoImpl.class);

    private static final Logger logger = LoggerFactory.getLogger(UimsClientUserInfoImpl.class);
    private String applicationName;
    private String userId;
    private String publicKey;
    private String privateKey;
    private String charFormat;

    public UimsClientUserInfoImpl(WebServiceMessageFactory messageFactory) {
        super(messageFactory);
        logger.debug("Initialized UimsClientUserInfoImpl");
    }

    public List<User> getGroups(String groupName) {
        logger.info("Entered getUserProfile new class ");
        gov.gsa.pbs.servicesgen.uimsclientv2.ObjectFactory objectFactory = new ObjectFactory();
        GetUsersByGroups groupsInfo = objectFactory.createGetUsersByGroups();

        List<User> users = null;
        SecurityUtil secUtil = new SecurityUtil(this.charFormat, this.privateKey, this.publicKey);

        try {

            GetUsersByGroupsInput groupsInput = objectFactory.createGetUsersByGroupsInput();

            groupsInput.setGroupNames(objectFactory.createGetUsersByGroupsInputGroupNames(secUtil.encryptWithPrivateKey(groupName)));

            groupsInput.setRequestTime(objectFactory.createGetUsersByRegionIDAndGroupsInputRequestTime(secUtil.encryptWithPrivateKey(getDateAsString(null, null, "MM/dd/yy hh:mm a", null, TimeZone.getTimeZone("America/New_York")))));

            groupsInput.setApplicationId(objectFactory.createGetUsersByRegionIDAndGroupsInputApplicationId(secUtil.encryptWithPublicKey(this.applicationName)));

            groupsInfo.setIn0(groupsInput);

            logger.info("Request object is --" + groupsInfo.toString());
            GetUsersByGroupsResponse response = (GetUsersByGroupsResponse) getWebServiceTemplate().marshalSendAndReceive(groupsInfo);

            InputStream is = new ByteArrayInputStream(secUtil.decryptWithPrivateKey(response.getOut()).getBytes());
            /*
	        BufferedReader br = null;
	        br = new BufferedReader(new InputStreamReader(is));

	        StringBuilder sb = new StringBuilder();

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			System.out.println("\n!GetUsersByGroupsResponse");
			System.out.println(sb.toString());
			System.out.println("\nDone!");
             */

            UsersInGroup userGroup = (UsersInGroup) JAXB.unmarshal(is, UsersInGroup.class);

            logger.info(("size" + userGroup.getUser().size()) + "");

            users = userGroup.getUser();
            for (User u : users) {
                logger.info("new user");
                logger.info(u.getUserid());
                logger.info(u.getRegionid());
                logger.info(u.getLastname());
                logger.info(u.getGivenname());
            }
        } catch (Exception e) {
            logger.error("Response Error ");
            e.printStackTrace();
        }

        return users;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
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
