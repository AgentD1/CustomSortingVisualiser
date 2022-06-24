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
	
	static Map<String, DataGenerator> dataGenerators;
	static SortingAlgorithm[] algorithms;
	static JComboBox<String> chooseAlgorithmBox;
	static JFormattedTextField dataPointsNumberField;
	static JComboBox<String> dataGeneratorSelector;
	static JButton playPauseButton;
	static JLabel speedLabel;
	
	static SortingList sortingList;
	static Thread sortingThread;
	
	
	public static void main(String[] args) {
		algorithms = new SortingAlgorithm[] {
				new BubbleSort(), // If you want to add a new sorting algorithm to the list, add it here like this: new YourClass(),
		};
		
		dataGenerators = Map.of(
				"Sequential Data", new SequentialDataGenerator(),
				"Random Data", new RandomDataGenerator(),
				"Sorted Data", new SortedDataGenerator(),
				"Almost Sorted Data (Outliers)", new AlmostSortedDataGenerator(),
				"Almost Sorted Data (Staggered)", new AlmostSortedStaggeredDataGenerator()
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
		frame.setResizable(false);
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
		
		
		JPanel chooseAlgorithmPanel = new JPanel();
		chooseAlgorithmPanel.setLayout(new BoxLayout(chooseAlgorithmPanel, BoxLayout.X_AXIS));
		JLabel chooseAlgorithmLabel = new JLabel("Choose a sorting algorithm from the list below:");
		chooseAlgorithmPanel.add(chooseAlgorithmLabel);
		chooseAlgorithmPanel.add(Box.createHorizontalGlue());
		rightPane.add(chooseAlgorithmPanel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		chooseAlgorithmBox = new JComboBox<>(Arrays.stream(algorithms).map(a -> a.getClass().getName()).toArray(String[]::new));
		chooseAlgorithmBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, chooseAlgorithmBox.getPreferredSize().height));
		rightPane.add(chooseAlgorithmBox);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		
		JPanel sortControlsPanel = new JPanel();
		sortControlsPanel.setLayout(new BoxLayout(sortControlsPanel, BoxLayout.X_AXIS));
		JLabel sortControlsLabel = new JLabel("Use the buttons below to control the sorting:");
		sortControlsPanel.add(sortControlsLabel);
		sortControlsPanel.add(Box.createHorizontalGlue());
		rightPane.add(sortControlsPanel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		
		JPanel playButtonsPanel = new JPanel();
		playButtonsPanel.setLayout(new BoxLayout(playButtonsPanel, BoxLayout.X_AXIS));
		
		JButton leftButton = new JButton("⏪");
		playPauseButton = new JButton("⏵");
		playPauseButton.addActionListener(e -> {
			panel.playPause();
		});
		leftButton.addActionListener(e -> {
			panel.speedIndex--;
			if (panel.speedIndex < 0) panel.speedIndex = 0;
			speedLabel.setText(String.format("Speed: %.2fx", panel.getSpeed()));
		});
		JButton rightButton = new JButton("⏩");
		rightButton.addActionListener(e -> {
			panel.speedIndex++;
			if (panel.speedIndex >= panel.speedOptions.length) panel.speedIndex = panel.speedOptions.length - 1;
			speedLabel.setText(String.format("Speed: %.2fx", panel.getSpeed()));
		});
		
		playButtonsPanel.add(leftButton);
		playButtonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		playButtonsPanel.add(playPauseButton);
		playButtonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		playButtonsPanel.add(rightButton);
		playButtonsPanel.add(Box.createHorizontalGlue());
		
		rightPane.add(playButtonsPanel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JPanel speedLabelPanel = new JPanel();
		speedLabelPanel.setLayout(new BoxLayout(speedLabelPanel, BoxLayout.X_AXIS));
		speedLabel = new JLabel("Speed: 1.00x");
		speedLabelPanel.add(speedLabel);
		speedLabelPanel.add(Box.createHorizontalGlue());
		
		rightPane.add(speedLabelPanel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(e -> panel.reset());
		rightPane.add(resetButton);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JLabel settingsLabel = new JLabel("Settings:");
		rightPane.add(settingsLabel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JPanel dataPointsNumberPanel = new JPanel();
		dataPointsNumberPanel.setLayout(new BoxLayout(dataPointsNumberPanel, BoxLayout.X_AXIS));
		JLabel dataPointsNumberLabel = new JLabel("Number of data points:");
		dataPointsNumberField = new JFormattedTextField(numbersOnlyFormatter);
		dataPointsNumberField.setValue(100);
		dataPointsNumberField.setMaximumSize(new Dimension(Integer.MAX_VALUE, dataPointsNumberField.getPreferredSize().height));
		dataPointsNumberPanel.add(dataPointsNumberLabel);
		dataPointsNumberPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		dataPointsNumberPanel.add(dataPointsNumberField);
		rightPane.add(dataPointsNumberPanel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		
		JPanel dataGeneratorPanel = new JPanel();
		dataGeneratorPanel.setLayout(new BoxLayout(dataGeneratorPanel, BoxLayout.X_AXIS));
		JLabel dataGeneratorLabel = new JLabel("Data format:");
		dataGeneratorPanel.add(dataGeneratorLabel);
		dataGeneratorSelector = new JComboBox<>(dataGenerators.keySet().toArray(new String[0]));
		dataGeneratorSelector.setMaximumSize(new Dimension(Integer.MAX_VALUE, dataGeneratorSelector.getPreferredSize().height));
		dataGeneratorPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		dataGeneratorPanel.add(dataGeneratorSelector);
		rightPane.add(dataGeneratorPanel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JButton loadCodeButton = new JButton("Load Coad");
		loadCodeButton.addActionListener(e -> CodeLoader.load("""
				import java.util.List;
				
				// Replace TemplateAlgorithm with the name of your algorithm, like QuickSort (but not BubbleSort because its already taken).
				public class TemplateAlgorithm implements SortingAlgorithm {
					@Override
					public void sort(List<Integer> list) { // The name and arguments of this method can't be changed.
						// put your code here
						swap(0, 1, list); // Swap 2 values
						int first = list.get(0); // Get the value at an index of a list
						list.set(0, 2050); // Set the value of the list at index to 2050
					}
					
					// New methods can be added to your heart's content
					
					// A swap method is included to make it look nice. Use it if you want.
					// public void swap(int index1, int index2, List<Integer> list) {}
				}
				
				"""));
		rightPane.add(loadCodeButton);
		
		rightPane.add(Box.createVerticalGlue());
		
		frame.add(rightPane);
		
		frame.pack();
		
		frame.setVisible(true);
		
		Timer timer = new Timer((int) (1000.0 / 60.0), e -> frame.repaint());
		timer.start();
		
		sortingThread = new Thread(() -> {
			List<Integer> list = new SortingList(panel);
			
			for (int i = 1; i < 100; i++) {
				list.add(i);
			}
			Collections.shuffle(list);
			System.out.println(Arrays.toString(list.toArray()));
			
			SortingAlgorithm algorithm = new BubbleSort();
			algorithm.sort(list);
		});
		sortingThread.start();
	}
	
	interface DataGenerator {
		List<Integer> data(int num);
	}
	
	static class SequentialDataGenerator implements DataGenerator {
		@Override
		public List<Integer> data(int num) {
			List<Integer> list = new ArrayList<>();
			for (int i = 1; i <= num; i++) {
				list.add(i);
			}
			Collections.shuffle(list);
			return list;
		}
	}
	
	static class RandomDataGenerator implements DataGenerator {
		@Override
		public List<Integer> data(int num) {
			Random random = new Random();
			List<Integer> list = new ArrayList<>();
			for (int i = 0; i < num; i++) {
				list.add(Math.abs(random.nextInt()));
			}
			Collections.shuffle(list);
			return list;
		}
	}
	
	static class SortedDataGenerator implements DataGenerator {
		@Override
		public List<Integer> data(int num) {
			List<Integer> list = new ArrayList<>();
			for (int i = 1; i <= num; i++) {
				list.add(i);
			}
			return list;
		}
	}
	
	static class AlmostSortedDataGenerator implements DataGenerator {
		@Override
		public List<Integer> data(int num) {
			List<Integer> list = new ArrayList<>();
			for (int i = 1; i <= num; i++) {
				list.add(i);
			}
			Random random = new Random();
			for (int i = 0; i < num / 5; i++) {
				int index1 = random.nextInt(num);
				int index2 = random.nextInt(num);
				int element1 = list.get(index1);
				list.set(index1, list.get(index2));
				list.set(index2, element1);
			}
			return list;
		}
	}
	
	static class AlmostSortedStaggeredDataGenerator implements DataGenerator {
		@Override
		public List<Integer> data(int num) {
			List<Integer> list = new ArrayList<>();
			for (int i = 1; i <= num; i++) {
				list.add(i);
			}
			Random random = new Random();
			for (int i = 0; i < num / 2; i++) {
				int index1 = random.nextInt(num);
				int index2 = random.nextInt(Math.max(0, index1 - 2), Math.min(index1 + 3, num - 1));
				int element1 = list.get(index1);
				list.set(index1, list.get(index2));
				list.set(index2, element1);
			}
			return list;
		}
	}
}
