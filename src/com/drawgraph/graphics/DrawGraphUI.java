package com.drawgraph.graphics;

import com.drawgraph.model.Graph;
import com.drawgraph.model.LayeredPositionedGraph;
import com.drawgraph.model.Node;
import com.drawgraph.parser.GraphMLParser;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.xml.sax.SAXException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Date: Oct 24, 2010
 * Time: 11:31:07 AM
 *
 * @author denisk
 */
public class DrawGraphUI implements ChangeListener {

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

	private final String DIGRAPHS = "data/digraphs";
	private Graph<Node> graph;
	private LayeredPositionedGraph layeredPositionedGraph;
	private SimpleGraphDrawer drawer;

	private File currentDirectory;
	private String currentFilePath;
	private GraphMLParser parser;

	private static final int MAXIMAL_RADIUS = 100;
	private static final int INITIAL_DISTANCE = 100;
	private static final int MINIMAL_OFFSET = 10;
	private static final int MINIMAL_DISTANCE = 10;
	private static final int MAXIMAL_LAYER_OFFSET = 500;
	private static final int MAXIMAL_DISTANCE = 500;
	private static final int MINIMUM_LAYERS_COUNT = 1;
	private static final int DISTANCE_STEP_SIZE = 1;
	private static final int MINIMAL_LAYER_OFFSET = 20;
	private static final int INITIAL_RADIUS = 20;
	private static final int MINIMAL_RADIUS = 1;
	private static final int RADIUS_STEP_SIZE = 1;
	private static final int INITIAL_OFFSET = 30;
	private static final int MAXIMAL_OFFSET = 200;
	private static final int OFFSET_STEP_SIZE = 1;
	private static final int INITIAL_LAYER_OFFSET = 50;
	private static final int LAYER_OFFSET_STEP_SIZE = 1;
	private static final int INITIAL_LAYERS_COUNT = 3;
	private static final int MAXIMUM_LAYERS_COUNT = 50;
	private static final int MAJOR_TICK_SPACING = 1;

	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
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

		ui.initSpinners();
		ui.initComponents();

		//	ui.simpleDraw();
		frame.setLocation((int) screenWidth / 2 - frame.getWidth() / 2, (int) screenHeight / 2 - frame.getHeight() / 2);
		frame.setVisible(true);
	}

	private void initComponents() throws IOException, SAXException, ParserConfigurationException {
		drawer = new SimpleGraphDrawer();
		//todo
		currentDirectory = new File(DIGRAPHS);
		if (!currentDirectory.exists()) {
			throw new IllegalStateException("No directory found: " + DIGRAPHS);
		}

		DefaultListModel listModel = new DefaultListModel();
		//todo
		listModel.addElement("g.100.0.graphml");
		listModel.addElement("g.100.1.graphml");

		chooseFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		chooseFileList.setModel(listModel);
		chooseFileList.setSelectedIndex(0);

		String selectedFile = (String) listModel.get(chooseFileList.getSelectedIndex());
		String path = currentDirectory + File.separator + selectedFile;

		currentFilePath = path;
		parser = new GraphMLParser();
		Graph<Node> g = parser.buildGraph(currentFilePath);
		graph = g;
		layeredPositionedGraph = scaleGraph(g);
	}

	private void initSpinners() {
		SpinnerModel distanceModel = new SpinnerNumberModel(INITIAL_DISTANCE, MINIMAL_DISTANCE, MAXIMAL_DISTANCE, DISTANCE_STEP_SIZE);
		distanceSpin.setModel(distanceModel);

		SpinnerModel radiusModel = new SpinnerNumberModel(INITIAL_RADIUS, MINIMAL_RADIUS, MAXIMAL_RADIUS, RADIUS_STEP_SIZE);
		radiusSpin.setModel(radiusModel);

		SpinnerModel leftOffsetModel = new SpinnerNumberModel(INITIAL_OFFSET, MINIMAL_OFFSET, MAXIMAL_OFFSET, OFFSET_STEP_SIZE);
		SpinnerModel topOffsetModel = new SpinnerNumberModel(INITIAL_OFFSET, MINIMAL_OFFSET, MAXIMAL_OFFSET, OFFSET_STEP_SIZE);
		leftOffsetSpin.setModel(leftOffsetModel);
		topOffsetSpin.setModel(topOffsetModel);

		SpinnerModel layerOffsetModel = new SpinnerNumberModel(INITIAL_LAYER_OFFSET, MINIMAL_LAYER_OFFSET, MAXIMAL_LAYER_OFFSET, LAYER_OFFSET_STEP_SIZE);
		layerOffsetSpin.setModel(layerOffsetModel);

		layerLengthSlider.setValue(INITIAL_LAYERS_COUNT);
		layerLengthSlider.setMaximum(MAXIMUM_LAYERS_COUNT);
		layerLengthSlider.setMinimum(MINIMUM_LAYERS_COUNT);
		layerLengthSlider.setMajorTickSpacing(MAJOR_TICK_SPACING);

		distanceSpin.addChangeListener(this);
		radiusSpin.addChangeListener(this);
		leftOffsetSpin.addChangeListener(this);
		topOffsetSpin.addChangeListener(this);
		layerOffsetSpin.addChangeListener(this);
		layerLengthSlider.addChangeListener(this);
	}

	private LayeredPositionedGraph scaleGraph(Graph<Node> graph) throws IOException, SAXException, ParserConfigurationException {

		GraphScaler scaler = new GraphScalerImpl();

		scaler.setLayerOffset((Integer) layerOffsetSpin.getValue());
		scaler.setLeftOffset((Integer) leftOffsetSpin.getValue());
		scaler.setMinDistance((Integer) distanceSpin.getValue());
		scaler.setTopOffset((Integer) topOffsetSpin.getValue());

		LayeredPositionedGraph layeredPositionedGraph = scaler.scale(graph, new SimpleLayeredGraphOrder(layerLengthSlider.getValue()));
		layeredPositionedGraph.setRadius((Integer) radiusSpin.getValue());

		return layeredPositionedGraph;
	}

	public void stateChanged(ChangeEvent e) {
		try {
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

	private void createUIComponents() {
		canvasPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				drawer.drawGraph(layeredPositionedGraph, g2);

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
				.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,center:d:grow,left:4dlu:noGrow,center:210px:grow,left:d:grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:45px:noGrow,top:8px:noGrow,center:d:grow"));
		mainPanel = new JPanel();
		mainPanel
				.setLayout(new FormLayout("center:150px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,center:d:grow", "center:d:grow,top:5dlu:noGrow,center:max(d;160px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
		CellConstraints cc = new CellConstraints();
		rootPanel.add(mainPanel, cc.xyw(3, 5, 4, CellConstraints.FILL, CellConstraints.FILL));
		chooseFileScrollPanel = new JScrollPane();
		mainPanel.add(chooseFileScrollPanel, cc.xy(MINIMUM_LAYERS_COUNT, MINIMUM_LAYERS_COUNT, CellConstraints.FILL, CellConstraints.FILL));
		chooseFileList = new JList();
		chooseFileScrollPanel.setViewportView(chooseFileList);
		canvasScrollPane = new JScrollPane();
		mainPanel.add(canvasScrollPane, cc.xywh(4, MINIMUM_LAYERS_COUNT, MINIMUM_LAYERS_COUNT, 3, CellConstraints.FILL, CellConstraints.FILL));
		canvasPanel.setBackground(new Color(-52378));
		canvasScrollPane.setViewportView(canvasPanel);
		optionsPane = new JPanel();
		optionsPane
				.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:1dlu:noGrow,fill:81px:noGrow,fill:6px:noGrow,fill:d:grow,left:2dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:21px:noGrow,top:2dlu:noGrow,center:21px:noGrow,top:2dlu:noGrow,center:21px:noGrow,top:2dlu:noGrow,center:21px:noGrow,top:2dlu:noGrow,center:21px:noGrow"));
		optionsPane.setBackground(new Color(-10027060));
		mainPanel.add(optionsPane, cc.xy(MINIMUM_LAYERS_COUNT, 3, CellConstraints.FILL, CellConstraints.FILL));
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
				.setLayout(new FormLayout("fill:d:noGrow,fill:60px:noGrow,left:4dlu:noGrow,fill:185px:noGrow,left:4dlu:noGrow,fill:185px:noGrow,left:4dlu:noGrow,fill:80px:noGrow,left:4dlu:noGrow,fill:d:grow", "center:35px:noGrow"));
		tweakPanel.setBackground(new Color(-3342388));
		rootPanel.add(tweakPanel, cc.xyw(3, 3, 4, CellConstraints.FILL, CellConstraints.FILL));
		tweakPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new FormLayout("fill:80px:noGrow,left:4dlu:noGrow,fill:185px:noGrow,left:4dlu:noGrow,fill:185px:noGrow,left:4dlu:noGrow,fill:80px:noGrow,left:4dlu:noGrow,fill:d:grow", "center:35px:noGrow"));
		panel1.setBackground(new Color(-3355393));
		panel1.setOpaque(false);
		tweakPanel.add(panel1, cc.xyw(2, MINIMUM_LAYERS_COUNT, 9, CellConstraints.DEFAULT, CellConstraints.FILL));
		directoryChooseButton = new JButton();
		directoryChooseButton.setText("Folder");
		panel1.add(directoryChooseButton, cc.xy(MINIMUM_LAYERS_COUNT, MINIMUM_LAYERS_COUNT));
		barycenterRadioButton = new JRadioButton();
		barycenterRadioButton.setOpaque(false);
		barycenterRadioButton.setText("Barycenter");
		barycenterRadioButton.setMnemonic('B');
		barycenterRadioButton.setDisplayedMnemonicIndex(0);
		panel1.add(barycenterRadioButton, cc.xy(3, MINIMUM_LAYERS_COUNT, CellConstraints.FILL, CellConstraints.DEFAULT));
		medianRadioButton = new JRadioButton();
		medianRadioButton.setOpaque(false);
		medianRadioButton.setText("Median");
		medianRadioButton.setMnemonic('M');
		medianRadioButton.setDisplayedMnemonicIndex(0);
		panel1.add(medianRadioButton, cc.xy(5, MINIMUM_LAYERS_COUNT));
		layerLengthSlider = new JSlider();
		layerLengthSlider.setMajorTickSpacing(MINIMUM_LAYERS_COUNT);
		layerLengthSlider.setMaximum(INITIAL_LAYER_OFFSET);
		layerLengthSlider.setMinimum(MINIMUM_LAYERS_COUNT);
		layerLengthSlider.setOpaque(false);
		layerLengthSlider.setPaintLabels(false);
		layerLengthSlider.setPaintTicks(true);
		layerLengthSlider.setValue(MINIMAL_OFFSET);
		panel1.add(layerLengthSlider, cc.xy(9, MINIMUM_LAYERS_COUNT, CellConstraints.FILL, CellConstraints.DEFAULT));
		final JLabel label6 = new JLabel();
		label6.setText("Layer length:");
		panel1.add(label6, cc.xy(7, MINIMUM_LAYERS_COUNT));
		ButtonGroup buttonGroup;
		buttonGroup = new ButtonGroup();
		buttonGroup.add(barycenterRadioButton);
		buttonGroup.add(medianRadioButton);
	}

	/** @noinspection ALL */
	public JComponent $$$getRootComponent$$$() {
		return rootPanel;
	}
}
