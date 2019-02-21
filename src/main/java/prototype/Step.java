/* Step.java

		Purpose:
                
		Description:
                
		History:
				Thu Feb 21 11:36:25 CST 2019, Created by charlesqiu

Copyright (C) 2019 Potix Corporation. All Rights Reserved.
*/
package prototype;


import java.util.Objects;

import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.impl.XulElement;

public class Step extends XulElement {
	private String _title;
	private String _description;
	private String _iconClass;
	private boolean _complete;
	private boolean _error;

	static {
		addClientEvent(Step.class, "onComplete", 0);
	}

	public Step() {
	}

	public Step(String title) {
		_title = title;
	}

	public Step(String title, String description) {
		_title = title;
		_description = description;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String title) {
		if (!Objects.equals(_title, title)) {
			_title = title;
			smartUpdate("title", _title);
		}
	}

	public String getDdescription() {
		return _description;
	}

	public void setDescription(String description) {
		if (!Objects.equals(_description, description)) {
			_description = description;
			smartUpdate("description", _description);
		}
	}

	public String getIconClass() {
		return _iconClass;
	}

	public void setIconClass(String iconClass) {
		if (!Objects.equals(_iconClass, iconClass)) {
			_iconClass = iconClass;
			smartUpdate("iconClass", _iconClass);
		}
	}

	public boolean isComplete() {
		return _complete;
	}

	public void setComplete(boolean complete) {
		if (_complete != complete) {
			_complete = complete;
			smartUpdate("complete", _complete);
		}
	}

	public boolean isError() {
		return _error;
	}

	public void setError(boolean error) {
		if (_error != error) {
			_error = error;
			smartUpdate("error", _error);
		}
	}

	public Stepper getStepper() {
		return (Stepper) getParent();
	}

	public int getIndex() {
		Stepper stepper = getStepper();
		return stepper.getChildren().indexOf(this);
	}

	public void beforeParentChanged(Component parent) {
		if (parent != null && !(parent instanceof Stepper))
			throw new UiException("Wrong parent: " + parent);
		super.beforeParentChanged(parent);
	}

	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
			throws java.io.IOException {
		super.renderProperties(renderer);
		render(renderer, "title", _title);
		render(renderer, "description", _description);
		render(renderer, "iconClass", _iconClass);
		render(renderer, "complete", _complete);
		render(renderer, "error", _error);
	}

	@Override
	public void service(AuRequest request, boolean everError) {
		if ("onComplete".equals(request.getCommand())) {
			CheckEvent evt = CheckEvent.getCheckEvent(request);
			_complete = evt.isChecked();
			Events.postEvent(evt);
		} else
			super.service(request, everError);
	}
}
