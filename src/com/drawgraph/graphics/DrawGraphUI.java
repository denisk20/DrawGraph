package com.drawgraph.graphics;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import java.awt.*;
import javax.swing.*;

/**
 * Date: Oct 24, 2010
 * Time: 11:31:07 AM
 *
 * @author denisk
 */
public class DrawGraphUI {

	private JList chooseFileList;
	//	private JSplitPane verticalSplit;
	private JScrollPane chooseFileScrollPanel;
	private JPanel canvasPanel;
	private JPanel rootPanel;
	private JPanel mainPanel;
	private JPanel tweakPanel;


	public static void main(String[] args) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();

		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		JFrame frame = new JFrame("DrawGraphUI");
		DrawGraphUI ui = new DrawGraphUI();
		frame.setContentPane(ui.rootPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(800, 600);

		ui.initComponents();
		frame.setLocation((int) screenWidth / 2 - frame.getWidth() / 2, (int) screenHeight / 2 - frame.getHeight() / 2);
		frame.setVisible(true);
	}

	private void initComponents() {
		DefaultListModel listModel = new DefaultListModel();
		listModel.addElement("Hello!");
		listModel.addElement("World!");
		chooseFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		chooseFileList.setSelectedIndex(0);
		chooseFileList.setModel(listModel);

		//		verticalSplit.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		//		verticalSplit.setLeftComponent(chooseFileScrollPanel);
		//		verticalSplit.setRightComponent(canvasPanel);
	}


	{
		// GUI initializer generated by IntelliJ IDEA GUI Designer
		// >>> IMPORTANT!! <<<
		// DO NOT EDIT OR ADD ANY CODE HERE!
		$$$setupUI$$$();
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		rootPanel = new JPanel();
		rootPanel.setLayout(new FormLayout("center:d:grow", "center:36px:noGrow,top:8px:noGrow,center:d:grow"));
		mainPanel = new JPanel();
		mainPanel.setLayout(new FormLayout("center:150px:noGrow,center:d:grow", "center:d:grow"));
		CellConstraints cc = new CellConstraints();
		rootPanel.add(mainPanel, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
		canvasPanel = new JPanel();
		canvasPanel.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
		canvasPanel.setBackground(new Color(-52378));
		mainPanel.add(canvasPanel, cc.xy(2, 1, CellConstraints.FILL, CellConstraints.FILL));
		chooseFileScrollPanel = new JScrollPane();
		mainPanel.add(chooseFileScrollPanel, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
		chooseFileList = new JList();
		chooseFileScrollPanel.setViewportView(chooseFileList);
		tweakPanel = new JPanel();
		tweakPanel.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
		tweakPanel.setBackground(new Color(-3342388));
		rootPanel
				.add(tweakPanel, new CellConstraints(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL, new Insets(5, 5, 0, 5)));
		tweakPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
	}

	/** @noinspection ALL */
	public JComponent $$$getRootComponent$$$() {
		return rootPanel;
	}
}