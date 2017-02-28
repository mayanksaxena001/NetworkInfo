package com.netinfo.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.stereotype.Component;

import com.netinfo.main.ClientUIMain;
import com.netinfo.main.NetInfoMain;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;

@Component
public class NetInfoMainController implements Initializable {

	@FXML
	public  Label downLabel;

	@FXML
	public  Label upLabel;
	
	@FXML
	public Label totalLabel;
	
	@FXML
	public ImageView imageView;
	
	@FXML
	public Button button;
	
	private static long total=0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				new NetInfoMain(new Sigar());
				while (!isCancelled()) {
					Platform.runLater(() -> {
						try {
							newMetricThread();
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
					Thread.sleep(1000);
				}
				return null;
			}
		};
		     Thread th = new Thread(task);
		     th.start();
		     
		     imageView.setFitWidth(100);

	}
	
	 public  void newMetricThread() throws SigarException, InterruptedException {
         Long[] m = NetInfoMain.getMetric();
         
         long totalrx = m[0];
         long totaltx = m[1];
         downLabel.setText(formatSize(totalrx,true));
         upLabel.setText(formatSize(totaltx,true));
         totalLabel.setText(formatSize(total=totalrx+total,true));
         imageView.setImage(textToImage(formatSize(totalrx,true)));
         button.setGraphic(new ImageView(textToImage(formatSize(totalrx,true))));
         ClientUIMain.setMessage(downLabel.getText(),upLabel.getText());
 }
	 
	 public static String formatSize(long bytes, boolean si) {
		    int unit = si ? 1000 : 1024;
		    if (bytes < unit) return bytes + " B";
		    int exp = (int) (Math.log(bytes) / Math.log(unit));
		    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
		    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
		}

	 public static Image textToImage(String text) {
		    Label label = new Label(text);
//		    label.setMinSize(125, 125);
//		    label.setMaxSize(125, 125);
//		    label.setPrefSize(125, 125);
		    HBox hbox=new HBox(label);
		    hbox.setAlignment(Pos.CENTER);
		    hbox.setStyle("-fx-background-color: white; -fx-text-fill:black;");
		    Scene scene = new Scene(hbox,50,50);
		    WritableImage img = new WritableImage(50, 50) ;
		    scene.snapshot(img);
		    return img ;
		}

}