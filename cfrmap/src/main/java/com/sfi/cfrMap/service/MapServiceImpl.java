package com.sfi.cfrmap.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sfi.cfrmap.domain.Location;
import com.sfi.cfrmap.uims.UimsClientUserProfileImpl;

@Service
public class MapServiceImpl implements MapService{
	
	
	@Autowired
	UimsClientUserProfileImpl ucup;
	public List<Integer> getAllRegions(){
		//get All regions from user profile
		List<String> roles = ucup.getroles();
		List<Integer> regions = new ArrayList();
		Set<Integer> set = new HashSet<>();
		for(int i = 0; i < roles.size(); i++) {
			if("CFR_ADMIN".equals(roles.get(i))) {
				List<Integer> temp = new ArrayList();
				for(int j = 1; j <= 11; j++) {
					temp.add(j);
				}
				set.addAll(temp);
				break;
			}
			if(roles.get(i).contains("CFR_R1_")) {
				set.add(1);
				continue;
			}
			if(roles.get(i).contains("CFR_R2_")) {
				set.add(2);
				continue;
			}
			if(roles.get(i).contains("CFR_R3_")) {
				set.add(3);
				continue;
			}
			if(roles.get(i).contains("CFR_R4_")) {
				set.add(4);
				continue;
			}
			if(roles.get(i).contains("CFR_R5_")) {
				set.add(5);
				continue;
			}
			if(roles.get(i).contains("CFR_R6_")) {
				set.add(6);
				continue;
			}
			if(roles.get(i).contains("CFR_R7_")||roles.get(i).contains("CFR_R07_")) {
				set.add(7);
				continue;
			}
			if(roles.get(i).contains("CFR_R8_")) {
				set.add(8);
				continue;
			}
			if(roles.get(i).contains("CFR_R9_")) {
				set.add(9);
				continue;
			}
			if(roles.get(i).contains("CFR_R10_")) {
				set.add(10);
				continue;
			}
			if(roles.get(i).contains("CFR_R11_")) {
				set.add(11);
				continue;
			}
		}
		regions.addAll(set);
		return regions;
	}
}
