package com.netinfo.main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.Timer;

import javax.imageio.ImageIO;

import com.netinfo.controller.NetInfoMainController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientUIMain extends Application {

	private static Stage stage;
	private Timer notificationTimer = new Timer();
	private DateFormat timeFormat = SimpleDateFormat.getTimeInstance();
	private static final String iconImageLoc = "http://icons.iconarchive.com/icons/scafer31000/bubble-circle-3/16/GameCenter-icon.png";
	private java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
	private  static java.awt.TrayIcon trayIcon=null;
	
	@Override
	public void start(Stage stage) throws Exception {
		Platform.runLater(this::addAppToTray);
		this.stage = stage;
		Platform.setImplicitExit(false);
		Parent parent = FXMLLoader.load(getClass().getResource("/fxml/netInfoMain.fxml"));
		Scene scene = new Scene(parent);
		stage.setScene(scene);
		stage.setTitle("Network Speed Info");
		stage.getScene().getStylesheets().add("/stylesheet/app.css");
		// confirmExit(primaryStage);
		stage.getIcons().add(new Image("/image/document.png"));
		// Image image=NetInfoMainController.textToImage("53B");
		// primaryStage.getIcons().add(image);
		// primaryStage.show();
		stage.setOnCloseRequest(event -> {
			Platform.exit();
		});
	}

	private void confirmExit(Stage primaryStage) {
		primaryStage.setOnCloseRequest(event -> {
			Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
			confirm.setResizable(false);
			confirm.setHeaderText(null);
			confirm.setContentText("Are you sure ?");
			Optional<ButtonType> buttonType = confirm.showAndWait();
			if (!buttonType.get().equals(ButtonType.OK)) {
				event.consume();
			}
		});
	}

	public static void main(String[] args) {
		// SpringApplicationContext springApplicationContext = new
		// SpringApplicationContext();
		launch(args);
	}

	/**
	 * Sets up a system tray icon for the application.
	 */
	private void addAppToTray() {
		try {
			// ensure awt toolkit is initialized.
			java.awt.Toolkit.getDefaultToolkit();

			// app requires system tray support, just exit if there is no
			// support.
			if (!java.awt.SystemTray.isSupported()) {
				System.out.println("No system tray support, application exiting.");
				Platform.exit();
			}

			// set up a system tray icon.
			
//			URL imageLoc = new URL(iconImageLoc);
			java.awt.Image image = ImageIO.read(getClass().getResource("/image/document.png"));
			
			trayIcon = new java.awt.TrayIcon(image);
			trayIcon.setToolTip("hello");
			trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

			java.awt.MenuItem openApp = new java.awt.MenuItem("Open Application");
			openApp.addActionListener(event -> Platform.runLater(this::showStage));

			java.awt.Font defaultFont = java.awt.Font.decode(null);
			java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
			openApp.setFont(boldFont);

			java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
			exitItem.addActionListener(event -> {
				notificationTimer.cancel();
				Platform.exit();
				tray.remove(trayIcon);
			});

			final java.awt.PopupMenu popup = new java.awt.PopupMenu();
			popup.add(openApp);
			popup.addSeparator();
			popup.add(exitItem);
			trayIcon.setPopupMenu(popup);

			// notificationTimer.schedule(new TimerTask() {
			// @Override
			// public void run() {
			// javax.swing.SwingUtilities.invokeLater(() ->
			// trayIcon.displayMessage("hello",
			// "The time is now " + timeFormat.format(new Date()),
			// java.awt.TrayIcon.MessageType.INFO));
			// }
			// }, 5_000, 60_000);

			tray.add(trayIcon);
		} catch (java.awt.AWTException | IOException e) {
			System.out.println("Unable to init system tray");
			e.printStackTrace();
		}
	}

	/**
	 * Shows the application stage and ensures that it is brought at the front
	 * of all stages.
	 */
	private void showStage() {
		if (stage != null) {
			stage.show();
			stage.toFront();
		}
	}

	public static void setMessage(String down, String up) {
		String value = "Download "+down+" | Upload "+up;
		trayIcon.setToolTip(value);
		BufferedImage image=SwingFXUtils.fromFXImage(NetInfoMainController.textToImage(down),null);
		trayIcon.setImage(image);
		stage.setTitle(value);
		stage.getIcons().add(SwingFXUtils.toFXImage(image, null));
	}
}
