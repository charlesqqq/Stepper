/* StepRenderer.java

		Purpose:
                
		Description:
                
		History:
				Tue Mar 12 15:52:31 CST 2019, Created by charlesqiu

Copyright (C) 2019 Potix Corporation. All Rights Reserved.
*/
package prototype;

public interface StepRenderer<T> {
	void render(Step step, T data, int index) throws Exception;
}
