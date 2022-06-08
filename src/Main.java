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
	
	static SortingList sortingList;
	static Thread sortingThread;
	
	
	public static void main(String[] args) {
		algorithms = new SortingAlgorithm[] {
				new BubbleSort(), // If you want to add a new sorting algorithm to the list, add it here like this: new YourClass(),
		};
		
		dataGenerators = Map.of(
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
		
		JLabel chooseAlgorithmLabel = new JLabel("Choose a sorting algorithm from the list below:");
		rightPane.add(chooseAlgorithmLabel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		chooseAlgorithmBox = new JComboBox<>(Arrays.stream(algorithms).map(a -> a.getClass().getName()).toArray(String[]::new));
		rightPane.add(chooseAlgorithmBox);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JLabel sortControlsLabel = new JLabel("Use the buttons below to control the sorting:");
		rightPane.add(sortControlsLabel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		
		JPanel playButtonsPanel = new JPanel();
		playButtonsPanel.setLayout(new BoxLayout(playButtonsPanel, BoxLayout.X_AXIS));
		
		JButton leftButton = new JButton("⏪");
		playPauseButton = new JButton("⏵");
		playPauseButton.addActionListener(e -> {
			panel.playPause();
		});
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
		dataGeneratorPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		dataGeneratorPanel.add(dataGeneratorSelector);
		rightPane.add(dataGeneratorPanel);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		
		JButton loadCodeButton = new JButton("Load Coad");
		loadCodeButton.addActionListener(e -> {
			CodeLoader.load("""
					import java.util.List;
					
					// Replace TemplateAlgorithm with the name of your algorithm, like BubbleSort (but not BubbleSort because its already taken).
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
					
					""");
		});
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
}
