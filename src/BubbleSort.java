import java.util.List;

public class BubbleSort implements SortingAlgorithm {
	@Override
	public void sort(List<Integer> list) {
		int iterations = 0;
		while(true) {
			var dirty = false;
			for(int i = 0; i < (list.size() - 1 - iterations); i++) {
				int firstElement = list.get(i);
				int secondElement = list.get(i + 1);
				int compared = Integer.compare(firstElement, secondElement);
				//yield(CompareTwoPoints(i, i + 1))
				if(compared == 0) continue;
				if(compared > 0) {
					swap(i, i + 1, list);
					//yield(SwapTwoPoints(i, i + 1))
					dirty = true;
				}
			}
			iterations++;
			if(!dirty) break;
		}
	}
}
