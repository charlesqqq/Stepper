function (out) {
	var uuid = this.uuid;

	out.push('<div', this.domAttrs_(), '>');
	out.push('<div id="', uuid, '-steps" class="', this.$s('steps'), '">');
	for (var step = this.firstChild; step; step = step.nextSibling)
		step.redraw(out);
	out.push('</div>');
	if (!this.isVertical()) {
		out.push('<div id="', uuid, '-contents" class="', this.$s('contents'), '">');
		for (var step = this.firstChild; step; step = step.nextSibling) {
			step._renderContent(out);
		}
		out.push('</div>');
	}
	out.push('<div id="', uuid, '-toolbar" class="', this.$s('toolbar'), '">');
	out.push('<button id="', uuid, '-back" class="', this.$s('button'), '">Back</button>');
	out.push('<button id="', uuid, '-next" class="', this.$s('button'), '">Next</button>');
	out.push('</div></div>');
}