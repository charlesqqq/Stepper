package test.ctrl;

import java.util.List;

import prototype.Step;
import prototype.StepModel;
import prototype.Stepper;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;

public class TestComposer extends SelectorComposer {
	@Wire
	private Stepper stepper;
	@Wire
	private Stepper stepper2;
	@Wire
	private Label answer;
	private StepModel<Integer> model;
	private int count = 1;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		model = new StepModel<>();
		while (count <= 3)
			model.add(count++);
		model.setCurrentIndex(0);
		stepper.setModel(model);
		stepper2.setModel(model);
		stepper2.setStepRenderer((step, data, index) -> {
			step.setTitle("renderer title " + data);
			step.setDescription("renderer description " + data);
			Textbox textbox = new Textbox();
			textbox.setPlaceholder("change title");
			textbox.addEventListener(Events.ON_CHANGE,
					(EventListener<InputEvent>) event -> step.setTitle(event.getValue()));
			Checkbox checkbox = new Checkbox("error " + data);
			checkbox.addEventListener(Events.ON_CHECK,
					(EventListener<CheckEvent>) event -> step.setError(event.isChecked()));
			step.appendChild(textbox);
			step.appendChild(checkbox);
		});
	}

	@Listen("onClick = #modelAdd")
	public void modelAdd() {
		model.add(count++);
	}

	@Listen("onClick = #modelRemove")
	public void modelRemove() {
		ListModelList<Integer> steps = model.getSteps();
		model.remove(steps.size() - 1);
		count--;
	}

	@Listen("onClick = #modelNext")
	public void modelNext() {
		model.next();
	}

	@Listen("onClick = #modelBack")
	public void modelBack() {
		model.back();
	}

	@Listen("onClick = #modelIndex2")
	public void modelIndex2() {
		model.setCurrentIndex(2);
	}

	@Listen("onClick = #modelGetIndex")
	public void modelGetIndex() {
		answer.setValue(String.valueOf(model.getCurrentIndex()));
	}

	@Listen("onClick = #vertical")
	public void vertical() {
		stepper.setVertical(!stepper.isVertical());
	}

	@Listen("onClick = #altLabels")
	public void altLabels() {
		stepper.setAltLabels(!stepper.isAltLabels());
	}

	@Listen("onClick = #toolbarVisible")
	public void toolbarVisible() {
		stepper.setToolbarVisible(!stepper.isToolbarVisible());
	}

	@Listen("onClick = #linear")
	public void linear() {
		stepper.setLinear(!stepper.isLinear());
	}

	@Listen("onClick = #back")
	public void back() {
		stepper.back();
	}

	@Listen("onClick = #next")
	public void next() {
		stepper.next();
	}

	@Listen("onClick = #getCurrentIndex")
	public void getCurrentIndex() {
		answer.setValue(String.valueOf(stepper.getCurrentIndex()));
	}

	@Listen("onClick = #isComplete1")
	public void isComplete1() {
		answer.setValue(String.valueOf(stepper.getSteps().get(0).isComplete()));
	}

	@Listen("onClick = #isComplete2")
	public void isComplete2() {
		answer.setValue(String.valueOf(stepper.getSteps().get(1).isComplete()));
	}

	@Listen("onClick = #isComplete3")
	public void isComplete3() {
		answer.setValue(String.valueOf(stepper.getSteps().get(2).isComplete()));
	}

	@Listen("onClick = #setCurrentIndex0")
	public void setCurrentIndex0() {
		stepper.setCurrentIndex(0);
	}

	@Listen("onClick = #setCurrentIndex1")
	public void setCurrentIndex1() {
		stepper.setCurrentIndex(1);
	}

	@Listen("onClick = #setCurrentIndex2")
	public void setCurrentIndex2() {
		stepper.setCurrentIndex(2);
	}

	@Listen("onClick = #setComplete1")
	public void setComplete1() {
		Step step = stepper.getSteps().get(0);
		step.setComplete(!step.isComplete());
	}

	@Listen("onClick = #setComplete2")
	public void setComplete2() {
		Step step = stepper.getSteps().get(1);
		step.setComplete(!step.isComplete());
	}

	@Listen("onClick = #setComplete3")
	public void setComplete3() {
		Step step = stepper.getSteps().get(2);
		step.setComplete(!step.isComplete());
	}

	@Listen("onClick = #addStep")
	public void addStep() {
		List<Step> steps = stepper.getSteps();
		int index = steps.size() + 1;
		Step step = new Step("Step " + index, "description " + index);
		step.appendChild(new Label("Content " + index));
		step.appendChild(new Textbox());
		stepper.appendChild(step);
	}

	@Listen("onClick = #removeStep")
	public void removeStep() {
		stepper.removeChild(stepper.getLastChild());
	}

	@Listen("onClick = #detach")
	public void detach() {
		stepper.detach();
	}

	@Listen("onClick = #setError1")
	public void setError1() {
		Step step = stepper.getSteps().get(0);
		step.setError(!step.isError());
	}

	@Listen("onClick = #setError2")
	public void setError2() {
		Step step = stepper.getSteps().get(1);
		step.setError(!step.isError());
	}

	@Listen("onClick = #setError3")
	public void setError3() {
		Step step = stepper.getSteps().get(2);
		step.setError(!step.isError());
	}
}