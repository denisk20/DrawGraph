package com.drawgraph.graphics;

import com.drawgraph.algorithms.AbstractCrossingReducer;
import com.drawgraph.algorithms.BarycenterReducer;
import com.drawgraph.algorithms.CoffmanGrahamLayeredGraphOrder;
import com.drawgraph.algorithms.CrossingReducer;
import com.drawgraph.algorithms.LayeredGraphOrder;
import com.drawgraph.algorithms.MedianReducer;
import com.drawgraph.algorithms.SimpleLayeredGraphOrder;
import com.drawgraph.algorithms.UnexpectedCycledGraphException;
import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.Node;
import com.drawgraph.parser.GraphMLParser;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Date: Oct 24, 2010
 * Time: 11:31:07 AM
 *
 * @author denisk
 */
public class DrawGraphUI implements ChangeListener, ActionListener, ListSelectionListener {

	private JList chooseFileList;
	//	private JSplitPane verticalSplit;
	private JScrollPane chooseFileScrollPanel;
	private JPanel rootPanel;
	private JPanel mainPanel;
	private JPanel tweakPanel;
	private JButton directoryChooseButton;
	private JRadioButton barycenterRadioButton;
	private JRadioButton medianRadioButton;
	private JSlider layerLengthSlider;
	private JPanel canvasPanel;
	private JPanel optionsPane;
	private JScrollPane canvasScrollPane;
	private JSpinner distanceSpin;
	private JSpinner radiusSpin;
	private JSpinner leftOffsetSpin;
	private JSpinner topOffsetSpin;
	private JSpinner layerOffsetSpin;
	private JCheckBox coffmanGrahamLayeringCheckBox;
	private JRadioButton noneRadioButton;

	private final String DIGRAPHS = "data/digraphs";
	private Graph<Node> graph;
	private LayeredPositionedGraph layeredPositionedGraph;
	private SimpleGraphDrawer drawer;

	private File currentDirectory;
	private String currentFilePath;
	private GraphMLParser parser;

	private static final int MAXIMUM_RADIUS = 100;
	private static final int INITIAL_DISTANCE = 100;
	private static final int MINIMUM_OFFSET = 10;
	private static final int MINIMUM_DISTANCE = 10;
	private static final int MAXIMUM_LAYER_OFFSET = 500;
	private static final int MAXIMUM_DISTANCE = 500;
	private static final int MINIMUM_LAYERS_COUNT = 0;
	private static final int DISTANCE_STEP_SIZE = 1;
	private static final int MINIMUM_LAYER_OFFSET = 20;
	private static final int INITIAL_LEFT_OFFSET = 125;
	private static final int INITIAL_TOP_OFFSET = 50;
	private static final int INITIAL_RADIUS = 20;
	private static final int MINIMUM_RADIUS = 1;
	private static final int RADIUS_STEP_SIZE = 1;
	private static final int MAXIMUM_OFFSET = 200;
	private static final int OFFSET_STEP_SIZE = 1;
	private static final int INITIAL_LAYER_OFFSET = 50;
	private static final int LAYER_OFFSET_STEP_SIZE = 1;
	private static final int INITIAL_LAYERS_COUNT = 3;
	private static final int MAXIMUM_LAYERS_COUNT = 50;
	private static final int MAJOR_TICK_SPACING = 10;
	private static final int MINOR_TICK_SPACING = 1;

	private static final int FRAME_WIDTH = 960;
	private static final int FRAME_HEIGHT = 700;


	private boolean useGrahamLayering = false;

	private static final int NO_REDUCTION_METHOD = 0;
	private static final int MEDIAN_METHOD = 1;
	private static final int BARYCENTER_METHOD = 2;
	private int currentReductionMethod = NO_REDUCTION_METHOD;


	private static JFrame frame;

	public static final String GRAPHML_EXT = ".graphml";

	private FilenameFilter graphMLFilenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(GRAPHML_EXT);
		}
	};
	private static final String FILE_CHOOSER_TITLE = "Choose a directory to fetch resources from";
	private CrossingReducer medianReducer = new MedianReducer();
	private CrossingReducer barycenterReducer = new BarycenterReducer();

	private LayeredGraphOrder<Node> simpleOrder = new SimpleLayeredGraphOrder(0);
	private LayeredGraphOrder<Node> coffmanGrahamOrder = new CoffmanGrahamLayeredGraphOrder(0);

	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					createAndShowUI();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void createAndShowUI() throws IOException, SAXException, ParserConfigurationException {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();

		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		frame = new JFrame("DrawGraphUI");
		DrawGraphUI ui = new DrawGraphUI();
		frame.setContentPane(ui.rootPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ui.initSpinners();
		ui.initCanvas();

		frame.pack();
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setLocation((int) screenWidth / 2 - frame.getWidth() / 2, (int) screenHeight / 2 - frame.getHeight() / 2);
		frame.setVisible(true);
	}

	private void initCanvas() throws IOException, SAXException, ParserConfigurationException {
		drawer = new SimpleGraphDrawer();
		currentDirectory = new File(DIGRAPHS);
		if (!currentDirectory.exists()) {
			throw new IllegalStateException("No directory found: " + DIGRAPHS);
		}

		DefaultListModel listModel = new DefaultListModel();
		chooseFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		chooseFileList.setModel(listModel);

		fillFileList(currentDirectory);
		chooseFileList.setSelectedIndex(0);

		frame.setTitle(currentDirectory.getAbsolutePath());
	}

	private void parseFileFromList() throws IOException, SAXException, ParserConfigurationException {
		int selectedIndex = chooseFileList.getSelectedIndex();
		if (selectedIndex == -1) {
			selectedIndex = 0;
		}
		DefaultListModel listModel = (DefaultListModel) chooseFileList.getModel();
		String selectedFile = (String) listModel.get(selectedIndex);
		String path = currentDirectory + File.separator + selectedFile;

		currentFilePath = path;
		parser = new GraphMLParser();
		Graph<Node> g = parser.buildGraph(currentFilePath);
		graph = g;
		layeredPositionedGraph = scaleGraph(g);
	}

	private void initSpinners() {
		SpinnerModel distanceModel = new SpinnerNumberModel(INITIAL_DISTANCE, MINIMUM_DISTANCE, MAXIMUM_DISTANCE, DISTANCE_STEP_SIZE);
		distanceSpin.setModel(distanceModel);

		SpinnerModel radiusModel = new SpinnerNumberModel(INITIAL_RADIUS, MINIMUM_RADIUS, MAXIMUM_RADIUS, RADIUS_STEP_SIZE);
		radiusSpin.setModel(radiusModel);

		SpinnerModel leftOffsetModel = new SpinnerNumberModel(INITIAL_LEFT_OFFSET, MINIMUM_OFFSET, MAXIMUM_OFFSET, OFFSET_STEP_SIZE);
		SpinnerModel topOffsetModel = new SpinnerNumberModel(INITIAL_TOP_OFFSET, MINIMUM_OFFSET, MAXIMUM_OFFSET, OFFSET_STEP_SIZE);
		leftOffsetSpin.setModel(leftOffsetModel);
		topOffsetSpin.setModel(topOffsetModel);

		SpinnerModel layerOffsetModel = new SpinnerNumberModel(INITIAL_LAYER_OFFSET, MINIMUM_LAYER_OFFSET, MAXIMUM_LAYER_OFFSET, LAYER_OFFSET_STEP_SIZE);
		layerOffsetSpin.setModel(layerOffsetModel);

		layerLengthSlider.setValue(INITIAL_LAYERS_COUNT);
		layerLengthSlider.setMaximum(MAXIMUM_LAYERS_COUNT);
		layerLengthSlider.setMinimum(MINIMUM_LAYERS_COUNT);
		layerLengthSlider.setMajorTickSpacing(MAJOR_TICK_SPACING);
		layerLengthSlider.setMinorTickSpacing(MINOR_TICK_SPACING);
		final Hashtable<Integer, JLabel> hashtable = new Hashtable<Integer, JLabel>();
		for (int i = MINIMUM_LAYERS_COUNT; i <= MAXIMUM_LAYERS_COUNT; i++) {
			if (i % 10 == 0) {
				hashtable.put(new Integer(i), new JLabel(Integer.toString(i)));
			}
		}
		layerLengthSlider.setLabelTable(hashtable);
		layerLengthSlider.setPaintLabels(true);
		layerLengthSlider.setPaintTicks(true);
		layerLengthSlider.setPaintTrack(true);

		distanceSpin.addChangeListener(this);
		radiusSpin.addChangeListener(this);
		leftOffsetSpin.addChangeListener(this);
		topOffsetSpin.addChangeListener(this);
		layerOffsetSpin.addChangeListener(this);
		layerLengthSlider.addChangeListener(this);

		directoryChooseButton.addActionListener(this);

		chooseFileList.addListSelectionListener(this);

		barycenterRadioButton.addActionListener(this);
		medianRadioButton.addActionListener(this);
		noneRadioButton.addActionListener(this);
		noneRadioButton.setSelected(true);

		coffmanGrahamLayeringCheckBox.setSelected(false);
		coffmanGrahamLayeringCheckBox.addActionListener(this);
	}

	private LayeredPositionedGraph scaleGraph(Graph<Node> graph) throws IOException, SAXException, ParserConfigurationException {

		GraphScaler scaler = new GraphScalerImpl();

		scaler.setLayerOffset((Integer) layerOffsetSpin.getValue());
		scaler.setLeftOffset((Integer) leftOffsetSpin.getValue());
		scaler.setMinDistance((Integer) distanceSpin.getValue());
		scaler.setTopOffset((Integer) topOffsetSpin.getValue());

		int layerLength = layerLengthSlider.getValue();
		LayeredGraphOrder<Node> layeredGraphOrder = getLayeredGraphOrder(layerLength);
		LayeredPositionedGraph layeredPositionedGraph;
		try {
			layeredPositionedGraph = scaler.scale(graph, layeredGraphOrder);
		} catch (UnexpectedCycledGraphException e) {
			layeredPositionedGraph = handleAsynkGraphError(graph, scaler, layerLength);
		} catch (StackOverflowError e) {
			layeredPositionedGraph = handleAsynkGraphError(graph, scaler, layerLength);
		}

		LayeredPositionedGraph reducedGraph = reduceCrossings(layeredPositionedGraph);

		reducedGraph.setRadius((Integer) radiusSpin.getValue());
		return reducedGraph;
	}

	private LayeredPositionedGraph handleAsynkGraphError(Graph<Node> graph, GraphScaler scaler, int layerLength) {
		final LayeredPositionedGraph layeredPositionedGraph;
		JOptionPane
				.showMessageDialog(frame, "Cycled graph detected, Coffman-Graham algo is not applicable. " + "Reordering using simple layout", "Error", JOptionPane.ERROR_MESSAGE);

		simpleOrder.setLayerLength(layerLength);
		layeredPositionedGraph = scaler.scale(graph, simpleOrder);
		coffmanGrahamLayeringCheckBox.setSelected(false);
		useGrahamLayering = false;
		return layeredPositionedGraph;
	}

	private LayeredPositionedGraph reduceCrossings(LayeredPositionedGraph layeredPositionedGraph) {
		LayeredPositionedGraph result;
		switch (currentReductionMethod) {
			case (NO_REDUCTION_METHOD):
				result = layeredPositionedGraph;
				break;
			case (MEDIAN_METHOD):
				result = medianReducer.reduce(layeredPositionedGraph);
				break;
			case (BARYCENTER_METHOD):
				result = barycenterReducer.reduce(layeredPositionedGraph);
				break;
			default:
				throw new IllegalStateException("Wrong reduction code passed: " + currentReductionMethod);
		}

		return result;
	}

	private LayeredGraphOrder<Node> getLayeredGraphOrder(int layerLength) {
		LayeredGraphOrder<Node> result;
		if (useGrahamLayering) {
			result = coffmanGrahamOrder;
			result.setLayerLength(layerLength);
		} else {
			result = simpleOrder;
			result.setLayerLength(layerLength);
		}

		return result;
	}

	public void stateChanged(ChangeEvent e) {
		try {
			if (layerLengthSlider.getValue() == 0) {
				layerLengthSlider.setValue(1);
			}
			layeredPositionedGraph = scaleGraph(graph);
			canvasPanel.repaint();
		} catch (IOException e1) {
			e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (SAXException e1) {
			e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == directoryChooseButton) {
			final JFileChooser fc = new JFileChooser();
			fc.setAcceptAllFileFilterUsed(false);
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			fc.setCurrentDirectory(currentDirectory);
			fc.setDialogTitle(FILE_CHOOSER_TITLE);
			if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				File directory = fc.getSelectedFile();

				currentDirectory = directory;
				fillFileList(currentDirectory);

				chooseFileList.setSelectedIndex(0);
				frame.setTitle(currentDirectory.getAbsolutePath());
			}
		} else if (e.getSource() == noneRadioButton) {
			currentReductionMethod = NO_REDUCTION_METHOD;
			scaleGraphAndCatchExceptions();
			canvasPanel.repaint();
		} else if (e.getSource() == barycenterRadioButton) {
			currentReductionMethod = BARYCENTER_METHOD;
			scaleGraphAndCatchExceptions();
			canvasPanel.repaint();
		} else if (e.getSource() == medianRadioButton) {
			currentReductionMethod = MEDIAN_METHOD;
			scaleGraphAndCatchExceptions();
			canvasPanel.repaint();
		} else if (e.getSource() == coffmanGrahamLayeringCheckBox) {
			useGrahamLayering = coffmanGrahamLayeringCheckBox.isSelected();
			scaleGraphAndCatchExceptions();
			canvasPanel.repaint();
		}
	}

	private void scaleGraphAndCatchExceptions() {
		try {
			layeredPositionedGraph = scaleGraph(graph);
		} catch (IOException e1) {
			e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (SAXException e1) {
			e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == chooseFileList && !e.getValueIsAdjusting()) {
			//this is done to avoid firing the method when chooseFileList.setModel is called
			if (chooseFileList.getSelectedIndex() != -1) {
				try {
					parseFileFromList();
					canvasPanel.repaint();
				} catch (IOException e1) {
					e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
				} catch (SAXException e1) {
					e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
				} catch (ParserConfigurationException e1) {
					e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
				}
			}
		}
	}

	private void fillFileList(File currentDirectory) {
		final DefaultListModel listModel = new DefaultListModel();
		listModel.clear();
		for (File f : currentDirectory.listFiles(graphMLFilenameFilter)) {
			listModel.addElement(f.getName());
		}

		chooseFileList.setModel(listModel);
	}

	private void createUIComponents() {
		canvasPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				try {
					drawer.drawGraph(layeredPositionedGraph, g2);
				} catch (IllegalArgumentException e) {
					coffmanGrahamLayeringCheckBox.setSelected(false);
					useGrahamLayering = false;
					//								JOptionPane
					//					.showMessageDialog(frame, "Coffman-Graham algo is not applicable. "
					//							+ "Choose another graph or uncheck layering", "Error", JOptionPane.ERROR_MESSAGE);
					//					throw e;
					System.out.println("Error - it seems that you're trying to apply algorithm for cycled graph: " + chooseFileList
							.getSelectedValue());
				}

				setPreferredSize(new Dimension(layeredPositionedGraph.getWidth(), layeredPositionedGraph.getHeight()));
				revalidate();
			}
		};
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
		createUIComponents();
		rootPanel = new JPanel();
		rootPanel
				.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,center:196px:noGrow,left:4dlu:noGrow,center:100px:noGrow,left:4dlu:noGrow,center:100px:noGrow,left:4dlu:noGrow,fill:119px:noGrow,left:4dlu:noGrow,center:210px:grow,left:d:grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:100px:noGrow,top:4dlu:noGrow,center:max(d;43px):noGrow,top:8px:noGrow,center:d:grow"));
		mainPanel = new JPanel();
		mainPanel
				.setLayout(new FormLayout("center:190px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,center:d:grow", "center:d:grow,top:5dlu:noGrow,center:max(d;160px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
		CellConstraints cc = new CellConstraints();
		rootPanel.add(mainPanel, cc.xyw(3, 7, 10, CellConstraints.FILL, CellConstraints.FILL));
		chooseFileScrollPanel = new JScrollPane();
		mainPanel.add(chooseFileScrollPanel, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
		chooseFileList = new JList();
		chooseFileScrollPanel.setViewportView(chooseFileList);
		canvasScrollPane = new JScrollPane();
		mainPanel.add(canvasScrollPane, cc.xywh(4, 1, 1, 3, CellConstraints.FILL, CellConstraints.FILL));
		canvasPanel.setBackground(new Color(-52378));
		canvasScrollPane.setViewportView(canvasPanel);
		optionsPane = new JPanel();
		optionsPane
				.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:1dlu:noGrow,left:87px:noGrow,fill:6px:noGrow,center:d:noGrow,center:4px:noGrow,center:44px:noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:21px:noGrow,top:2dlu:noGrow,center:21px:noGrow,top:2dlu:noGrow,center:21px:noGrow,top:2dlu:noGrow,center:21px:noGrow,top:2dlu:noGrow,center:21px:noGrow"));
		optionsPane.setBackground(new Color(-10027060));
		mainPanel.add(optionsPane, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
		optionsPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null));
		final JLabel label1 = new JLabel();
		label1.setText("Distance");
		optionsPane.add(label1, cc.xy(3, 3));
		final JLabel label2 = new JLabel();
		label2.setText("Node radius");
		optionsPane.add(label2, cc.xy(3, 5));
		final JLabel label3 = new JLabel();
		label3.setText("Left offset");
		optionsPane.add(label3, cc.xy(3, 7));
		final JLabel label4 = new JLabel();
		label4.setText("Top offset");
		optionsPane.add(label4, cc.xy(3, 9));
		final JLabel label5 = new JLabel();
		label5.setText("Layer offset");
		optionsPane.add(label5, cc.xy(3, 11));
		distanceSpin = new JSpinner();
		optionsPane.add(distanceSpin, cc.xy(5, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
		radiusSpin = new JSpinner();
		optionsPane.add(radiusSpin, cc.xy(5, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
		leftOffsetSpin = new JSpinner();
		optionsPane.add(leftOffsetSpin, cc.xy(5, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
		topOffsetSpin = new JSpinner();
		optionsPane.add(topOffsetSpin, cc.xy(5, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
		layerOffsetSpin = new JSpinner();
		optionsPane.add(layerOffsetSpin, cc.xy(5, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
		tweakPanel = new JPanel();
		tweakPanel
				.setLayout(new FormLayout("fill:d:noGrow,fill:197px:noGrow,left:4dlu:noGrow,fill:185px:noGrow,left:4dlu:noGrow,fill:185px:noGrow,left:4dlu:noGrow,fill:80px:noGrow,left:4dlu:noGrow,fill:d:grow", "center:70px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
		tweakPanel.setBackground(new Color(-3342388));
		rootPanel.add(tweakPanel, cc.xyw(3, 3, 10, CellConstraints.FILL, CellConstraints.FILL));
		tweakPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new FormLayout("fill:143px:noGrow,left:4dlu:noGrow,center:d:grow", "center:67px:noGrow"));
		panel1.setBackground(new Color(-3355393));
		panel1.setOpaque(false);
		tweakPanel.add(panel1, cc.xyw(2, 1, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
		directoryChooseButton = new JButton();
		directoryChooseButton.setText("Choose Folder:");
		panel1.add(directoryChooseButton, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new FormLayout("fill:d:grow", "center:46px:noGrow"));
		panel2.setOpaque(false);
		panel1.add(panel2, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Layer length"));
		layerLengthSlider = new JSlider();
		layerLengthSlider.setMajorTickSpacing(1);
		layerLengthSlider.setMaximum(50);
		layerLengthSlider.setMinimum(1);
		layerLengthSlider.setOpaque(false);
		layerLengthSlider.setPaintLabels(false);
		layerLengthSlider.setPaintTicks(false);
		layerLengthSlider.setPaintTrack(false);
		layerLengthSlider.setValue(10);
		layerLengthSlider.setValueIsAdjusting(true);
		panel2.add(layerLengthSlider, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		coffmanGrahamLayeringCheckBox = new JCheckBox();
		coffmanGrahamLayeringCheckBox.setText("Coffman-Graham layering");
		tweakPanel.add(coffmanGrahamLayeringCheckBox, cc.xy(2, 3));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:26px:noGrow"));
		rootPanel.add(panel3, cc.xyw(3, 5, 5));
		panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Crossing redunction method"));
		medianRadioButton = new JRadioButton();
		medianRadioButton.setText("Median");
		panel3.add(medianRadioButton, cc.xy(3, 1));
		barycenterRadioButton = new JRadioButton();
		barycenterRadioButton.setText("Barycenter");
		panel3.add(barycenterRadioButton, cc.xy(5, 1));
		noneRadioButton = new JRadioButton();
		noneRadioButton.setText("none");
		panel3.add(noneRadioButton, cc.xy(1, 1));
		ButtonGroup buttonGroup;
		buttonGroup = new ButtonGroup();
		buttonGroup.add(noneRadioButton);
		buttonGroup.add(medianRadioButton);
		buttonGroup.add(barycenterRadioButton);
	}

	/** @noinspection ALL */
	public JComponent $$$getRootComponent$$$() {
		return rootPanel;
	}
}
