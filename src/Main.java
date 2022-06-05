import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class Main {
	static JFrame frame;
	static SortingPanel panel;
	static JPanel rightPane;
	
	public static void main(String[] args) {
		SortingAlgorithm[] algorithms = new SortingAlgorithm[] {
				new BubbleSort(), // If you want to add a new sorting algorithm to the list, add it here like this: new YourClass(),
		};
		
		Map<String, DataGenerator> dataGenerators = Map.of(
				"Sequential Data", new SequentialDataGenerator(),
				"Random Data", new RandomDataGenerator()
		);
		
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter numbersOnlyFormatter = new NumberFormatter(format);
		numbersOnlyFormatter.setValueClass(Integer.class);
		numbersOnlyFormatter.setMinimum(1);
		numbersOnlyFormatter.setMaximum(100000);
		numbersOnlyFormatter.setAllowsInvalid(true);
		numbersOnlyFormatter.setCommitsOnValidEdit(true);
		
		
		frame = new JFrame("Sorting by Jacob");
		frame.setSize(840, 480);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		FlowLayout layout = new FlowLayout();
		
		frame.setLayout(layout);
		
		panel = new SortingPanel();
		frame.add(panel);
		panel.setPreferredSize(new Dimension(640, 480));
		
		rightPane = new JPanel();
		rightPane.setPreferredSize(new Dimension(400, 480));
		
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
		
		JLabel chooseAlgorithmLabel = new JLabel("Choose a sorting algorithm from the list below:");
		rightPane.add(chooseAlgorithmLabel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JComboBox<String> chooseAlgorithmBox = new JComboBox<>(Arrays.stream(algorithms).map(a -> a.getClass().getName()).toArray(String[]::new));
		rightPane.add(chooseAlgorithmBox);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JLabel sortControlsLabel = new JLabel("Use the buttons below to control the sorting:");
		rightPane.add(sortControlsLabel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		
		JPanel playButtonsPanel = new JPanel();
		playButtonsPanel.setLayout(new BoxLayout(playButtonsPanel, BoxLayout.X_AXIS));
		
		JButton leftButton = new JButton("⏪");
		leftButton.addActionListener(System.out::println);
		JButton playPauseButton = new JButton("⏵"); // ⏸
		JButton rightButton = new JButton("⏩");
		
		playButtonsPanel.add(leftButton);
		playButtonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		playButtonsPanel.add(playPauseButton);
		playButtonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		playButtonsPanel.add(rightButton);
		playButtonsPanel.add(Box.createHorizontalGlue());
		
		rightPane.add(playButtonsPanel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JButton resetButton = new JButton("Reset");
		rightPane.add(resetButton);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JLabel settingsLabel = new JLabel("Settings:");
		rightPane.add(settingsLabel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JPanel dataPointsNumberPanel = new JPanel();
		dataPointsNumberPanel.setLayout(new BoxLayout(dataPointsNumberPanel, BoxLayout.X_AXIS));
		JLabel dataPointsNumberLabel = new JLabel("Number of data points:");
		JFormattedTextField dataPointsNumberField = new JFormattedTextField(numbersOnlyFormatter);
		dataPointsNumberField.setValue(100);
		dataPointsNumberPanel.add(dataPointsNumberLabel);
		dataPointsNumberPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		dataPointsNumberPanel.add(dataPointsNumberField);
		rightPane.add(dataPointsNumberPanel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		
		JPanel dataGeneratorPanel = new JPanel();
		dataGeneratorPanel.setLayout(new BoxLayout(dataGeneratorPanel, BoxLayout.X_AXIS));
		JLabel dataGeneratorLabel = new JLabel("Data format:");
		dataGeneratorPanel.add(dataGeneratorLabel);
		JComboBox<String> dataGeneratorSelector = new JComboBox<>(dataGenerators.keySet().toArray(new String[0]));
		dataGeneratorPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		dataGeneratorPanel.add(dataGeneratorSelector);
		rightPane.add(dataGeneratorPanel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		Thread.currentThread().stop();
		
		
		rightPane.add(Box.createVerticalGlue());
		
		frame.add(rightPane);
		
		frame.pack();
		
		frame.setVisible(true);
		
		Timer timer = new Timer((int) (1000.0 / 60.0), e -> frame.repaint());
		timer.start();
		
		new Thread(() -> {
			List<Integer> list = new SortingList(panel);
			
			for (int i = 1; i < 100; i++) {
				list.add(i);
			}
			Collections.shuffle(list);
			System.out.println(Arrays.toString(list.toArray()));
			
			SortingAlgorithm algorithm = new BubbleSort();
			algorithm.sort(list);
		}).start();
	}
	
	interface DataGenerator {
		List<Integer> data(int num);
	}
	
	static class SequentialDataGenerator implements DataGenerator {
		@Override
		public List<Integer> data(int num) {
			List<Integer> list = new ArrayList<>();
			for (int i = 0; i < num; i++) {
				list.add(i);
			}
			return list;
		}
	}
	
	static class RandomDataGenerator implements DataGenerator {
		@Override
		public List<Integer> data(int num) {
			Random random = new Random();
			List<Integer> list = new ArrayList<>();
			for (int i = 0; i < num; i++) {
				list.add(random.nextInt());
			}
			return list;
		}
	}
}
