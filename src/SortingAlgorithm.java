import java.util.List;

public interface SortingAlgorithm {
	void sort(List<Integer> list);
	
	default void swap(int index1, int index2, List<Integer> list) {
		if(list instanceof SortingList l) {
			l.swap(index1, index2);
		} else {
			int index2item = list.get(index2);
			list.set(index2, list.get(index1));
			list.set(index1, index2item);
		}
	}
}
