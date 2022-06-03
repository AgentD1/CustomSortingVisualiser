import javax.swing.*;
import java.sql.Array;
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
	
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		colorList = new ArrayList<>(dataList.stream().map(i -> Color.gray).toList());
		
		if(operationReady.get()) {
			SortOperation op = operation.get();
			
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
		
		Graphics2D g = (Graphics2D) graphics;
		
		double w = getSize().getWidth();
		double h = getSize().getHeight();
		
		double barWidth = w / dataList.size();
		
		g.setStroke(new BasicStroke(2f));
		
		for(int i = 0; i < dataList.size(); i++) {
			if(barWidth > 2) {
				g.setColor(Color.black);
			} else {
				g.setColor(colorList.get(i));
			}
			double height = (h - 1) * ((float)dataList.get(i) / dataList.stream().max(Integer::compareTo).orElseThrow());
			g.drawRect((int) (i * barWidth), (int) (h - height - 1), (int) barWidth, (int) (height - 1));
			g.setColor(colorList.get(i));
			g.fillRect((int) (i * barWidth), (int) (h - height - 1), (int) barWidth, (int) (height - 1));
		}
		
		g.drawString(String.valueOf(dataList.size()), 0, 0);
		
		Toolkit.getDefaultToolkit().sync();
	}
	
	
}
