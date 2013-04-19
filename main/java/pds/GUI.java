package main.java.pds;

import java.awt.EventQueue;
import main.java.pds.ui.*;

public class GUI {
	public static void run(final PhoneOperator phoneOperator, final Menu menu, final Stage startStage) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomeWindow frame = new WelcomeWindow(phoneOperator, menu, startStage);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
