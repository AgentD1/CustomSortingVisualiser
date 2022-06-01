import java.util.*;
import java.util.concurrent.locks.Condition;

public class SortingList<E> implements List<E> {
	List<E> underlyingList = new ArrayList<>();
	
	Condition continueSortCondition;
	
	public SortingList(Condition condition) {
		continueSortCondition = condition;
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
	public Iterator<E> iterator() {
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
	public boolean add(E e) {
		// TODO: add right thing
		try {
			continueSortCondition.await();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		underlyingList.add(e);
		return true;
	}
	
	@Override
	public boolean remove(Object o) {
		// TODO: add the thing
		return underlyingList.remove(o);
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		return underlyingList.containsAll(c);
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		return underlyingList.addAll(c);
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return underlyingList.addAll(index, c);
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		return underlyingList.removeAll(c);
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		return underlyingList.retainAll(c);
	}
	
	@Override
	public void clear() {
		// TODO: add the thing
		underlyingList.clear();
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
	public E get(int index) {
		return underlyingList.get(index);
	}
	
	@Override
	public E set(int index, E element) {
		// TODO: Add the thing
		return underlyingList.set(index, element);
	}
	
	@Override
	public void add(int index, E element) {
		// TODO: Add the thing
		underlyingList.add(index, element);
	}
	
	@Override
	public E remove(int index) {
		// TODO: Add the thing
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
	public ListIterator<E> listIterator() {
		return underlyingList.listIterator();
	}
	
	@Override
	public ListIterator<E> listIterator(int index) {
		return underlyingList.listIterator(index);
	}
	
	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return underlyingList.subList(fromIndex, toIndex);
	}
}
