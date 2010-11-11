package com.drawgraph.graphics;

import com.drawgraph.algorithms.BarycenterReducer;
import com.drawgraph.algorithms.CoffmanGrahamLayeredGraphOrder;
import com.drawgraph.algorithms.CoordinateAssignmentReducer;
import com.drawgraph.algorithms.PositionedGraphTransformer;
import com.drawgraph.algorithms.DummyNodesAssigner;
import com.drawgraph.algorithms.LayeredGraphOrder;
import com.drawgraph.algorithms.MedianReducer;
import com.drawgraph.algorithms.NoDummyNodesAssigner;
import com.drawgraph.algorithms.SimpleDummyNodesAssigner;
import com.drawgraph.algorithms.SimpleLayeredGraphOrder;
import com.drawgraph.algorithms.UnexpectedCycledGraphException;
import com.drawgraph.graphics.prefuse.InfrastructureView;
import com.drawgraph.graphics.prefuse.PreFuseCanvas;
import com.drawgraph.graphics.prefuse.RadialGraphView;
import com.drawgraph.graphics.prefuse.TreeView;
import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredGraph;
import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.Node;
import com.drawgraph.model.SimpleNode;
import com.drawgraph.parser.GraphMLParser;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.xml.sax.SAXException;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;

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
	private JRadioButton dummyEnabledRadioButton;
	private JRadioButton dummyDisabledRadioButton;
	private JSpinner shiftSpin;
	private JRadioButton coordinateAssignementRadioButton;
	private JPanel prefuseCanvasPanel;
	private JTabbedPane mainTabbedPanel;
	private JPanel mainRoot;
	private JRadioButton treeLayoutPrefuseRadiobutton;
	private JRadioButton radialLayoutPrefuseRadiobutton;
	private JRadioButton infrastructureLayoutPrefuseRadiobutton;
	private JCheckBox coordinateAssignmentCheckBox;

	private boolean dummiesEnabled = false;

	private final String DIGRAPHS = "data/digraphs";
	private Graph<SimpleNode> graph;
	private LayeredPositionedGraph layeredPositionedGraph;
	private SimpleGraphDrawer drawer;

	private File currentDirectory;
	private String currentFilePath;
	private GraphMLParser parser = new GraphMLParser();

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
	private static final int INITIAL_RADIUS = 35;
	private static final int MINIMUM_RADIUS = 1;
	private static final int RADIUS_STEP_SIZE = 1;
	private static final int MAXIMUM_TOP_OFFSET = 200;
	private static final int MAXIMUM_LEFT_OFFSET = 400;
	private static final int OFFSET_STEP_SIZE = 1;
	private static final int INITIAL_LAYER_OFFSET = 50;
	private static final int LAYER_OFFSET_STEP_SIZE = 1;
	private static final int INITIAL_LAYERS_COUNT = 3;
	private static final int MAXIMUM_LAYERS_COUNT = 50;
	private static final int INITIAL_SHIFT = 10;
	private static final int MINIMUM_SHIFT = -200;
	private static final int MAXIMUM_SHIFT = 200;
	private static final int MAJOR_TICK_SPACING = 10;
	private static final int MINOR_TICK_SPACING = 1;

	private static final int FRAME_WIDTH = 960;
	private static final int FRAME_HEIGHT = 700;


	private boolean useGrahamLayering = false;

	private static final int NO_REDUCTION_METHOD = 0;
	private static final int MEDIAN_METHOD = 1;
	private static final int BARYCENTER_METHOD = 2;
	private static final int COORDINATE_ASSIGNMENT_METHOD = 3;
	private int currentReductionMethod = NO_REDUCTION_METHOD;

	private DummyNodesAssigner noDummyAssigner = new NoDummyNodesAssigner();
	private DummyNodesAssigner simpleDummyAssigner = new SimpleDummyNodesAssigner();
	private DummyNodesAssigner currentAssigner = noDummyAssigner;

	private static JFrame frame;

	public static final String GRAPHML_EXT = ".graphml";

	private FilenameFilter graphMLFilenameFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(GRAPHML_EXT);
		}
	};
	private static final String FILE_CHOOSER_TITLE = "Choose a directory to fetch resources from";
	private PositionedGraphTransformer medianReducer = new MedianReducer();
	private PositionedGraphTransformer barycenterReducer = new BarycenterReducer();
	private PositionedGraphTransformer coordinateAssigner = new CoordinateAssignmentReducer();

	private LayeredGraphOrder simpleOrder = new SimpleLayeredGraphOrder(0);
	private LayeredGraphOrder coffmanGrahamOrder = new CoffmanGrahamLayeredGraphOrder(0);
	private boolean useCoordinateAssignment = false;

	private static final int ALGO_TAB_INDEX = 0;
	private static final int PREFUSE_TAB_INDEX = 1;

	private int currentTab = ALGO_TAB_INDEX;

	private prefuse.data.Graph currentPrefuseGraph;
	private GraphMLReader graphReaderPrefuse = new GraphMLReader();
	private int currentPreFuseLayout = TREE_LAYOUT_PREFUSE;
	private static final int TREE_LAYOUT_PREFUSE = 0;
	private static final int RADIAL_LAYOUT_PREFUSE = 1;
	private static final int INFRASTRUCTURE_LAYOUT_PREFUSE = 2;

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
		frame.setContentPane(ui.$$$getRootComponent$$$());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ui.initSpinners();
		ui.initCanvas();

		frame.pack();
		//		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
		String path = getSelectedFilePath();

		currentFilePath = path;
		Graph<SimpleNode> g = parser.buildGraph(currentFilePath);
		graph = g;
		layeredPositionedGraph = transformGraph(g);
	}

	private String getSelectedFilePath() {
		int selectedIndex = chooseFileList.getSelectedIndex();
		if (selectedIndex == -1) {
			selectedIndex = 0;
		}
		DefaultListModel listModel = (DefaultListModel) chooseFileList.getModel();
		String selectedFile = (String) listModel.get(selectedIndex);
		String path = currentDirectory + File.separator + selectedFile;
		return path;
	}

	private void initSpinners() {
		SpinnerModel distanceModel = new SpinnerNumberModel(INITIAL_DISTANCE, MINIMUM_DISTANCE, MAXIMUM_DISTANCE, DISTANCE_STEP_SIZE);
		distanceSpin.setModel(distanceModel);

		SpinnerModel radiusModel = new SpinnerNumberModel(INITIAL_RADIUS, MINIMUM_RADIUS, MAXIMUM_RADIUS, RADIUS_STEP_SIZE);
		radiusSpin.setModel(radiusModel);

		SpinnerModel leftOffsetModel = new SpinnerNumberModel(INITIAL_LEFT_OFFSET, MINIMUM_OFFSET, MAXIMUM_LEFT_OFFSET, OFFSET_STEP_SIZE);
		SpinnerModel topOffsetModel = new SpinnerNumberModel(INITIAL_TOP_OFFSET, MINIMUM_OFFSET, MAXIMUM_TOP_OFFSET, OFFSET_STEP_SIZE);
		leftOffsetSpin.setModel(leftOffsetModel);
		topOffsetSpin.setModel(topOffsetModel);

		SpinnerModel shiftModel = new SpinnerNumberModel(INITIAL_SHIFT, MINIMUM_SHIFT, MAXIMUM_SHIFT, OFFSET_STEP_SIZE);
		shiftSpin.setModel(shiftModel);

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
				hashtable.put(i, new JLabel(Integer.toString(i)));
			}
		}
		layerLengthSlider.setLabelTable(hashtable);
		layerLengthSlider.setPaintLabels(true);
		layerLengthSlider.setPaintTicks(true);
		layerLengthSlider.setPaintTrack(true);
		layerLengthSlider.setSnapToTicks(true);

		distanceSpin.addChangeListener(this);
		radiusSpin.addChangeListener(this);
		leftOffsetSpin.addChangeListener(this);
		topOffsetSpin.addChangeListener(this);
		layerOffsetSpin.addChangeListener(this);
		shiftSpin.addChangeListener(this);

		layerLengthSlider.addChangeListener(this);

		directoryChooseButton.addActionListener(this);

		chooseFileList.addListSelectionListener(this);

		barycenterRadioButton.addActionListener(this);
		medianRadioButton.addActionListener(this);
//		coordinateAssignementRadioButton.addActionListener(this);
		noneRadioButton.addActionListener(this);
		noneRadioButton.setSelected(true);

		coffmanGrahamLayeringCheckBox.setSelected(false);
		coffmanGrahamLayeringCheckBox.addActionListener(this);

		dummyDisabledRadioButton.addActionListener(this);
		dummyDisabledRadioButton.setSelected(true);
		dummyEnabledRadioButton.addActionListener(this);

		mainTabbedPanel.addChangeListener(this);

		treeLayoutPrefuseRadiobutton.addActionListener(this);
		treeLayoutPrefuseRadiobutton.setSelected(true);
		radialLayoutPrefuseRadiobutton.addActionListener(this);
		infrastructureLayoutPrefuseRadiobutton.addActionListener(this);
		coordinateAssignmentCheckBox.addActionListener(this);
		coordinateAssignmentCheckBox.setEnabled(false);

	}

	private LayeredPositionedGraph scaleGraph(LayeredGraph<? extends Node> source) {
		GraphScaler scaler = new GraphScalerImpl();

		scaler.setLayerOffset((Integer) layerOffsetSpin.getValue());
		scaler.setLeftOffset((Integer) leftOffsetSpin.getValue());
		scaler.setMinDistance((Integer) distanceSpin.getValue());
		scaler.setTopOffset((Integer) topOffsetSpin.getValue());
		scaler.setShift((Integer) shiftSpin.getValue());
		LayeredPositionedGraph result = scaler.scale(source);
		result.setRadius((Integer) radiusSpin.getValue());

		return result;
	}

	private <T extends Node<T>> LayeredPositionedGraph transformGraph(Graph<T> graph) throws IOException, SAXException, ParserConfigurationException {

		graph = graph.copy();
		int layerLength = layerLengthSlider.getValue();
		LayeredGraphOrder layeredGraphOrder = getLayeredGraphOrder(layerLength);
		LayeredGraph<T> layeredGraph;
		try {
			layeredGraph = layeredGraphOrder.getLayeredGraph(graph);
		} catch (UnexpectedCycledGraphException e) {
			layeredGraph = handleAsynkGraphError(graph, layerLength);
		} catch (StackOverflowError e) {
			layeredGraph = handleAsynkGraphError(graph, layerLength);
		}

		LayeredGraph<T> graphWithDummies = currentAssigner.assignDummyNodes(layeredGraph);
		LayeredPositionedGraph scaledGraph = scaleGraph(graphWithDummies);
		LayeredPositionedGraph reducedGraph = reduceCrossings(scaledGraph);

		if (useCoordinateAssignment) {
			reducedGraph = coordinateAssigner.transform(reducedGraph);
		}
		return reducedGraph;
	}

	private <T extends Node<T>> LayeredGraph<T> handleAsynkGraphError(Graph<T> graph, int layerLength) {
		JOptionPane
				.showMessageDialog(frame, "Cycled graph detected, Coffman-Graham algo is not applicable. " + "Reordering using simple layout", "Error", JOptionPane.ERROR_MESSAGE);

		simpleOrder.setLayerLength(layerLength);
		LayeredGraph<T> layeredGraph = simpleOrder.getLayeredGraph(graph);
		coffmanGrahamLayeringCheckBox.setSelected(false);
		useGrahamLayering = false;
		return layeredGraph;
	}

	private LayeredPositionedGraph reduceCrossings(LayeredPositionedGraph graph) {
		LayeredPositionedGraph result;
		switch (currentReductionMethod) {
			case (NO_REDUCTION_METHOD):
				result = graph;
				break;
			case (MEDIAN_METHOD):
				result = medianReducer.transform(graph);
				break;
			case (BARYCENTER_METHOD):
				result = barycenterReducer.transform(graph);
				break;
//			case (COORDINATE_ASSIGNMENT_METHOD):
//				result = coordinateAssigner.transform(graph);
//				break;
			default:
				throw new IllegalStateException("Wrong reduction code passed: " + currentReductionMethod);
		}

		return result;
	}

	private LayeredGraphOrder getLayeredGraphOrder(int layerLength) {
		LayeredGraphOrder result;
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
		if (e.getSource() == layerLengthSlider) {
			if (layerLengthSlider.getValue() == 0) {
				layerLengthSlider.setValue(1);
			}
			try {
				layeredPositionedGraph = transformGraph(graph);
				canvasPanel.repaint();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SAXException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == mainTabbedPanel) {
			int index = mainTabbedPanel.getSelectedIndex();
			if (index == 0) {
				// algo tab
				currentTab = ALGO_TAB_INDEX;
				processFileFromList();
			} else if (index == 1) {
				renderPrefuseCanvas();
				currentTab = PREFUSE_TAB_INDEX;
			}
		} else {
			layeredPositionedGraph = scaleGraph(layeredPositionedGraph);
			canvasPanel.repaint();
		}
	}

	private void renderPrefuseCanvas() {
		try {
			currentPrefuseGraph = graphReaderPrefuse.readGraph(getSelectedFilePath());
		} catch (DataIOException e1) {
			e1.printStackTrace();
		}
		renderPrefuseContent();
	}

	private void renderPrefuseContent() {
		PreFuseCanvas preFuseCanvas = getPreFuseCanvas();
		prefuseCanvasPanel.removeAll();
		CellConstraints cc = new CellConstraints();
		JComponent canvas = preFuseCanvas.getCanvas();

		prefuseCanvasPanel.add(canvas, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
		prefuseCanvasPanel.revalidate();
		prefuseCanvasPanel.repaint();
	}

	private PreFuseCanvas getPreFuseCanvas() {
		PreFuseCanvas canvas;
		switch (currentPreFuseLayout) {
			case TREE_LAYOUT_PREFUSE:
				canvas = new TreeView(currentPrefuseGraph, "id");
				break;
			case RADIAL_LAYOUT_PREFUSE:
				canvas = new RadialGraphView(currentPrefuseGraph, "id");
				break;
			case INFRASTRUCTURE_LAYOUT_PREFUSE:
				canvas = new InfrastructureView(currentPrefuseGraph, "id");
				break;

			default:
				throw new IllegalStateException("Unknown prefuse layout: " + currentPreFuseLayout);
		}

		return canvas;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == directoryChooseButton) {
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
		} else if (source == noneRadioButton) {
			coordinateAssignmentCheckBox.setEnabled(false);
			currentReductionMethod = NO_REDUCTION_METHOD;
			scaleGraphAndCatchExceptions(graph);
			canvasPanel.repaint();
		} else if (source == barycenterRadioButton) {
			coordinateAssignmentCheckBox.setEnabled(true);
			currentReductionMethod = BARYCENTER_METHOD;
			scaleGraphAndCatchExceptions(graph);
			canvasPanel.repaint();
		} else if (source == medianRadioButton) {
			coordinateAssignmentCheckBox.setEnabled(true);
			currentReductionMethod = MEDIAN_METHOD;
			scaleGraphAndCatchExceptions(graph);
			canvasPanel.repaint();
		} else if (source == coordinateAssignementRadioButton) {
			currentReductionMethod = COORDINATE_ASSIGNMENT_METHOD;
			scaleGraphAndCatchExceptions(graph);
			canvasPanel.repaint();
		} else if (e.getSource() == coordinateAssignmentCheckBox) {
			useCoordinateAssignment = coordinateAssignmentCheckBox.isSelected();
//			currentReductionMethod = COORDINATE_ASSIGNMENT_METHOD;
			scaleGraphAndCatchExceptions(graph);
			canvasPanel.repaint();
		} else if (source == coffmanGrahamLayeringCheckBox) {
			useGrahamLayering = coffmanGrahamLayeringCheckBox.isSelected();
			scaleGraphAndCatchExceptions(graph);
			canvasPanel.repaint();
		} else if (source == dummyDisabledRadioButton) {
			if (!dummiesEnabled) {
				return;
			}
			dummiesEnabled = false;
			currentAssigner = noDummyAssigner;
			try {
				layeredPositionedGraph = transformGraph(graph);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SAXException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			}
			scaleGraphAndCatchExceptions(layeredPositionedGraph);
			canvasPanel.repaint();
		} else if (source == dummyEnabledRadioButton) {
			if (dummiesEnabled) {
				return;
			}
			dummiesEnabled = true;
			currentAssigner = simpleDummyAssigner;
			scaleGraphAndCatchExceptions(layeredPositionedGraph);
			canvasPanel.repaint();
		} else if (source == treeLayoutPrefuseRadiobutton) {
			currentPreFuseLayout = TREE_LAYOUT_PREFUSE;
			renderPrefuseContent();
		} else if (source == radialLayoutPrefuseRadiobutton) {
			currentPreFuseLayout = RADIAL_LAYOUT_PREFUSE;
			renderPrefuseContent();
		} else if (source == infrastructureLayoutPrefuseRadiobutton) {
			currentPreFuseLayout = INFRASTRUCTURE_LAYOUT_PREFUSE;
			renderPrefuseContent();
		}
	}

	private void scaleGraphAndCatchExceptions(Graph<? extends Node> graph) {
		try {
			layeredPositionedGraph = transformGraph(graph);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource() == chooseFileList && !e.getValueIsAdjusting()) {
			if (currentTab == ALGO_TAB_INDEX) {
				processFileFromList();
			} else if (currentTab == PREFUSE_TAB_INDEX) {
				renderPrefuseCanvas();
			} else {
				throw new IllegalStateException("Wrong tab index: " + currentTab);
			}
		}
	}

	private void processFileFromList() {
		//this is done to avoid firing the method when chooseFileList.setModel is called
		if (chooseFileList.getSelectedIndex() != -1) {
			try {
				parseFileFromList();
				canvasPanel.repaint();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (SAXException e1) {
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void fillFileList(File currentDirectory) {
		final DefaultListModel listModel = new DefaultListModel();
		listModel.clear();
		File[] files = currentDirectory.listFiles(graphMLFilenameFilter);
		Arrays.sort(files);
		for (File f : files) {
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
					System.out
							.println("Error - it seems that you're trying to apply algorithm for cycled graph: " + chooseFileList
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
		mainRoot = new JPanel();
		mainRoot.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:128px:noGrow,left:4dlu:noGrow,fill:d:grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow"));
		mainRoot.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), null));
		mainTabbedPanel = new JTabbedPane();
		mainTabbedPanel.setEnabled(true);
		mainTabbedPanel.setTabPlacement(1);
		CellConstraints cc = new CellConstraints();
		mainRoot.add(mainTabbedPanel, cc.xy(5, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
		mainTabbedPanel.addTab("Algorithms", panel1);
		rootPanel = new JPanel();
		rootPanel
				.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,center:196px:noGrow,left:4dlu:noGrow,center:74px:noGrow,left:96dlu:noGrow,left:4dlu:noGrow,fill:16px:noGrow,left:4dlu:noGrow,fill:191px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,center:d:grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:86px:noGrow,top:4dlu:noGrow,center:d:grow"));
		panel1.add(rootPanel, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
		mainPanel = new JPanel();
		mainPanel
				.setLayout(new FormLayout("center:268px:noGrow,left:5dlu:noGrow,fill:max(d;4px):noGrow,center:d:grow", "center:48px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,center:max(d;4px):noGrow,center:max(d;160px):grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
		rootPanel.add(mainPanel, cc.xyw(3, 5, 18, CellConstraints.FILL, CellConstraints.FILL));
		canvasScrollPane = new JScrollPane();
		mainPanel.add(canvasScrollPane, cc.xywh(4, 1, 1, 7, CellConstraints.FILL, CellConstraints.FILL));
		canvasPanel.setBackground(new Color(-2627329));
		canvasScrollPane.setViewportView(canvasPanel);
		optionsPane = new JPanel();
		optionsPane
				.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:1dlu:noGrow,left:114px:noGrow,fill:73px:noGrow,center:50px:noGrow,center:23px:noGrow,center:44px:noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:21px:noGrow,top:2dlu:noGrow,center:21px:noGrow,top:2dlu:noGrow,center:21px:noGrow,top:2dlu:noGrow,center:21px:noGrow,top:2dlu:noGrow,center:21px:noGrow,top:4dlu:noGrow,center:21px:noGrow"));
		optionsPane.setBackground(new Color(-10027060));
		mainPanel.add(optionsPane, cc.xy(1, 7, CellConstraints.FILL, CellConstraints.TOP));
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
		final JLabel label6 = new JLabel();
		label6.setText("Layer shift");
		optionsPane.add(label6, cc.xy(3, 13));
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
		shiftSpin = new JSpinner();
		optionsPane.add(shiftSpin, cc.xy(5, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new FormLayout("fill:95px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:26px:noGrow"));
		mainPanel.add(panel2, cc.xy(1, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
		panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Dummy nodes"));
		dummyEnabledRadioButton = new JRadioButton();
		dummyEnabledRadioButton.setText("enabled");
		panel2.add(dummyEnabledRadioButton, cc.xy(3, 1));
		dummyDisabledRadioButton = new JRadioButton();
		dummyDisabledRadioButton.setText("disabled");
		panel2.add(dummyDisabledRadioButton, cc.xy(1, 1));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:26px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
		mainPanel.add(panel3, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
		panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Crossing reduction method"));
		barycenterRadioButton = new JRadioButton();
		barycenterRadioButton.setText("Barycenter");
		panel3.add(barycenterRadioButton, cc.xy(3, 1));
		coordinateAssignementRadioButton = new JRadioButton();
		coordinateAssignementRadioButton.setText("Coordinate Assignement");
		panel3.add(coordinateAssignementRadioButton, cc.xy(3, 3));
		noneRadioButton = new JRadioButton();
		noneRadioButton.setText("none");
		panel3.add(noneRadioButton, cc.xy(1, 3, CellConstraints.LEFT, CellConstraints.TOP));
		medianRadioButton = new JRadioButton();
		medianRadioButton.setText("Median");
		panel3.add(medianRadioButton, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));
		final JPanel panel4 = new JPanel();
		panel4.setLayout(new FormLayout("center:258px:noGrow", "center:d:grow"));
		mainPanel.add(panel4, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.CENTER));
		panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Arrange graph"));
		coffmanGrahamLayeringCheckBox = new JCheckBox();
		coffmanGrahamLayeringCheckBox.setText("Coffman-Graham layering");
		panel4.add(coffmanGrahamLayeringCheckBox, cc.xy(1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		tweakPanel = new JPanel();
		tweakPanel
				.setLayout(new FormLayout("fill:d:noGrow,center:279px:noGrow,center:13dlu:noGrow,fill:185px:noGrow,left:4dlu:noGrow,fill:185px:noGrow,left:4dlu:noGrow,fill:80px:noGrow,left:4dlu:noGrow,fill:d:grow", "center:82px:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
		tweakPanel.setBackground(new Color(-3342388));
		rootPanel.add(tweakPanel, cc.xyw(3, 3, 18, CellConstraints.FILL, CellConstraints.FILL));
		tweakPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
		final JPanel panel5 = new JPanel();
		panel5.setLayout(new FormLayout("center:d:grow", "center:84px:noGrow"));
		panel5.setBackground(new Color(-3355393));
		panel5.setOpaque(false);
		tweakPanel.add(panel5, cc.xyw(2, 1, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
		final JPanel panel6 = new JPanel();
		panel6.setLayout(new FormLayout("center:d:grow", "center:46px:noGrow"));
		panel6.setOpaque(false);
		panel5.add(panel6, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Layer length"));
		layerLengthSlider = new JSlider();
		layerLengthSlider.setMajorTickSpacing(1);
		layerLengthSlider.setMaximum(50);
		layerLengthSlider.setMinimum(1);
		layerLengthSlider.setOpaque(false);
		layerLengthSlider.setPaintLabels(false);
		layerLengthSlider.setPaintTicks(false);
		layerLengthSlider.setPaintTrack(false);
		layerLengthSlider.setSnapToTicks(false);
		layerLengthSlider.setValue(10);
		layerLengthSlider.setValueIsAdjusting(true);
		panel6.add(layerLengthSlider, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		final JPanel panel7 = new JPanel();
		panel7.setLayout(new FormLayout("left:15px:noGrow,left:4dlu:noGrow,center:d:grow", "center:59px:noGrow,top:4dlu:noGrow,center:d:grow"));
		mainTabbedPanel.addTab("PrefUse", panel7);
		final JScrollPane scrollPane1 = new JScrollPane();
		panel7.add(scrollPane1, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.FILL));
		prefuseCanvasPanel = new JPanel();
		prefuseCanvasPanel.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
		scrollPane1.setViewportView(prefuseCanvasPanel);
		final JPanel panel8 = new JPanel();
		panel8.setLayout(new FormLayout("fill:d:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow"));
		panel7.add(panel8, cc.xy(3, 1, CellConstraints.LEFT, CellConstraints.FILL));
		treeLayoutPrefuseRadiobutton = new JRadioButton();
		treeLayoutPrefuseRadiobutton.setText("Tree");
		panel8.add(treeLayoutPrefuseRadiobutton, cc.xy(1, 1));
		radialLayoutPrefuseRadiobutton = new JRadioButton();
		radialLayoutPrefuseRadiobutton.setText("Radial");
		panel8.add(radialLayoutPrefuseRadiobutton, cc.xy(3, 1));
		infrastructureLayoutPrefuseRadiobutton = new JRadioButton();
		infrastructureLayoutPrefuseRadiobutton.setText("Infrastructure");
		panel8.add(infrastructureLayoutPrefuseRadiobutton, cc.xy(5, 1));
		final JPanel panel9 = new JPanel();
		panel9.setLayout(new FormLayout("fill:d:grow", "center:62px:noGrow,top:4dlu:noGrow,center:535px:grow"));
		mainRoot.add(panel9, cc.xy(3, 3, CellConstraints.DEFAULT, CellConstraints.FILL));
		directoryChooseButton = new JButton();
		directoryChooseButton.setText("Choose Folder:");
		panel9.add(directoryChooseButton, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
		chooseFileScrollPanel = new JScrollPane();
		panel9.add(chooseFileScrollPanel, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
		chooseFileList = new JList();
		chooseFileScrollPanel.setViewportView(chooseFileList);
		ButtonGroup buttonGroup;
		buttonGroup = new ButtonGroup();
		buttonGroup.add(noneRadioButton);
		buttonGroup.add(medianRadioButton);
		buttonGroup.add(barycenterRadioButton);
		buttonGroup.add(coordinateAssignementRadioButton);
		buttonGroup = new ButtonGroup();
		buttonGroup.add(dummyEnabledRadioButton);
		buttonGroup.add(dummyDisabledRadioButton);
		buttonGroup = new ButtonGroup();
		buttonGroup.add(radialLayoutPrefuseRadiobutton);
		buttonGroup.add(radialLayoutPrefuseRadiobutton);
		buttonGroup.add(infrastructureLayoutPrefuseRadiobutton);
		buttonGroup.add(treeLayoutPrefuseRadiobutton);
	}

	/** @noinspection ALL */
	public JComponent $$$getRootComponent$$$() {
		return mainRoot;
	}
}
