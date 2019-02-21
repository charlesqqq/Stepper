/* step.js

		Purpose:
                
		Description:
                
		History:
				Thu Feb 21 11:36:50 CST 2019, Created by charlesqiu

Copyright (C) 2019 Potix Corporation. All Rights Reserved.

*/
function (out) {
	var uuid = this.uuid,
		stepper = this.getStepper(),
		contents = stepper.$n('contents');

	out.push('<div', this.domAttrs_(), '>');
	out.push('<span id="', uuid, '-icon" class="', this.$s('icon'), '">', this._getIconContent(), '</span>');
	out.push('<div class="', this.$s('label'), '">');
	out.push('<div id="', uuid, '-title" class="', this.$s('title'), '">', this.getTitle(), '</div>');
	out.push('<div id="', uuid, '-description" class="', this.$s('description'), '">', this.getDescription(), '</div></div>');
	out.push('</div>');

	if (stepper.isVertical()) {
		this._renderContent(out);
	} else if (contents) {
		var buffer = new zk.Buffer();
		this._renderContent(buffer);
		jq(contents).append(buffer.join(''));
	}
}