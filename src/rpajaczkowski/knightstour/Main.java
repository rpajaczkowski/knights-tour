package rpajaczkowski.knightstour;

/*
Creating main window.
*/

import javax.swing.*;


public class Main {
	/*
	Create the GUI and show it. For thread safety,
	this method should be invoked from the event dispatch thread.
	*/
	private static void createAndShowGUI() {
		JFrame frame = new JFrame(KnightsTour.PROGRAM_NAME);
		
		KnightsTour kt = new KnightsTour();
		
		frame.setJMenuBar(kt.createMenuBar());
		frame.setContentPane(kt);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600,600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		//Schedule a job for the event dispatching thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
