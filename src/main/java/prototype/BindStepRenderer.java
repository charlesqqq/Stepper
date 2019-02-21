/* BindStepRenderer.java

		Purpose:
                
		Description:
                
		History:
				Wed Mar 13 18:32:52 CST 2019, Created by charlesqiu

Copyright (C) 2019 Potix Corporation. All Rights Reserved.
*/
package prototype;

import java.util.Objects;

import org.zkoss.bind.Binder;
import org.zkoss.bind.impl.AbstractForEachStatus;
import org.zkoss.bind.impl.AbstractRenderer;
import org.zkoss.bind.impl.BinderImpl;
import org.zkoss.bind.impl.BinderUtil;
import org.zkoss.bind.impl.IndirectBinding;
import org.zkoss.bind.sys.TemplateResolver;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.util.ForEachStatus;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zul.Attributes;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;

public class BindStepRenderer extends AbstractRenderer implements StepRenderer {
	@Override
	public void render(Step step, final Object data, final int index) throws Exception {
		final Stepper stepper = step.getStepper();
		final Template template = resolveTemplate(stepper, stepper, data, index, stepper.getModel().size(), "model");
		final String oldId = step.getId();
		if (!Strings.isEmpty(oldId)) {
			step.setId("$$FAKE_ID$$");
		}
		if (template == null) {
			Label label = new Label(Objects.toString(data));
			label.setParent(step);
		} else {
			final ForEachStatus iterStatus = new AbstractForEachStatus() {
				private static final long serialVersionUID = 1L;

				public int getIndex() {
					return index;
				}

				public Object getCurrent() {
					return data;
				}

				public Integer getEnd() {
					throw new UiException("end attribute is not supported");
				}
			};

			final String var = (String) template.getParameters().get(EACH_ATTR);
			final String varnm = var == null ? EACH_VAR : var;
			final String itervar = (String) template.getParameters().get(STATUS_ATTR);
			final String itervarnm = itervar == null ? (var == null ? EACH_STATUS_VAR : varnm + STATUS_POST_VAR)
					: itervar;

			Object oldVar = stepper.getAttribute(varnm);
			Object oldIter = stepper.getAttribute(itervarnm);
			stepper.setAttribute(varnm, data);
			stepper.setAttribute(itervarnm, iterStatus);

			final Component[] items = filterOutShadows(stepper, template.create(stepper, step, null, null));

			if (oldVar == null) {
				stepper.removeAttribute(varnm);
			} else {
				stepper.setAttribute(varnm, oldVar);
			}
			if (oldIter == null) {
				stepper.removeAttribute(itervarnm);
			} else {
				stepper.setAttribute(itervarnm, oldIter);
			}

			if (items.length != 1)
				throw new UiException("The model template must have exactly one item, not " + items.length);

			final Step item = (Step) items[0];
			item.setAttribute(BinderImpl.VAR, varnm);
			item.setAttribute(varnm, data);
			item.setAttribute(AbstractRenderer.IS_TEMPLATE_MODEL_ENABLED_ATTR, true);
			item.setAttribute(AbstractRenderer.CURRENT_INDEX_RESOLVER_ATTR, new IndirectBinding(data) {
				public Binder getBinder() {
					return BinderUtil.getBinder(item, true);
				}

				protected ListModel getModel() {
					return stepper.getModel().getSteps();
				}

				public Component getComponent() {
					return item;
				}
			});
			addItemReference(stepper, item, index, varnm); //kept the reference to the data, before ON_BIND_INIT
			item.setAttribute(itervarnm, iterStatus);
			item.setAttribute(TemplateResolver.TEMPLATE_OBJECT, stepper.removeAttribute(TemplateResolver.TEMPLATE_OBJECT));
			addTemplateTracking(stepper, item, data, index, stepper.getModel().size());
			if ("$$FAKE_ID$$".equals(item.getId())) {
				item.setId(oldId);
			}
			step.setAttribute(Attributes.MODEL_RENDERAS, item);
			step.detach();
		}
	}
}
