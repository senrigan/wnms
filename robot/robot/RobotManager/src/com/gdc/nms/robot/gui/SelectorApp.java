package com.gdc.nms.robot.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.gdc.nms.robot.util.CreatorRobotManager;
import com.gdc.nms.robot.util.ValidatorManagement;
import com.gdc.nms.robot.util.indexer.AppJsonObject;
import com.gdc.nms.robot.util.indexer.FlujoInformation;
import com.gdc.robothelper.webservice.robot.CreatorRobotWebService;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;

public class SelectorApp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JComboBox<AppJsonObject> comboBox;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JButton cancelButton;
	private JButton continueButton;
	private JFrame mainFrame=this;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SelectorApp frame = new SelectorApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SelectorApp() {
		init();
		setApplicationNames();
//		setTestApplicatioNames();
		setButtonListener();
	}
	
	
	private  void init(){
		setTitle("A\u00F1adir Robot");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450,170);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 392, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		lblNewLabel_1 = new JLabel("Selecciona La Aplicacion que deseas a\u00F1adir \r\n");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 1;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		lblNewLabel = new JLabel("Application");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 3;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
		comboBox = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 4;
		contentPane.add(comboBox, gbc_comboBox);
		
		cancelButton = new JButton("Cancelar");
		GridBagConstraints gbc_cancelButton = new GridBagConstraints();
		gbc_cancelButton.insets = new Insets(0, 0, 5, 5);
		gbc_cancelButton.gridx = 1;
		gbc_cancelButton.gridy = 5;
		contentPane.add(cancelButton, gbc_cancelButton);
		
		continueButton = new JButton("Continuar");
		GridBagConstraints gbc_continueButton = new GridBagConstraints();
		gbc_continueButton.insets = new Insets(0, 0, 5, 0);
		gbc_continueButton.gridx = 2;
		gbc_continueButton.gridy = 5;
		contentPane.add(continueButton, gbc_continueButton);
	}
	
	
	private void setApplicationNames(){
		ArrayList<AppJsonObject> appsName = ValidatorManagement.getAppsName();
		for (AppJsonObject appJsonObject : appsName) {
			String alias = appJsonObject.getAlias();
			comboBox.addItem(appJsonObject);
		}
	}
	
	private void setTestApplicatioNames(){
		for (int i = 0; i < 10; i++) {
			AppJsonObject js=new AppJsonObject();
			js.setAlias("hola");
			js.setId(i);
			comboBox.addItem(js);
		}
	}
	
	private void setButtonListener(){
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				closeWindows();
			}
		});
		
		continueButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int showOpenDialog = chooser.showOpenDialog(mainFrame);
				if(showOpenDialog==JFileChooser.APPROVE_OPTION){
					File selectedFile = chooser.getSelectedFile();
					Path path = selectedFile.toPath();
					AppJsonObject selectedItem = (AppJsonObject) comboBox.getSelectedItem();
//					ArrayList<FlujoInformation> validFlujos = ValidatorManagement.
//							getValidFlujos(path,selectedItem.getId());
					ArrayList<FlujoInformation> validFlujos;
					Path data;
					if(ValidatorManagement.isValidMainFolder(path)){
						validFlujos = ValidatorManagement.getValidFlujos(path.resolve("application"),selectedItem.getId());
						data=path.resolve("data");
						createFlujosWithData(data, validFlujos, selectedItem);
					}else{
						 validFlujos= ValidatorManagement.
								getValidFlujosTest(path,selectedItem.getId());
						 createFlujosWithoutData(validFlujos, selectedItem);

					}
					
										
				}
//				chooser.showOpenDialog(null);
//				JOptionPane.showMessageDialog(null, "La carpeta fue insatalada correctamente.", "Correcto", JOptionPane.INFORMATION_MESSAGE);
//				JOptionPane.showMessageDialog(null, "Carpeta Incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
	
	
	private void createFlujosWithData(Path data,ArrayList<FlujoInformation> validFlujos,AppJsonObject selectedItem ){
		if(!validFlujos.isEmpty()){
			CreatorRobotManager creator=new CreatorRobotManager();
			if(creator.createRobot(selectedItem.getAlias(),selectedItem.getId(),validFlujos)){
				
				
				JOptionPane.showMessageDialog(null, "La carpeta fue insatalada correctamente.", "Correcto", JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(null,
						 "No existe Flujo Valido Correspondiente a la aplicacion dada","Error.", JOptionPane.ERROR_MESSAGE);

			}
			
		}
		else{
			JOptionPane.showMessageDialog(null,
					 "No existe Flujo Valido Correspondiente a la aplicacion dada","Error.", JOptionPane.ERROR_MESSAGE);

		}
	}
	private void createFlujosWithoutData(ArrayList<FlujoInformation> validFlujos,AppJsonObject selectedItem ){
		if(!validFlujos.isEmpty()){
			CreatorRobotManager creator=new CreatorRobotManager();
			if(creator.createRobot(selectedItem.getAlias(),selectedItem.getId(),validFlujos)){
				
				
				JOptionPane.showMessageDialog(null, "La carpeta fue insatalada correctamente.", "Correcto", JOptionPane.INFORMATION_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(null,
						 "No existe Flujo Valido Correspondiente a la aplicacion dada","Error.", JOptionPane.ERROR_MESSAGE);

			}
			
		}
		else{
			JOptionPane.showMessageDialog(null,
					 "No existe Flujo Valido Correspondiente a la aplicacion dada","Error.", JOptionPane.ERROR_MESSAGE);

		}
	}
	
	private void closeWindows(){
		this.dispose();
	}
	
	
	

}
