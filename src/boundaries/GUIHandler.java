package boundaries;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

/**
 * 
 * @author gSoft Team
 * 
 */
public interface GUIHandler extends Runnable {

	void setMax(int numberOfCommandsToExecute);

	JProgressBar getProgressBar();

	void setCurrentFlow(String name, String fileName);

	JComboBox getSuccessListOfFlows();

	JComboBox getFailListOfFlows();

	void updateResult(int result);

	void setValue();

	public JFrame getFrame();

}
