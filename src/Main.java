import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Main {
	static JFrame frame;
	static SortingPanel panel;
	static JScrollPane rightPane;
	
	public static void main(String[] args) {
		frame = new JFrame("Sorting by Jacob");
		frame.setSize(640, 480);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		FlowLayout layout = new FlowLayout();
		//panel.setSize(640, 480);
		
		frame.setLayout(layout);
		
		panel = new SortingPanel();
		
		rightPane = new JScrollPane();
		rightPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		rightPane.setMinimumSize(new Dimension(100, 0));
		rightPane.setPreferredSize(new Dimension(100, 10000));
		rightPane.setMaximumSize(new Dimension(100, 10000));
		rightPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		frame.add(panel);
		frame.add(rightPane);
		
		frame.setVisible(true);
		
		Timer timer = new Timer((int) (1000.0/60.0), e -> frame.repaint());
		timer.start();
		
		new Thread(() -> {
			List<Integer> list = new SortingList(panel);
			
			for (int i = 0; i < 100; i++) {
				list.add(i);
			}
			Collections.shuffle(list);
			System.out.println(Arrays.toString(list.toArray()));
			
			SortingAlgorithm algorithm = new BubbleSort();
			algorithm.sort(list);
		}).start();
	}
}
