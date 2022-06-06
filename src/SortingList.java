import java.util.*;

public class SortingList implements List<Integer>, RandomAccess {
	public List<Integer> underlyingList = new ArrayList<>();
	
	SortingPanel panel;
	
	public SortingList(SortingPanel panel) {
		this.panel = panel;
	}
	
	public void swap(int index1, int index2) {
		int index2item = underlyingList.get(index2);
		underlyingList.set(index2, underlyingList.get(index1));
		underlyingList.set(index1, index2item);
		panel.nextOperationLock.lock();
		try {
			panel.operationReady.set(true);
			panel.operation.set(new SortOperation.SwapPoints(index1, index2));
			
			panel.continueSortCondition.await();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			try {
				panel.nextOperationLock.unlock();
			} catch(IllegalMonitorStateException ignored) {}
		}
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
			try {
				panel.nextOperationLock.unlock();
			} catch(IllegalMonitorStateException ignored) {}
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
			try {
				panel.nextOperationLock.unlock();
			} catch(IllegalMonitorStateException ignored) {}
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
		boolean changed = false;
		for(Integer i : underlyingList) {
			if(!c.contains(i)) {
				remove(i);
				changed = true;
			}
		}
		return changed;
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
			try {
				panel.nextOperationLock.unlock();
			} catch(IllegalMonitorStateException ignored) {}
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
			try {
				panel.nextOperationLock.unlock();
			} catch(IllegalMonitorStateException ignored) {}
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
			try {
				panel.nextOperationLock.unlock();
			} catch(IllegalMonitorStateException ignored) {}
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
		throw new UnsupportedOperationException("List iterators are not currently supported!");
	}
	
	@Override
	public ListIterator<Integer> listIterator(int index) {
		throw new UnsupportedOperationException("List iterators are not currently supported!");
	}
	
	@Override
	public List<Integer> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException("Sublists are not currently supported!");
	}
}
