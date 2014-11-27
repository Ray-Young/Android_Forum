package com.helpers;

import java.util.ArrayList;

import com.obj.CompareObjTitle;

//在Helpers中的是在两个activity都会用到的一些函数
public class Helpers {
	// bundleSort，通过改写冒泡排序，将title的创建时间进行排序，直接调换JSONObject
	public static void bubbleSort(ArrayList<CompareObjTitle> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 0; j < list.size() - i - 1; j++) {
				//index所代表的内容是content的time_created
				if (list.get(j).getIndex() <= list.get(j + 1).getIndex()) {
					CompareObjTitle tmp = list.get(j);
					list.set(j, list.get(j + 1));
					list.set(j + 1, tmp);
				}
			}
		}
	}
    //init包括两步操作，读取JSON文件，读取本地的默认图片
}
