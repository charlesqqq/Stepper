/* StepModel.java

		Purpose:
                
		Description:
                
		History:
				Thu Mar 07 09:57:13 CST 2019, Created by charlesqiu

Copyright (C) 2019 Potix Corporation. All Rights Reserved.
*/
package prototype;

import org.zkoss.zul.ListModelList;

public class StepModel<T> {

	private ListModelList<T> steps = new ListModelList<>();

	public void back() {
		setCurrentIndex(getCurrentIndex() - 1);
	}

	public void next() {
		setCurrentIndex(getCurrentIndex() + 1);
	}

	public void setCurrentIndex(int index) {
		if (index < 0 || index > steps.size() - 1)
			return;
		steps.addToSelection(steps.get(index));
	}

	public void setCurrentStep(T item) {
		steps.addToSelection(item);
	}

	public void add(T item) {
		steps.add(item);
	}

	public void add(int index, T item) {
		steps.add(index, item);
	}

	public void remove(int index) {
		steps.remove(index);
	}

	public void remove(T item) {
		steps.remove(item);
	}

	public int getCurrentIndex() {
		return steps.indexOf(getCurrentStep());
	}

	public T getCurrentStep() {
		return steps.getSelection().iterator().next();
	}

	public ListModelList<T> getSteps() {
		return steps;
	}

	public int size() {
		return steps.size();
	}
}