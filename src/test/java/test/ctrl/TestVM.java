/* TestVM.java

		Purpose:
                
		Description:
                
		History:
				Thu Mar 07 09:57:45 CST 2019, Created by charlesqiu

Copyright (C) 2019 Potix Corporation. All Rights Reserved.
*/
package test.ctrl;

import prototype.Step;
import prototype.StepModel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

public class TestVM {
	private StepModel<TestItem> model;
	private boolean vertical;

	@Init
	public void init() {
		model = new StepModel<>();
		model.add(new TestItem("Step 1", "description 1", "content 1", false, false));
		model.add(new TestItem("Step 2", "description 2", "content 2", false, false));
		model.add(new TestItem("Step 3", "description 3", "content 3", false, false));
		model.setCurrentIndex(0);
	}

	@Command
	@NotifyChange("vertical")
	public void vertical() {
		vertical = !vertical;
	}

	@Command
	@NotifyChange("model")
	public void changeAllTitle() {
		model.getSteps().forEach(item -> item.setTitle("new " + item.getTitle()));
	}

	@Command
	public void onComplete(@BindingParam("step") Step step) {
		model.getSteps().get(step.getIndex()).setComplete(step.isComplete());
	}

	@Command
	public void back() {
		model.back();
	}

	@Command
	public void next() {
		model.next();
	}

	public StepModel<TestItem> getModel() {
		return model;
	}

	public boolean isVertical() {
		return vertical;
	}
}
