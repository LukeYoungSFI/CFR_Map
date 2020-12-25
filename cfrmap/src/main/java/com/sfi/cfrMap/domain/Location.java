package com.sfi.cfrmap.domain;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "location")
public class Location {
	@Id
	private String code;
	private Integer region_code;
	private String region_name;
	private String state_code;
	private String state_name;
	private String facility_code;
	private String facility_name;
	private String name;
	private String status_code;
	private String status_name;
	private String ownership_code;
	private String ownership_name;
	private String real_prop_type_code;
	private String real_prop_type_name;
	private Integer mission_dep_code;
	private String mission_dep_name;
	private String address;
	private Double latitude;
	private Double longitude;
	private Double land_acres;
	private Date disp_date;
	private Date const_comp_date;
	private Integer floors_above_ground;
	private Integer floors_below_ground;
	private Integer hist_status_code;
	private String hist_status_name;
	private Integer security_code;
	private String security_name;
	private String lpoe_ind;
	private Double est_gsf;
	private Integer rsf;
	private Integer usf;
	private Integer ru;
	private Integer vrsf;
	private Integer vusf;
	private Integer stp;
	private Integer sup;
	private String etl_date;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getRegion_code() {
		return region_code;
	}
	public void setRegion_code(Integer region_code) {
		this.region_code = region_code;
	}
	public String getRegion_name() {
		return region_name;
	}
	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}
	public String getState_code() {
		return state_code;
	}
	public void setState_code(String state_code) {
		this.state_code = state_code;
	}
	public String getState_name() {
		return state_name;
	}
	public void setState_name(String state_name) {
		this.state_name = state_name;
	}
	
	public String getFacility_code() {
		return facility_code;
	}
	public void setFacility_code(String facility_code) {
		this.facility_code = facility_code;
	}
	public String getFacility_name() {
		return facility_name;
	}
	public void setFacility_name(String facility_name) {
		this.facility_name = facility_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus_code() {
		return status_code;
	}
	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}
	public String getStatus_name() {
		return status_name;
	}
	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}
	public String getOwnership_code() {
		return ownership_code;
	}
	public void setOwnership_code(String ownership_code) {
		this.ownership_code = ownership_code;
	}
	public String getOwnership_name() {
		return ownership_name;
	}
	public void setOwnership_name(String ownership_name) {
		this.ownership_name = ownership_name;
	}
	public String getReal_prop_type_code() {
		return real_prop_type_code;
	}
	public void setReal_prop_type_code(String real_prop_type_code) {
		this.real_prop_type_code = real_prop_type_code;
	}
	public String getReal_prop_type_name() {
		return real_prop_type_name;
	}
	public void setReal_prop_type_name(String real_prop_type_name) {
		this.real_prop_type_name = real_prop_type_name;
	}
	public Integer getMission_dep_code() {
		return mission_dep_code;
	}
	public void setMission_dep_code(Integer mission_dep_code) {
		this.mission_dep_code = mission_dep_code;
	}
	public String getMission_dep_name() {
		return mission_dep_name;
	}
	public void setMission_dep_name(String mission_dep_name) {
		this.mission_dep_name = mission_dep_name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLand_acres() {
		return land_acres;
	}
	public void setLand_acres(Double land_acres) {
		this.land_acres = land_acres;
	}
	public Date getDisp_date() {
		return disp_date;
	}
	public void setDisp_date(Date disp_date) {
		this.disp_date = disp_date;
	}
	public Date getConst_comp_date() {
		return const_comp_date;
	}
	public void setConst_comp_date(Date const_comp_date) {
		this.const_comp_date = const_comp_date;
	}
	public Integer getFloors_above_ground() {
		return floors_above_ground;
	}
	public void setFloors_above_ground(Integer floors_above_ground) {
		this.floors_above_ground = floors_above_ground;
	}
	public Integer getFloors_below_ground() {
		return floors_below_ground;
	}
	public void setFloors_below_ground(Integer floors_below_ground) {
		this.floors_below_ground = floors_below_ground;
	}
	public Integer getHist_status_code() {
		return hist_status_code;
	}
	public void setHist_status_code(Integer hist_status_code) {
		this.hist_status_code = hist_status_code;
	}
	public String getHist_status_name() {
		return hist_status_name;
	}
	public void setHist_status_name(String hist_status_name) {
		this.hist_status_name = hist_status_name;
	}
	public Integer getSecurity_code() {
		return security_code;
	}
	public void setSecurity_code(Integer security_code) {
		this.security_code = security_code;
	}
	public String getSecurity_name() {
		return security_name;
	}
	public void setSecurity_name(String security_name) {
		this.security_name = security_name;
	}
	public String getLpoe_ind() {
		return lpoe_ind;
	}
	public void setLpoe_ind(String lpoe_ind) {
		this.lpoe_ind = lpoe_ind;
	}
	public Double getEst_gsf() {
		return est_gsf;
	}
	public void setEst_gsf(Double est_gsf) {
		this.est_gsf = est_gsf;
	}
	public Integer getRsf() {
		return rsf;
	}
	public void setRsf(Integer rsf) {
		this.rsf = rsf;
	}
	public Integer getUsf() {
		return usf;
	}
	public void setUsf(Integer usf) {
		this.usf = usf;
	}
	public Integer getRu() {
		return ru;
	}
	public void setRu(Integer ru) {
		this.ru = ru;
	}
	public Integer getVrsf() {
		return vrsf;
	}
	public void setVrsf(Integer vrsf) {
		this.vrsf = vrsf;
	}
	public Integer getVusf() {
		return vusf;
	}
	public void setVusf(Integer vusf) {
		this.vusf = vusf;
	}
	public Integer getStp() {
		return stp;
	}
	public void setStp(Integer stp) {
		this.stp = stp;
	}
	public Integer getSup() {
		return sup;
	}
	public void setSup(Integer sup) {
		this.sup = sup;
	}
	public String getEtl_date() {
		return etl_date;
	}
	public void setEtl_date(String etl_date) {
		this.etl_date = etl_date;
	}
}
