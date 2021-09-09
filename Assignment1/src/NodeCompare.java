// Author: Joyce Tan

import java.util.Comparator;

public class NodeCompare implements Comparator<Node>{

	@Override
	public int compare(Node a, Node b) {
		if(a.getF() > b.getF()) return 1;
		if(a.getF() < b.getF()) return -1;
		else return 0;
	}
}
