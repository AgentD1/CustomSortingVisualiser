import java.util.*;

public class SortingList implements List<Integer> {
	public List<Integer> underlyingList = new ArrayList<>();
	
	SortingPanel panel;
	
	public SortingList(SortingPanel panel) {
		this.panel = panel;
	}
	
	@Override
	public int size() {
		return underlyingList.size();
	}
	
	@Override
	public boolean isEmpty() {
		return underlyingList.isEmpty();
	}
	
	@Override
	public boolean contains(Object o) {
		return underlyingList.contains(o);
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return underlyingList.iterator();
	}
	
	@Override
	public Object[] toArray() {
		return underlyingList.toArray();
	}
	
	@Override
	public <T> T[] toArray(T[] a) {
		// SUS (among us) lol
		//noinspection SuspiciousToArrayCall
		return underlyingList.toArray(a);
	}
	
	@Override
	public boolean add(Integer e) {
		panel.nextOperationLock.lock();
		try {
			panel.operationReady.set(true);
			panel.operation.set(new SortOperation.AddPoint(underlyingList.size(), (int)e));
			
			panel.continueSortCondition.await();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			panel.nextOperationLock.unlock();
		}
		
		underlyingList.add(e);
		return true;
	}
	
	@Override
	public boolean remove(Object o) {
		int index = underlyingList.indexOf(o);
		if(index == -1) return false;
		
		panel.nextOperationLock.lock();
		try {
			panel.operationReady.set(true);
			panel.operation.set(new SortOperation.RemovePoint(index));
			
			panel.continueSortCondition.await();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			panel.nextOperationLock.unlock();
		}
		
		return underlyingList.remove(o);
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		return underlyingList.containsAll(c);
	}
	
	@Override
	public boolean addAll(Collection<? extends Integer> c) {
		for(int e : c) {
			add(e);
		}
		return true;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends Integer> c) {
		for(int e : c) {
			add(index, e);
			index++; // TODO: make all these functions compliant
		}
		return true;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for(Object e : c) {
			if(remove(e)) {
				changed = true;
			}
		}
		return changed;
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		for(Object e : c) {
			remove(e);
		}
		return underlyingList.retainAll(c);
	}
	
	@Override
	public void clear() {
		int size = size();
		for(int i = 0; i < size; i++) {
			remove(0);
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if(!o.getClass().equals(getClass())) return false;
		return underlyingList.equals(o);
	}
	
	@Override
	public int hashCode() {
		return underlyingList.hashCode();
	}
	
	@Override
	public Integer get(int index) {
		return underlyingList.get(index);
	}
	
	@Override
	public Integer set(int index, Integer element) {
		
		panel.nextOperationLock.lock();
		try {
			panel.operationReady.set(true);
			panel.operation.set(new SortOperation.DirectSetPoint(index, element));
			
			panel.continueSortCondition.await();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			panel.nextOperationLock.unlock();
		}
		
		return underlyingList.set(index, element);
	}
	
	@Override
	public void add(int index, Integer element) {
		panel.nextOperationLock.lock();
		try {
			panel.operationReady.set(true);
			panel.operation.set(new SortOperation.AddPoint(index, element));
			
			panel.continueSortCondition.await();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			panel.nextOperationLock.unlock();
		}
		
		underlyingList.add(index, element);
	}
	
	@Override
	public Integer remove(int index) {
		panel.nextOperationLock.lock();
		try {
			panel.operationReady.set(true);
			panel.operation.set(new SortOperation.RemovePoint(index));
			
			panel.continueSortCondition.await();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			panel.nextOperationLock.unlock();
		}
		
		return underlyingList.remove(index);
	}
	
	@Override
	public int indexOf(Object o) {
		return underlyingList.indexOf(o);
	}
	
	@Override
	public int lastIndexOf(Object o) {
		return underlyingList.lastIndexOf(o);
	}
	
	@Override
	public ListIterator<Integer> listIterator() {
		return underlyingList.listIterator();
	}
	
	@Override
	public ListIterator<Integer> listIterator(int index) {
		return underlyingList.listIterator(index);
	}
	
	@Override
	public List<Integer> subList(int fromIndex, int toIndex) {
		return underlyingList.subList(fromIndex, toIndex);
	}
}
