package com.sfi.cfrmap.controller;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.chemistry.opencmis.client.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sfi.cfrmap.service.MapService;
import com.sfi.cfrmap.uims.UimsClientUserInfoImpl;
import com.sfi.cfrmap.uims.UimsClientUserProfileImpl;


@Controller
public class MapServiceController implements ErrorController{
	
	private static final Logger logger = LoggerFactory.getLogger(MapServiceController.class);
	
	@Autowired
	MapService mapService;
	
	ResourceBundle rb = ResourceBundle.getBundle("server");
	
	
	@GetMapping("/")
	public String cfrmap(HttpServletRequest request, Model model) {
		//logger.info("principal name is: " + request.getUserPrincipal().getName());
		List<String> list = Collections.list(request.getHeaderNames());
		String regionStr = "";
		StringBuilder sb = new StringBuilder();
		List<Integer> regions = mapService.getAllRegions();
		for(int i = 0; i < regions.size(); i++) {
			sb.append(regions.get(i)).append("-");
		}
		if(sb.length() > 0) {			
			regionStr = sb.toString().substring(0, sb.length() - 1);
		}
		System.out.println("user region roles are: " + regionStr);
		model.addAttribute("regionStr", regionStr);
		model.addAttribute("token", rb.getString("token"));
		model.addAttribute("urlHeader", rb.getString("urlHeader"));
		model.addAttribute("serverName", rb.getString("serverName"));
		return "map";
	}


	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	    
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	            return "404";
	        }
	        else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	            return "500";
	        }
	    }
	    return "error";
	}
		
}
