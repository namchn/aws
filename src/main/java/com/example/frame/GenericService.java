package com.example.frame;

import java.util.ArrayList;
import java.util.List;

public class GenericService {

	public GenericService() {
		// TODO Auto-generated constructor stub
	}

	public <T> List<T> ccc2(T o) {
		ArrayList<T> al = new ArrayList();
		al.add(o);
		return al;
	}

	public <T> T ccc(T o) {
		return o;
	}

}
