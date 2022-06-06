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
