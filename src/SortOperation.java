public interface SortOperation {
	record AddPoint(int addIndex, int numToAdd) implements SortOperation {}
	record RemovePoint(int removeIndex) implements SortOperation {}
	record DirectSetPoint(int index, int value) implements SortOperation {}
	record SwapPoints(int index1, int index2) implements SortOperation {}
}

