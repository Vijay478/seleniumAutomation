package com.ctylor.domain;

import java.util.LinkedList;
import java.util.List;

public class ResultObj {

	List<String> skippedElements = new LinkedList<String>();
	LinkedList<Items> items = new LinkedList<Items>();

	public List<String> getSkippedElements() {
		return skippedElements;
	}

	public void setSkippedElements(List<String> skippedElements) {
		this.skippedElements = skippedElements;
	}

	public LinkedList<Items> getItems() {
		return items;
	}

	public void setItems(LinkedList<Items> items) {
		this.items = items;
	}

}
