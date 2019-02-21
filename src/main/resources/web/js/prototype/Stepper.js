(function () {

prototype.Stepper = zk.$extends(zul.Widget, {
	_altLabels: false,
	_linear: true,
	_vertical: false,
	_toolbarVisible: true,
	_currentIndex: 0,
	$define: {
		altLabels: function (v) {
			if (this.desktop)
				jq(this.$n()).toggleClass(this.$s('altLabels'), v);
		},
		vertical: function (v) {
			if (this.desktop)
				this.rerender();
		},
		toolbarVisible: function (v) {
			if (this.desktop)
				this._toggleToolbar();
		},
		linear: function (v) {
			if (this.desktop)
				this.rerender();
		}
	},
	getCurrentIndex: function () {
		return this._currentIndex;
	},
	setCurrentIndex: function (index, fromServer) {
		var currentIndex = this.getCurrentIndex();
		if (currentIndex != index) {
			var lastStep = this._getStepAt(currentIndex);
			if (lastStep)
				lastStep._toggleActive(false);
			var newStep = this._getStepAt(index);
			if (newStep)
				newStep._toggleActive(true);
			this._currentIndex = index;
			if (!fromServer)
				this.smartUpdate('currentIndex', index);
		}
	},
	_getCurrentStep: function () {
		return this._getStepAt(this.getCurrentIndex());
	},
	bind_: function () {
		this.$supers('bind_', arguments);
		this._toggleToolbar();
		this.domListen_(this.$n('next'), 'onClick', '_next')
			.domListen_(this.$n('back'), 'onClick', '_back');
	},
	unbind_: function () {
		this.domUnlisten_(this.$n('back'), 'onClick', '_back')
			.domUnlisten_(this.$n('next'), 'onClick', '_next');
		this.$supers('unbind_', arguments);
	},
	_toggleToolbar: function () {
		jq(this.$n('toolbar')).toggleClass(this.$s('toolbar-hide'), !this.isToolbarVisible());
	},
	_next: function () {
		var currentIndex = this.getCurrentIndex(),
			nextIndex = currentIndex + 1;
		if (nextIndex > this.nChildren - 1)
			return;
		if (this.isLinear())
			this._getStepAt(currentIndex).setComplete(true);
		this.setCurrentIndex(nextIndex);
	},
	_back: function () {
		var currentIndex = this.getCurrentIndex(),
			backIndex = currentIndex - 1;
		if (backIndex < 0)
			return;
		if (this.isLinear())
			this._getStepAt(backIndex).setComplete(false);
		this.setCurrentIndex(backIndex);
	},
	_getStepAt: function (index) {
		return this.getChildAt(index);
	},
	domClass_: function () {
		var cls = this.$supers('domClass_');
		if (this.isVertical())
			cls += ' ' + this.$s('vertical');
		if (this.isAltLabels())
			cls += ' ' + this.$s('altLabels');
		if (this.isLinear())
			cls += ' ' + this.$s('linear');
		return cls;
	},
	onChildAdded_: function (child) {
		this.$supers('onChildAdded_', arguments);
		this.rerender();
	},
	onChildRemoved_: function (child) {
		this.$supers('onChildRemoved_', arguments);
		this.rerender();
	}
});
})();