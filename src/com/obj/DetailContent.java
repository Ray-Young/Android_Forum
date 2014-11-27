package com.obj;

import java.io.Serializable;

import org.json.JSONObject;
//DetailContent是用作传输Overview_Activity到DetailActivity的对象
//它在创建bubble时使用
public class DetailContent implements Serializable {
	private JSONObject jb;
	private int pos;

	public JSONObject getJb() {
		return jb;
	}

	public void setJb(JSONObject jb) {
		this.jb = jb;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

}
