package boundaries;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * 
 * @author gSoft Team
 * 
 */
public class ProgressBar extends Thread implements GUIHandler, Runnable {

	private static int successFlag = 0;
	private static int failFlag = 0;

	private static int executed = 0;
	private static int expected = 0;
	private static int count = 0;
	private static int flowsCount = 0;
	private static int successCount = 0;
	private static int failedCount = 0;
	private static long startTime;
	private static int max;
	public JLabel percentage;
	public JLabel currentFlow;
	public JLabel testNumber;
	public JLabel time;
	public JLabel success;
	public JLabel failed;
	public JProgressBar progressBar;
	public JComboBox failListOfFlows;
	public JComboBox successListOfFlows;
	private JFrame frame;

	public ProgressBar(ProgressBar guiHandler) {
		guiHandler = this;
	}

	public ProgressBar() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		setStartTime(System.currentTimeMillis());
		progressBar = new JProgressBar(0, 500);
		progressBar.setSize(5, 5);
		progressBar.setBackground(new Color(255, 255, 255));
		progressBar.setBorderPainted(true);
		progressBar.setForeground(new Color(0, 113, 0));

		JPanel percentagePanel = new JPanel(new GridLayout(1, 2));
		percentagePanel.add(new JLabel("Percentage:"));
		percentage = new JLabel("0%");
		percentage.setFont(new Font(null, 2, 14));
		percentagePanel.add(percentage);
		percentagePanel.setBackground(new Color(150, 150, 150));

		JPanel currentFlowPanel = new JPanel(new GridLayout(1, 2));
		currentFlowPanel.add(new JLabel("Current Flow:"));
		currentFlow = new JLabel("ddd");
		currentFlow.setFont(new Font(null, 2, 13));
		currentFlowPanel.add(currentFlow);
		currentFlowPanel.setBackground(new Color(150, 150, 150));

		JPanel statPanel = new JPanel(new GridLayout(1, 4));
		statPanel.add(new JLabel("Test Number:"));
		testNumber = new JLabel("1");
		testNumber.setFont(new Font(null, 2, 14));
		statPanel.add(testNumber);

		statPanel.add(new JLabel("Elapsed Time:"));
		time = new JLabel("0");
		time.setFont(new Font(null, 2, 14));
		statPanel.add(time);
		statPanel.setBackground(new Color(150, 150, 150));

		JPanel successFailPanel = new JPanel(new GridLayout(1, 2));
		successFailPanel.setBorder(new LineBorder(new Color(121, 121, 121), 2));
		successFailPanel.add(new JLabel("Success:"));
		success = new JLabel("0");
		success.setToolTipText("Click here to show the successfull flows.");
		success.setForeground(new Color(15, 107, 1));
		successFailPanel.add(success);

		successFailPanel.add(new JLabel("Failed:"));
		failed = new JLabel("0");
		failed.setToolTipText("Click here to show the failed flows.");
		failed.setForeground(new Color(255, 0, 0));
		successFailPanel.add(failed);
		successFailPanel.setBackground(new Color(242, 245, 116));

		successListOfFlows = new JComboBox(new String[] {});
		successListOfFlows.setForeground(new Color(0, 128, 0));
		successListOfFlows.setFont(new Font(null, 1, 10));

		failListOfFlows = new JComboBox(new String[] {});
		failListOfFlows.setForeground(new Color(255, 0, 0));
		failListOfFlows.setFont(new Font(null, 1, 10));

		final JPanel content = new JPanel(new GridLayout(5, 1, 1, 1));
		content.add(progressBar);
		content.add(percentagePanel);
		content.add(currentFlowPanel);
		content.add(statPanel);
		content.add(successFailPanel);

		Border border = new LineBorder(new Color(0, 0, 240), 1);
		content.setBorder(border);

		frame = new JFrame("Progress Bar - The Climate Corporation");
		frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		frame.setAlwaysOnTop(true);

		frame.setUndecorated(true);
		frame.add(content);
		frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		frame.setSize(455, 128);
		frame.setResizable(false);
		frame.setVisible(true);

		failed.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (failFlag == 0) {
					if (successFlag == 1)
						content.remove(successListOfFlows);
					content.setLayout(new GridLayout(6, 1, 1, 1));
					content.add(failListOfFlows);
					failFlag = 1;
				} else if (failFlag == 1) {
					content.setLayout(new GridLayout(5, 1, 1, 1));
					failFlag = 0;
					content.remove(failListOfFlows);
				}
				successFlag = 0;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		success.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (successFlag == 0) {
					if (failFlag == 1)
						content.remove(failListOfFlows);
					content.setLayout(new GridLayout(6, 1, 1, 1));
					content.add(successListOfFlows);
					successFlag = 1;
				} else if (successFlag == 1) {
					content.setLayout(new GridLayout(5, 1, 1, 1));
					successFlag = 0;
					content.remove(successListOfFlows);
				}
				failFlag = 0;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void setMax(int max) {
		progressBar.setMinimum(1);
		progressBar.setMaximum(max + 1);
		ProgressBar.max = max;
	}

	public static int getMax() {
		return max;
	}

	public static void setStartTime(long startTime) {
		ProgressBar.startTime = startTime;
	}

	public static long getStartTime() {
		return startTime;
	}

	public static void setCount(int count) {
		ProgressBar.count = count;
	}

	public static int getCount() {
		return count;
	}

	@Override
	public void updateResult(int result) {
		if (result == 1)
			success.setText("" + (++successCount));
		else if (result == 0)
			failed.setText("" + (++failedCount));
	}

	private static String getTimeDuration(long endTime, long startTime) {

		float duration = endTime - startTime;

		if (duration > 3600000)
			return "" + getDuration(duration / 3600000) + " hours";
		else if (duration > 60000)
			return "" + getDuration(duration / 60000) + " minutes";
		else if (duration > 1000)
			return "" + getDuration(duration / 1000) + " seconds";
		else
			return "" + getDuration(duration) + " millis";
	}

	private static String getDuration(float duration) {
		Float float1 = new Float(duration);
		String float2 = String.valueOf(float1);
		return (float2.length() > 4 ? float2.substring(0, 4) : float2);
	}

	@Override
	public void setCurrentFlow(String name, String fileName) {
		currentFlow.setText("" + name);
		currentFlow.setToolTipText(fileName);
		testNumber.setText("" + (++flowsCount));
	}

	@Override
	public void setValue() {
		time.setText(getTimeDuration(System.currentTimeMillis(), startTime));
		progressBar.setValue(++count);
		setExecuted(count);
		Float float1 = new Float(
				(((float) (progressBar.getValue()) / (float) (progressBar
						.getMaximum())) * 100));
		String float2 = String.valueOf(float1);
		percentage
				.setText(""
						+ (float2.length() > 4 ? float2.substring(0, 4)
								: float2) + "%");

	}

	public static void setExecuted(int executed) {
		ProgressBar.executed = executed;
	}

	public static int getExecuted() {
		return executed;
	}

	public static void setExpected(int expected) {
		ProgressBar.expected = expected;
	}

	public static int getExpected() {
		return expected;
	}

	@Override
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	@Override
	public synchronized JComboBox getSuccessListOfFlows() {
		return successListOfFlows;
	}

	public synchronized void setSuccessListOfFlows(JComboBox successListOfFlows) {
		this.successListOfFlows = successListOfFlows;
	}

	@Override
	public synchronized JComboBox getFailListOfFlows() {
		return failListOfFlows;
	}

	public synchronized void setFailListOfFlows(JComboBox failListOfFlows) {
		this.failListOfFlows = failListOfFlows;
	}

	@Override
	public JFrame getFrame() {
		return frame;
	}

}
