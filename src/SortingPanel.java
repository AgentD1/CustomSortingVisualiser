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
	
	List<Integer> listToSort = new ArrayList<>();
	
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		
		if(operationReady.get()) {
			SortOperation op = operation.get();
			
			
			
			
			operationReady.set(false);
			continueSortCondition.signal();
		}
		
		graphics.drawString(String.valueOf(num), 10, 10);
		
		Toolkit.getDefaultToolkit().sync();
	}
	
	
}
