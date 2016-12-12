package com.bmc.cloud.bsasimulator.resources;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class VMDetails {
	@XmlElement
	private String name;
	@XmlElement
	private String OSname;
	@XmlElement
	private int NICCount;
	@XmlElement
	private int ram;
	@XmlElement
	private int storage;

	public VMDetails() {
		// TODO Auto-generated constructor stub
	}
	public VMDetails(String name,String OSname,int NICcount,int ram,int storage){
		this.name=name;
		this.OSname=OSname;
		this.NICCount=NICcount;
		this.ram=ram;
		this.storage=storage;
	}
}
