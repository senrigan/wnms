package com.gdc.nms.robot.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.JMenuBar;
import javax.swing.JMenu;

import com.gdc.nms.robot.gui.tree.Element;
import com.gdc.nms.robot.gui.tree.TreeListener;
import com.gdc.nms.robot.gui.tree.TreeModelElements;
import com.gdc.nms.robot.util.AppExaminator;
import com.gdc.nms.robot.util.CommandExecutor;
import com.gdc.nms.robot.util.Constants;
import com.gdc.nms.robot.util.ValidatorManagement;
import com.gdc.nms.robot.util.indexer.AppInformation;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class RobotManagerGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTree appTree;
	private JPanel panelInfo;
	private JTextArea textArea;
	private JButton startButton;
	private JButton stopButton;
	private JScrollPane scrollPane_1;
	private JSplitPane splitPane;
	private JScrollPane scrollPane;
	private JMenuBar menuBar;
	private TreeListener listenerTree;
	private TreeModelElements model;
	private Element root;
	private Element runningNode;
	private Element notRunningNode;

	public RobotManagerGui() {
		super("SisproRobotManager");
		
		System.out.println("iniciando componentes");
		initComponents();
		
		// JMenuItem mnNewMenu = new JMenuItem("Actualizar Robot");
		// mnAr.add(mnNewMenu);
		setLocationRelativeTo(null);
		System.out.println("mostrando");
		setVisible(true);
		
		
		setNodesParent();
		System.out.println("corriendo tarea");
		runTask();
		System.out.println("expandiendo todos");
		expandAll();
		
	}
	
	private void initComponents(){
		System.out.println("procesando arbol");
		model = new TreeModelElements(getDataForTree());
		appTree = new JTree(model);
		System.out.println("pprosesando deamas cosas");
		appTree.setEditable(true);
		listenerTree = new TreeListener(RobotManagerGui.this, appTree);
		appTree.addTreeSelectionListener(listenerTree);
		scrollPane_1 = new JScrollPane(appTree);
		root = (Element) model.getRoot();
		

		
		panelInfo = new JPanel();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, scrollPane_1, panelInfo);
		GridBagLayout gbl_panelInfo = new GridBagLayout();
		gbl_panelInfo.columnWidths = new int[] { 0, 0, 0 };
		gbl_panelInfo.rowHeights = new int[] { 0, 171, 0, 0 };
		gbl_panelInfo.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
		gbl_panelInfo.rowWeights = new double[] { 0.0, 1.0, 1.0, Double.MIN_VALUE };
		panelInfo.setLayout(gbl_panelInfo);
		textArea = new JTextArea();
		textArea.setOpaque(false);
		textArea.setEditable(false);
		scrollPane = new JScrollPane(textArea);
		scrollPane.getViewport().setOpaque(false);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		panelInfo.add(scrollPane, gbc_scrollPane);

		// panel.add(textPane);

		startButton = new JButton("Run Robot");
		startButton.setEnabled(false);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 2;
		panelInfo.add(startButton, gbc_btnNewButton);

		stopButton = new JButton("Stop Robot");
		stopButton.setEnabled(false);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.gridx = 1;
		gbc_btnNewButton_1.gridy = 2;
		panelInfo.add(stopButton, gbc_btnNewButton_1);
		getContentPane().add(splitPane);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(640, 480);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnAr = new JMenu("Archivo");
		menuBar.add(mnAr);
		setButtonsListeners();
		JMenuItem mnA = new JMenuItem("Agregar Robot");
		mnA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// new Thread(new Runnable() {
				// @Override
				// public void run() {
				// JDialog f = new JDialog();
				// JPanel p = new JPanel();
				// p.setLayout(new FlowLayout());
				// p.add(new JTextField());
				// p.add(new JButton("Buscar"));
				// p.add(new JButton("Validar"));
				// f.add(p, BorderLayout.CENTER);
				// f.setSize(400, 300);
				// f.setLayout(new BorderLayout());
				// f.setModal(true);
				// f.setVisible(true);
				// }
				// }).start();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
//						if (ValidatorManagement.getAppsName() != null) {

							SelectorApp selector = new SelectorApp();
							selector.setVisible(true);
//						} else {
//							JOptionPane.showMessageDialog(null, "No es Posible Registrar Robots en este momento",
//									"Error", JOptionPane.ERROR_MESSAGE);
//						}
					}
				});

			}
		});
		mnAr.add(mnA);
	}

	private void setButtonsListeners() {
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Element element = (Element) appTree.getLastSelectedPathComponent();
				AppInformation appinfo = element.getAppinfo();
				long idRobot = appinfo.getIdRobot();
				if (RobotManager.runRobot(idRobot)) {
					enableButton(ButtonType.START, false);
					JOptionPane.showMessageDialog(null, "el robot se inicio correctamente", "Info",
							JOptionPane.INFORMATION_MESSAGE);
					enableButton(ButtonType.STOP, false);
					removeRegistryNotRunning(idRobot);
					registryRunningRobot(idRobot);
				} else {
					JOptionPane.showMessageDialog(null, "No es Posible Iniciar al Robot en este momento", "Error",
							JOptionPane.ERROR_MESSAGE);
					enableButton(ButtonType.START, false);

				}
			}
		});

		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Element element = (Element) appTree.getLastSelectedPathComponent();
				AppInformation appinfo = element.getAppinfo();
				long idRobot = appinfo.getIdRobot();
				try {
					RobotManager.stopRobot(idRobot);
					enableButton(ButtonType.STOP, false);

					JOptionPane.showMessageDialog(null, "El robot se detuvo correctamente", "Error",
							JOptionPane.INFORMATION_MESSAGE);
					enableButton(ButtonType.START, false);

					setInformation(" ");
					removeRegistryRunningRobot(idRobot);
					registryStopedRobot(idRobot);
				} catch (Exception e1) {

					JOptionPane.showMessageDialog(null, "No es Posible Detener al Robot en este momento", "Error",
							JOptionPane.ERROR_MESSAGE);
					enableButton(ButtonType.STOP, true);

					e1.printStackTrace();
				}
			}
		});
	}

	private void registryStopedRobot(long idRobot) {
		try {
			String registry = CommandExecutor.readRegistrySpecificRegistry(Constants.LOCALREGISTRY, "robotnotRun",
					"REG_SZ");
			if (registry.endsWith(",")) {
				registry = registry.substring(0, registry.length() - 1);
			}
			String[] split = registry.split(",");
			boolean isContained = false;
			for (int i = 0; i < split.length; i++) {
				if (split[i].length() > 0) {
					long stopedid = Long.parseLong(split[i]);
					if (idRobot == stopedid) {
						isContained = true;
					}

				}
			}
			if (!isContained) {
				registry = registry + "," + idRobot;
			}
			CommandExecutor.addRegistryWindows(Constants.LOCALREGISTRY, "robotnotRun", registry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void registryRunningRobot(long idRobot) {
		try {
			String registry = CommandExecutor.readRegistrySpecificRegistry(Constants.LOCALREGISTRY, "robotmustRun",
					"REG_SZ");
			if (registry.endsWith(",")) {
				registry = registry.substring(0, registry.length() - 1);
			}
			String[] split = registry.split(",");
			boolean isContained = false;
			for (int i = 0; i < split.length; i++) {
				if (split[i].length() > 0) {
					long stopedid = Long.parseLong(split[i]);
					if (idRobot == stopedid) {
						isContained = true;
					}

				}
			}
			if (!isContained) {
				registry = registry + "," + idRobot;
			}
			CommandExecutor.addRegistryWindows(Constants.LOCALREGISTRY, "robotmustRun", registry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removeRegistryRunningRobot(long idRobot) {
		try {
			String registry = CommandExecutor.readRegistrySpecificRegistry(Constants.LOCALREGISTRY, "robotmustRun",
					"REG_SZ");
			if (registry.endsWith(",")) {
				registry = registry.substring(0, registry.length() - 1);
			}

			String[] split = registry.split(",");
			boolean isContained = false;
			String newRegistry = "";
			for (int i = 0; i < split.length; i++) {
				if (split[i].length() > 0) {
					long stopedid = Long.parseLong(split[i]);

					if (idRobot == stopedid) {
						isContained = true;
					} else {
						newRegistry += stopedid + ",";
					}

				}
			}
			CommandExecutor.addRegistryWindows(Constants.LOCALREGISTRY, "robotmustRun", newRegistry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removeRegistryNotRunning(long idRobot) {
		try {
			String registry = CommandExecutor.readRegistrySpecificRegistry(Constants.LOCALREGISTRY, "robotnotRun",
					"REG_SZ");
			if (registry.endsWith(",")) {
				registry = registry.substring(0, registry.length() - 1);
			}

			String[] split = registry.split(",");
			boolean isContained = false;
			String newRegistry = "";
			for (int i = 0; i < split.length; i++) {
				if (split[i].length() > 0) {

					long stopedid = Long.parseLong(split[i]);

					if (idRobot == stopedid) {
						isContained = true;
					} else {
						newRegistry += stopedid + ",";
					}
				}
			}
			CommandExecutor.addRegistryWindows(Constants.LOCALREGISTRY, "robotnotRun", newRegistry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void runTask() {
		// Timer timer=new Timer();
		// TimerTask task=new TimerTask() {
		//
		// @Override
		// public void run() {
		// try{
		// System.out.println("actualizando running");
		// updateNodes(runningNode, getElementRunning());
		// System.out.println("actualizando no running");
		// updateNodes(notRunningNode, getElementNotRunning());
		//
		// }catch(Exception ex){
		// ex.printStackTrace();
		// }
		//
		// }
		// };
		// timer.schedule(task, 0,30000);
		Thread hilo = new Thread(new Runnable() {
			public void run() {
				while (true) {
					updateTree();
				}
			}
		});
		hilo.start();
	}

	private  void updateTree() {
		try {
//			System.out.println("E: " + SwingUtilities.isEventDispatchThread());
//			System.out.println("borrando datos del arbol");
			// appTree.clearSelection();
			// appTree.setModel(new TreeModelElements());
			appTree.removeTreeSelectionListener(listenerTree);

			appTree.setModel(new TreeModelElements(getDataForTree()));
			appTree.addTreeSelectionListener(listenerTree);
			// DefaultTreeModel model = (DefaultTreeModel)appTree.getModel();
			// DefaultMutableTreeNode
			// root=(DefaultMutableTreeNode)model.getRoot();
			// root.add(new DefaultMutableTreeNode(getDataForTree()));
			// model.reload();

//			System.out.println("esperando a siguente actializadicon");
			expandAll();

			Thread.sleep(10000);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void expandAll() {
		int row = 0;
		while (row < appTree.getRowCount()) {
			appTree.expandRow(row);
			row++;
		}
	}

	@SuppressWarnings("unchecked")
	private void updateNodes(Element toUpdate, Element dataForUpdate) {
		Enumeration<Element> children = (Enumeration<Element>) dataForUpdate.children();
		Enumeration<Element> childrenNode = toUpdate.children();
		ArrayList<Element> elementsToAdd = new ArrayList<Element>();
		ArrayList<Element> foundElements = new ArrayList<Element>();
		while (children.hasMoreElements()) {
			Element object = children.nextElement();
			while (childrenNode.hasMoreElements()) {
				Element object2 = childrenNode.nextElement();
				if (object.toString().equals(object2.toString())) {
					System.out.println("elemenetos iguales" + object.toString());
					if (elementsToAdd.contains(object2)) {
						elementsToAdd.remove(object2);
					}
					foundElements.add(object2);
					break;
				} else {
					// long index=runninNode.getIndex(object2);
					if (!elementsToAdd.contains(object2)) {
						elementsToAdd.add(object2);
					}
				}
			}
		}
		for (Element element : elementsToAdd) {
			if (!foundElements.contains(element)) {
				System.out.println("boorando elemento" + element);
				toUpdate.remove(element);

			}
		}
	}

	private void setNodesParent() {
		@SuppressWarnings("unchecked")
		Enumeration<Element> children = root.children();
		while (children.hasMoreElements()) {
			Element object = children.nextElement();
			if (object.toString().equals("Running")) {
				runningNode = object;
			} else if (object.toString().equals("Not Running")) {
				notRunningNode = object;
			}
		}
	}

	public synchronized Element getDataForTree() {
		Vector<Thread> hilos=new Vector<>();
		System.out.println("procesando elementos del arbol");

		Element appTree = new Element("Aplication");
		Thread hilo=new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("elementos que no corren");
				appTree.add(getElementNotRunning());
			}
		});
		hilos.add(hilo);
		hilo.start();
		Thread hilo2=new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("++elementos que corren");
				appTree.add(getElementRunning());
			}
		});
		hilos.add(hilo2);
		hilo2.start();
		for (Thread thread : hilos) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("///termono de hacer elementos");

		// System.out.println("childAt"+childAt.);
		return appTree;

	}

	private synchronized Element getElementRunning() {
		System.out.println("opteninando apps");
		Element element = new Element("Running");
		ArrayList<AppInformation> runningApps = AppExaminator.getRunningApps();
		 System.out.println("running apps \n"+runningApps+"\n");
		for (AppInformation appInformation : runningApps) {
			Element elementNode = new Element(appInformation.getAppName());
			elementNode.setAppinfo(appInformation);
			elementNode.setStopAble(true);
			element.add(elementNode);

		}
		System.out.println("termino getlementrunning");
		return element;
	}

	private synchronized Element getElementNotRunning() {
		Element element = new Element("Not Running");
		ArrayList<AppInformation> notRunningApps = AppExaminator.getNotRunnigApps();
		// System.out.println("not running apps \n"+notRunningApps+"\n");
		for (AppInformation appInformation : notRunningApps) {
			Element elementNode = new Element(appInformation.getAppName());
			elementNode.setAppinfo(appInformation);
			elementNode.setStopAble(false);
			element.add(elementNode);
		}
		return element;
	}

	public void setInformation(String info) {
		this.textArea.setText(info);
	}

	public void enableButton(ButtonType type, boolean status) {
		switch (type) {
		case START:
			startButton.setEnabled(status);
			break;
		case STOP:
			stopButton.setEnabled(status);
			break;
		}
	}

	public enum ButtonType {
		START, STOP
	}
}
