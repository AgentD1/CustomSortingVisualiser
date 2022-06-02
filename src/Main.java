import javax.swing.*;
import java.util.List;

public class Main {
	static JFrame frame;
	static SortingPanel panel;
	
	public static void main(String[] args) {
		frame = new JFrame("Sorting by Jacob");
		frame.setSize(640, 480);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		panel = new SortingPanel();
		panel.setSize(640, 480);
		frame.add(panel);
		
		frame.setVisible(true);
		
		Timer timer = new Timer((int) (1000.0/60.0), e -> frame.repaint());
		timer.start();
		
		new Thread(() -> {
			List<Integer> list = new SortingList(panel);
			
			while(true) {
				for (int i = 0; i < 100; i++) {
					list.add(i);
				}
				
				for (int i = 0; i < 100; i++) {
					list.remove(0);
				}
				
				for (int i = 0; i < 100; i++) {
					list.add(0, i);
				}
				
				for (int i = 0; i < 100; i++) {
					list.set(i, i);
				}
				
				list.clear();
			}
		}).start();
	}
}
