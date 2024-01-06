package gui;

import javax.swing.*;
import java.awt.*;

import gui.components.*;
import gui.components.button.*;
import gui.components.checkbox.*;
import gui.components.label.*;
import gui.components.panel.*;
import gui.components.progressbar.*;
import system.ConfigReader_APS;

public class AutomaticPlaybackSystemFrame_2 extends JFrame implements APS_Image {

	public AutomaticPlaybackSystemFrame_2(String title, String config_path) {

		///////////////////////
		///// Screen Size /////
		///////////////////////
		final int width = 480;
		final int height = 320;

		/////////////////
		///// Color /////
		/////////////////
		Color green = new Color(6, 199, 85);
		Color black = new Color(25, 25, 25);
		Color white = new Color(255, 255, 255);

		//////////////////////
		///// All JPanel /////
		//////////////////////
		JPanel cardPanel = new JPanel();
		JPanel OptionPanel = new JPanel();
		CardLayout CardLayout = new CardLayout();
		CardLayout OptionLayout = new CardLayout();

		setTitle(title);
		this.setIconImage((new ImageIcon(Base64Image.decodedImage(app_img))).getImage());
		setBounds(150, 150, width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		////////////////////////////
		///// Information Card /////
		////////////////////////////
		JPanel InformationPanel = new JPanel();
		InformationPanel.setLayout(null);
		InformationPanel.setBounds(0, 0, 640, 25);
		InformationPanel.setPreferredSize(new Dimension(640, 25));
		InformationPanel.setBackground(white);
		ChartLabel InformationLabel = new ChartLabel(15, "File Path: ", black, 20, 5, 640, 15);
		InformationPanel.add(InformationLabel);
		ChartLabel FilePathLabel = new ChartLabel(15, (new ConfigReader_APS(config_path)).getFileName(), black, 100, 5, 640, 15);
		InformationPanel.add(FilePathLabel);

		/////////////////////
		///// Main Card /////
		/////////////////////
		MyPanel MainCard = new MyPanel(1, white);

		ChartLabel TitleLabel = new ChartLabel(60, "APS 2", green,
				120, 20, 480, 80);
		ChartProgressBar ProgressBar = new ChartProgressBar(green, white,
				120, 100, 300, 20);
		ChartCheckBox NotificationCheckBox = new ChartCheckBox(15, "Notification", "notification", black, white, null,
				140, 120, 400, 40);
		NotificationCheckBox.setupIcon(1);
		NotificationCheckBox.setConfigFileName(config_path);
		ChartCheckBox AgreementCheckBox = new ChartCheckBox(15, "I accept the agreement", "agreement", black, white, null,
				140, 160, 400, 40);
		AgreementCheckBox.setupIcon(1);

		{
			MainCard.add(TitleLabel);
			MainCard.add(ProgressBar);
			MainCard.add(NotificationCheckBox);
			MainCard.add(AgreementCheckBox);
		}

		///////////////////////
		///// Option Card /////
		///////////////////////
		MyPanel OptionCard = new MyPanel(2, white);
		OptionCard.setChart(0, height - 50, width, 50);

		ChartButton SelectFileCommand = new ChartButton(20, "file", "select_file", black,
				width / 2 - 200, 5, 100, 40);
		SelectFileCommand.setupIcon(2);
		SelectFileCommand.setRelatedComponents(null, FilePathLabel, null, null, null, null);

		ChartButton ExecuteCommand = new ChartButton(20, "run", "execute", black,
				width / 2 - 100, 5, 100, 40);
		ExecuteCommand.setupIcon(1);
		ExecuteCommand.setConfigPath(config_path);
		ExecuteCommand.setAPSThread();
		ExecuteCommand.setRelatedComponents(this, FilePathLabel, SelectFileCommand, ProgressBar, NotificationCheckBox, AgreementCheckBox);
		AgreementCheckBox.setRelatedButton(ExecuteCommand);

		ChartButton CancelCommand = new ChartButton(20, "cancel", "cancel", black,
				width / 2, 5, 100, 40);
		CancelCommand.setupIcon(3);
		CancelCommand.setRelatedComponents(this, FilePathLabel, SelectFileCommand, null, NotificationCheckBox, AgreementCheckBox);

		ChartButton CloseCommand = new ChartButton(20, "close", "close", black,
				width / 2 + 100, 5, 100, 40);
		CloseCommand.setupIcon(4);

		{
			OptionCard.add(SelectFileCommand);
			OptionCard.add(ExecuteCommand);
			OptionCard.add(CancelCommand);
			OptionCard.add(CloseCommand);
		}

		///////////////////////
		///// Card Layout /////
		///////////////////////
		cardPanel.setLayout(CardLayout);
		cardPanel.add(MainCard, "MainCard");

		/////////////////////////
		///// Option Layout /////
		/////////////////////////
		OptionPanel.setLayout(OptionLayout);
		OptionPanel.add(OptionCard, "OptionCard");

		getContentPane().add(InformationPanel, BorderLayout.NORTH);
		getContentPane().add(cardPanel, BorderLayout.CENTER);
		getContentPane().add(OptionPanel, BorderLayout.SOUTH);
	}
}
