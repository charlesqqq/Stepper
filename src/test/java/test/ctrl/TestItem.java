/* TestItem.java

		Purpose:
                
		Description:
                
		History:
				Wed Mar 13 12:29:53 CST 2019, Created by charlesqiu

Copyright (C) 2019 Potix Corporation. All Rights Reserved.
*/
package test.ctrl;

public class TestItem {
	private String title;
	private String description;
	private String content;
	private boolean complete;
	private boolean error;

	public TestItem(String title, String description, String content, boolean complete, boolean error) {
		this.title = title;
		this.description = description;
		this.content = content;
		this.complete = complete;
		this.error = error;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
}
