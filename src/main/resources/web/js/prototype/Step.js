/* Step.js

		Purpose:
                
		Description:
                
		History:
				Thu Feb 21 11:35:49 CST 2019, Created by charlesqiu

Copyright (C) 2019 Potix Corporation. All Rights Reserved.

*/
(function () {
prototype.Step = zk.$extends(zul.Widget, {
	_complete: false,
	_error: false,
	$define: {
		title: function (v) {
			if (this.desktop)
				jq(this.$n('title')).text(v);
		},
		description: function (v) {
			if (this.desktop)
				jq(this.$n('description')).text(v);
		},
		complete: function (v, fromServer) {
			if (this.desktop) {
				jq(this.$n()).toggleClass(this.$s('complete'), v);
				this._adjustIconContent();
				if (!fromServer) {
					this.fire('onComplete', v);
				}
			}
		},
		error: function (v) {
			if (this.desktop) {
				jq(this.$n()).toggleClass(this.$s('error'), v);
				this._adjustIconContent();
			}
		},
		iconClass: function (v) {
			if (this.desktop)
				this._adjustIconContent();
		}
	},
	bind_: function () {
		this.$supers('bind_', arguments);
		if (!this.getStepper().isLinear())
			this.domListen_(this.$n(), 'onClick', '_active');
	},
	unbind_: function () {
		this.$supers('unbind_', arguments);
		if (!this.getStepper().isLinear())
			this.domUnlisten_(this.$n(), 'onClick', '_active');
	},
	_active: function () {
		this.getStepper().setCurrentIndex(this.getChildIndex());
	},
	_toggleActive: function (active) {
		jq(this.$n()).toggleClass(this.$s('active'), active);
		jq(this.$n('content')).toggleClass(this.$s('content-active'), active);
	},
	_adjustIconContent: function () {
		jq(this.$n('icon')).html(this._getIconContent());
	},
	_getIconContent: function () {
		if (this.isError())
			return '<i class="z-icon-exclamation"/>';
		if (this.isComplete())
			return '<i class="z-icon-check"/>';
		if (this.getIconClass())
			return '<i class="' + this.getIconClass() + '"/>';
		return this.getChildIndex() + 1;
	},
	_renderContent: function (out) {
		var cls = this.$s('content');
		if (this._isCurrentStep())
			cls += ' ' + this.$s('content-active');
		out.push('<div id="', this.uuid, '-content" class="', cls, '">');
		for (var w = this.firstChild; w; w = w.nextSibling)
			w.redraw(out);
		out.push('</div>');
	},
	_isCurrentStep: function () {
		return this.getStepper()._getCurrentStep() == this;
	},
	getStepper: function () {
		return this.parent;
	},
	domClass_: function () {
		var cls = this.$supers('domClass_');
		if (this.isComplete())
			cls += ' ' + this.$s('complete');
		if (this.isError())
			cls += ' ' + this.$s('error');
		if (this._isCurrentStep())
			cls += ' ' + this.$s('active');
		return cls;
	},
	getCaveNode: function () {
		return this.$n('content');
	}
});
})();