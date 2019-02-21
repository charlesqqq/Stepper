package prototype;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zkoss.xel.VariableResolver;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ShadowElementsCtrl;
import org.zkoss.zk.ui.util.ForEachStatus;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zul.Attributes;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.RendererCtrl;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.event.ListDataListener;
import org.zkoss.zul.event.ZulEvents;
import org.zkoss.zul.impl.XulElement;

public class Stepper extends XulElement {
	private static final Logger log = LoggerFactory.getLogger(Stepper.class);

	private boolean _altLabels;
	private boolean _vertical;
	private boolean _linear = true;
	private boolean _toolbarVisible = true;
	private int _currentIndex;
	private StepModel _model;
	private ListDataListener _listener;
	private StepRenderer _stepRenderer;
	private static final String ATTR_ON_INIT_RENDER_POSTED = "org.zkoss.zul.onInitLaterPosted";

	public boolean isAltLabels() {
		return _altLabels;
	}

	public void setAltLabels(boolean altLabels) {
		if (_altLabels != altLabels) {
			_altLabels = altLabels;
			smartUpdate("altLabels", _altLabels);
		}
	}

	public boolean isVertical() {
		return _vertical;
	}

	public void setVertical(boolean vertical) {
		if (_vertical != vertical) {
			_vertical = vertical;
			smartUpdate("vertical", _vertical);
		}
	}

	public boolean isLinear() {
		return _linear;
	}

	public void setLinear(boolean linear) {
		if (_linear != linear) {
			_linear = linear;
			smartUpdate("linear", _linear);
		}
	}

	public boolean isToolbarVisible() {
		return _toolbarVisible;
	}

	public void setToolbarVisible(boolean toolbarVisible) {
		if (_toolbarVisible != toolbarVisible) {
			_toolbarVisible = toolbarVisible;
			smartUpdate("toolbarVisible", _toolbarVisible);
		}
	}

	public int getCurrentIndex() {
		return _currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		if (currentIndex < 0 || currentIndex > getSteps().size() - 1)
			return;
		if (_currentIndex != currentIndex) {
			int diff = currentIndex - _currentIndex;
			if (this.isLinear()) {
				List<Step> steps = getSteps();
				if (diff < 0) {
					for (int i = 0; i <= Math.abs(diff); i++)
						steps.get(currentIndex + i).setComplete(false);
				} else {
					for (int i = 1; i <= diff; i++)
						steps.get(currentIndex - i).setComplete(true);
				}
			}
			updateCurrentIndex(currentIndex);
			if (_model != null)
				_model.setCurrentIndex(_currentIndex);
		}
	}

	private void updateCurrentIndex(int currentIndex) {
		_currentIndex = currentIndex;
		smartUpdate("currentIndex", _currentIndex);
	}

	public Step getCurrentStep() {
		return getSteps().get(getCurrentIndex());
	}

	public void setCurrentStep(Step step) {
		setCurrentIndex(getSteps().indexOf(step));
	}

	public StepRenderer getStepRenderer() {
		return _stepRenderer;
	}

	public void setStepRenderer(StepRenderer stepRenderer) {
		if (_stepRenderer != stepRenderer) {
			_stepRenderer = stepRenderer;
			if (_model != null)
				postOnInitRender();
		}
	}

	public StepModel getModel() {
		return _model;
	}

	public void setModel(StepModel model) {
		if (model != null) {
			if (_model != model) {
				if (_model != null)
					_model.getSteps().removeListDataListener(_listener);
				_model = model;
				initDataListener();
			}
			postOnInitRender();
		} else if (_model != null) {
			_model.getSteps().removeListDataListener(_listener);
			_model = null;
			getSteps().clear();
		}
	}

	private void initDataListener() {
		if (_listener == null) {
			final Stepper stepper = this;
			_listener = new ListDataListener() {
				@Override
				public void onChange(ListDataEvent event) {
					int newsz = _model.getSteps().getSize(), oldsz = getSteps().size(),
							min = event.getIndex0(), max = event.getIndex1(), cnt;
					switch (event.getType()) {
						case ListDataEvent.INTERVAL_ADDED:
							cnt = newsz - oldsz;
							if (cnt < 0)
								throw new UiException("Adding causes a smaller list?");
							if (cnt == 0)
								return;
							if (min < 0)
								if (max < 0)
									min = 0;
								else
									min = max - cnt + 1;
							if (min > oldsz)
								min = oldsz;

							final Renderer renderer = new Renderer();
							final Component next = min < oldsz ? getSteps().get(min) : null;
							int index = min;
							try {
								StepRenderer stepRenderer = null;
								while (--cnt >= 0) {
									if (stepRenderer == null)
										stepRenderer = getRealRenderer();
									Step step = new Step();
									step.applyProperties();
									insertBefore(step, next);
									renderer.render(step, _model.getSteps().get(index), index++);
								}
							} catch (Throwable ex) {
								renderer.doCatch(ex);
							} finally {
								renderer.doFinally();
							}
							break;
						case ListDataEvent.INTERVAL_REMOVED:
							cnt = oldsz - newsz;
							if (cnt < 0)
								throw new UiException("Removal causes a larger list?");
							if (cnt == 0)
								return;
							if (min >= 0)
								max = min + cnt - 1;
							else if (max < 0)
								max = cnt - 1;
							if (max > oldsz - 1)
								max = oldsz - 1;

							Component comp = getSteps().get(max);
							while (--cnt >= 0) {
								Component p = comp.getPreviousSibling();
								comp.detach();
								comp = p;
							}
							break;
						case ListDataEvent.SELECTION_CHANGED:
							updateCurrentIndex(min);
					}
				}
			};
		}
		_model.getSteps().addListDataListener(_listener);
	}

	private void postOnInitRender() {
		if (getAttribute(ATTR_ON_INIT_RENDER_POSTED) == null) {
			setAttribute(ATTR_ON_INIT_RENDER_POSTED, Boolean.TRUE);
			Events.postEvent("onInitRender", this, null);
		}
	}

	public void onInitRender() {
		removeAttribute(ATTR_ON_INIT_RENDER_POSTED);
		Renderer renderer = new Renderer();
		ListModelList steps = _model.getSteps();
		try {
			getChildren().clear();
			for (int i = 0; i < steps.size(); i++) {
				Step step = new Step();
				step.applyProperties();
				step.setParent(this);
				Object data = steps.getElementAt(i);
				renderer.render(step, data, i);
				Object v = step.getAttribute(Attributes.MODEL_RENDERAS);
				if (v != null)
					step = (Step) v;
				if (steps.isSelected(data))
					setCurrentStep(step);
			}
		} catch (Throwable ex) {
			renderer.doCatch(ex);
		} finally {
			renderer.doFinally();
		}
		Events.postEvent(ZulEvents.ON_AFTER_RENDER, this, null);
	}

	@Override
	public void onPageAttached(Page newpage, Page oldpage) {
		super.onPageAttached(newpage, oldpage);
		if (_model != null && _listener != null) {
			_model.getSteps().removeListDataListener(_listener);
			_model.getSteps().addListDataListener(_listener);
		}
	}

	@Override
	public void onPageDetached(Page page) {
		super.onPageDetached(page);
		if (_model != null && _listener != null) {
			_model.getSteps().removeListDataListener(_listener);
		}
	}

	private class Renderer implements java.io.Serializable {
		StepRenderer _renderer;
		private boolean _rendered, _ctrled;

		public Renderer() {
			_renderer = getRealRenderer();
		}

		public void render(Step step, Object data, int index) throws Throwable {
			if (!_rendered && (_renderer instanceof RendererCtrl)) {
				((RendererCtrl) _renderer).doTry();
				_ctrled = true;
			}
			try {
				_renderer.render(step, data, index);
			} catch (Throwable ex) {
				log.error("", ex);
				throw ex;
			}
			_rendered = true;
		}

		private void doCatch(Throwable ex) {
			if (_ctrled) {
				try {
					((RendererCtrl) _renderer).doCatch(ex);
				} catch (Throwable t) {
					throw UiException.Aide.wrap(t);
				}
			} else {
				throw UiException.Aide.wrap(ex);
			}
		}

		private void doFinally() {
			if (_ctrled)
				((RendererCtrl) _renderer).doFinally();
		}
	}

	public StepRenderer getRealRenderer() {
		return _stepRenderer != null ? _stepRenderer : _defaultRenderer;
	}

	private static final StepRenderer _defaultRenderer = new StepRenderer() {
		@Override
		public void render(Step step, final Object data, final int index) throws Exception {
			Stepper stepper = step.getStepper();
			final Template template = stepper.getTemplate("model");
			if (template == null) {
				Label label = new Label(Objects.toString(data));
				label.setParent(step);
			} else {
				final Component[] items = ShadowElementsCtrl
						.filterOutShadows(template.create(step.getParent(), step, new VariableResolver() {
							public Object resolveVariable(String name) {
								if ("each".equals(name)) {
									return data;
								} else if ("forEachStatus".equals(name)) {
									return new ForEachStatus() {

										public ForEachStatus getPrevious() {
											return null;
										}

										public Object getEach() {
											return getCurrent();
										}

										public int getIndex() {
											return index;
										}

										public Integer getBegin() {
											return 0;
										}

										public Integer getEnd() {
											throw new UnsupportedOperationException("end not available");
										}

										public Object getCurrent() {
											return data;
										}

										public boolean isFirst() {
											return getCount() == 1;
										}

										public boolean isLast() {
											return getIndex() + 1 == getEnd();
										}

										public Integer getStep() {
											return null;
										}

										public int getCount() {
											return getIndex() + 1;
										}
									};
								} else {
									return null;
								}
							}
						}, null));
				if (items.length != 1)
					throw new UiException("The model template must have exactly one item, not " + items.length);

				final Step newStep = (Step) items[0];
				step.setAttribute(Attributes.MODEL_RENDERAS, newStep);
				step.detach();
			}
		}
	};

	public void next() {
		setCurrentIndex(getCurrentIndex() + 1);
	}

	public void back() {
		setCurrentIndex(getCurrentIndex() - 1);
	}

	public List<Step> getSteps() {
		return getChildren();
	}

	@Override
	public void beforeChildAdded(Component child, Component insertBefore) {
		if (!(child instanceof Step))
			throw new UiException("Unsupported child for Stepper: " + child);
		super.beforeChildAdded(child, insertBefore);
	}

	protected void renderProperties(org.zkoss.zk.ui.sys.ContentRenderer renderer)
			throws java.io.IOException {
		super.renderProperties(renderer);
		render(renderer, "altLabels", _altLabels);
		render(renderer, "vertical", _vertical);
		if (_currentIndex != 0)
			render(renderer, "currentIndex", _currentIndex);
		if (!_linear)
			renderer.render("linear", false);
		if (!_toolbarVisible)
			renderer.render("toolbarVisible", false);
	}

	@Override
	protected void updateByClient(String name, Object value) {
		if ("currentIndex".equals(name) && value instanceof Integer) {
			_currentIndex = (Integer) value;
			if (_model != null)
				_model.setCurrentIndex(_currentIndex);
		} else
			super.updateByClient(name, value);
	}
}

