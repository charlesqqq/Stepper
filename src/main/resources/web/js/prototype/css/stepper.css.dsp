<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

.z-stepper {
	background: #fff;
	overflow: hidden;
	position: relative;
	padding: 16px;
	box-shadow: 0px 3px 1px -2px rgba(0,0,0,0.2),
				0px 2px 2px 0px rgba(0,0,0,0.14),
				0px 1px 5px 0px rgba(0,0,0,0.12);
}

.z-stepper-toolbar {
	margin: 8px 0;
}

.z-stepper-toolbar-hide {
	display: none;
}

.z-stepper-button {
	margin-right: 8px;
}

.z-stepper-vertical .z-stepper-steps{
	display: flex;
	flex-direction: column;
}

.z-stepper-vertical .z-step-content-active {
	width: auto;
	margin-left: 36px;
	padding: 0;
}

.z-stepper-altLabels .z-step {
	flex-direction: column;
	align-self: flex-start;
}

.z-stepper-altLabels .z-step-icon {
	margin-right: 0;
	margin-bottom: 8px;
}

.z-stepper-linear .z-step {
	cursor: default;
}

.z-stepper-steps {
	align-items: stretch;
	display: flex;
	flex-wrap: wrap;
	justify-content: space-between;
	box-shadow: 0px 3px 1px -2px rgba(0,0,0,0.2),
				0px 2px 2px 0px rgba(0,0,0,0.14),
				0px 1px 5px 0px rgba(0,0,0,0.12);
}

.z-step {
	align-items: center;
	display: flex;
	flex-direction: row;
	padding: 16px;
	position: relative;
	cursor: pointer;
}

.z-step-icon {
	align-items: center;
	border-radius: 50%;
	display: inline-flex;
	font-size: 12px;
	justify-content: center;
	height: 24px;
	margin-right: 8px;
	min-width: 24px;
	width: 24px;
	color: #fff;
	background: rgba(0,0,0,0.38);
}

.z-step-label {
	align-items: center;
	display: flex;
	flex-direction: column;
	text-align: left;
	color: rgba(0,0,0,0.38);
}

.z-step-description {
	font-size: 14px;
}

.z-step-active > .z-step-icon {
	background-color: #1867c0;
	border-color: #1867c0;
}

.z-step-active > .z-step-label {
	color: black;
}

.z-step-complete > .z-step-icon {
	background-color: #1867c0;
	border-color: #1867c0;
}

.z-step-complete > .z-step-label {
	color: black;
}

.z-step-error > .z-step-icon {
	background-color: red;
}

.z-step-error > .z-step-label {
	color: red;
}

.z-stepper-contents {
	position: relative;
	overflow: hidden;
	box-shadow: 0px 3px 1px -2px rgba(0,0,0,0.2),
				0px 2px 2px 0px rgba(0,0,0,0.14),
				0px 1px 5px 0px rgba(0,0,0,0.12);
}

.z-step-content {
	display: none;
	top: 0;
	padding: 16px;
	flex: 1 0 auto;
	width: 100%;
	min-height: 200px;
}

.z-step-content-active {
	display: block;
}

