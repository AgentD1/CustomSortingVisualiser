import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.*;
import java.util.concurrent.locks.*;

public class SortingPanel extends JPanel {
	int num = 0;
	
	Lock nextOperationLock = new ReentrantLock();
	Condition continueSortCondition = nextOperationLock.newCondition();
	
	AtomicBoolean operationReady = new AtomicBoolean(false);
	AtomicReference<SortOperation> operation = new AtomicReference<>();
	
	List<Integer> dataList = new ArrayList<>();
	List<Color> colorList = new ArrayList<>();
	
	public boolean paused = true;
	
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		if(!paused) {
			colorList = new ArrayList<>(dataList.stream().map(i -> Color.gray).toList());
		}
		
		if (!paused && operationReady.get()) {
			SortOperation op = operation.get();
			
			if(op != null) {
				switch (op) {
					case SortOperation.AddPoint o -> {
						dataList.add(o.addIndex(), o.numToAdd());
						colorList.add(o.addIndex(), Color.green);
					}
					case SortOperation.RemovePoint o -> {
						dataList.remove(o.removeIndex());
						colorList.remove(o.removeIndex());
					}
					case SortOperation.DirectSetPoint o -> {
						dataList.set(o.index(), o.value());
						colorList.set(o.index(), Color.red);
					}
					case SortOperation.SwapPoints o -> {
						int index2item = dataList.get(o.index2());
						dataList.set(o.index2(), dataList.get(o.index1()));
						dataList.set(o.index1(), index2item);
						colorList.set(o.index1(), Color.green);
						colorList.set(o.index2(), Color.green);
					}
					default -> throw new IllegalStateException("Unexpected value: " + op);
				}
				
				num++;
				operationReady.set(false);
				nextOperationLock.lock();
				try {
					continueSortCondition.signalAll();
				} finally {
					nextOperationLock.unlock();
				}
			}
		}
		
		if (dataList.size() == 0) return;
		
		Graphics2D g = (Graphics2D) graphics;
		
		double w = getSize().getWidth();
		double h = getSize().getHeight();
		
		double barWidth = (w - 2) / dataList.size();
		
		g.setStroke(new BasicStroke(2f));
		
		double maxHeight = dataList.stream().max(Integer::compareTo).orElseThrow() + 1;
		
		for (int i = 0; i < dataList.size(); i++) {
			if (barWidth > 2) {
				g.setColor(Color.black);
			} else {
				g.setColor(colorList.get(i));
			}
			double height = (h - 1) * ((float) dataList.get(i) / maxHeight);
			g.drawRect((int) (i * barWidth) + 1, (int) (h - height - 1), (int) barWidth, (int) (height - 1));
			g.setColor(colorList.get(i));
			g.fillRect((int) (i * barWidth) + 1, (int) (h - height - 1), (int) barWidth, (int) (height - 1));
		}
		
		g.drawString(String.valueOf(dataList.size()), 0, 0);
		
		Toolkit.getDefaultToolkit().sync();
	}
	
	public void reset() {
		Main.sortingThread.stop();
		
		dataList.clear();
		Main.DataGenerator generator = Main.dataGenerators.get((String) Main.dataGeneratorSelector.getSelectedItem());
		dataList.addAll(generator.data((Integer) Main.dataPointsNumberField.getValue()));
		
		operation.set(null);
		nextOperationLock = new ReentrantLock();
		continueSortCondition = nextOperationLock.newCondition();
		Main.sortingList = new SortingList(Main.panel);
		Main.sortingList.underlyingList.addAll(dataList);
		
		SortingAlgorithm algorithm = Arrays.stream(Main.algorithms).filter(
				x -> x.getClass().getName().equals(Main.chooseAlgorithmBox.getSelectedItem())
		).findFirst().orElseThrow();
		
		Main.sortingThread = new Thread(() -> algorithm.sort(Main.sortingList));
		
		Main.sortingThread.start();
	}
	
	public void playPause() {
		if(paused) {
			play();
		} else {
			pause();
		}
	}
	
	public void play() {
		Main.playPauseButton.setText("⏸");
		paused = false;
	}
	
	public void pause() {
		Main.playPauseButton.setText("⏵");
		paused = true;
	}
}
