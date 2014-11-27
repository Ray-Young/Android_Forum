package com.obj;

import org.json.JSONObject;
// CompareObjTitle是为简化title排序而创建的对象
//其中包括两个元素
//JSONOBJECT是每个titleObject
//index是title的created_time
public class CompareObjTitle {
	private int index;
	private JSONObject jb;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public JSONObject getJb() {
		return jb;
	}
	public void setJb(JSONObject jb) {
		this.jb = jb;
	}
	
//	public CompareObj(String RESULT, int INDEX) {
//		this.index=INDEX;
//		this.result=RESULT;
//		
//		// TODO Auto-generated constructor stub
//	}
	

}
