import javax.swing.*;

public class Main {
	static JFrame frame;
	static SortingPanel panel;
	
	public static void main(String[] args) {
		frame = new JFrame("Sorting by Jacob");
		frame.setSize(640, 480);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		panel = new SortingPanel();
		panel.setSize(640, 480);
		frame.add(panel);
		
		frame.setVisible(true);
	}
}
