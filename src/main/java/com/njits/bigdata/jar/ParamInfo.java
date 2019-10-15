package com.njits.bigdata.jar;

import java.util.List;

public class ParamInfo {

	private List<String> outParamTypeNames;
	
	private List<String> outParamFieldNames;
	
	private List<String> inParamTypeNames;
	
	private int ret;
	
	public List<String> getOutParamFieldNames() {
		return outParamFieldNames;
	}

	public void setOutParamFieldNames(List<String> outParamFieldNames) {
		this.outParamFieldNames = outParamFieldNames;
	}
	
	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public List<String> getOutParamTypeNames() {
		return outParamTypeNames;
	}

	public void setOutParamTypeNames(List<String> outParamTypeNames) {
		this.outParamTypeNames = outParamTypeNames;
	}

	public List<String> getInParamTypeNames() {
		return inParamTypeNames;
	}

	public void setInParamTypeNames(List<String> inParamTypeNames) {
		this.inParamTypeNames = inParamTypeNames;
	}
}
