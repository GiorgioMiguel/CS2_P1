package Assignment1;

public class TwoFourTree {

	private class TwoFourTreeItem {

		int values = 1;
		int value1 = 0; // always exists.
		int value2 = 0; // exists iff the node is a 3 node or 4-node.
		int value3 = 0; // exists iff the node is a 4- node.
		boolean isLeaf = true;

		TwoFourTreeItem parent = null; // parent exists iff the node is not root.
		TwoFourTreeItem leftChild = null; // left and right child exist iff the note is a non-leaf.
		TwoFourTreeItem rightChild = null;
		TwoFourTreeItem centerChild = null; // center child exists iff the node is a non-leaf 3-node.

		// center-left and center-right children exist iff the node is a 4-node
		TwoFourTreeItem centerLeftChild = null;
		TwoFourTreeItem centerRightChild = null;

		// methods
		public boolean isTwoNode() {
			if (this.values == 1) {
				return true;
			} // end if
			return false;
		}// end isTwoNode

		public boolean isThreeNode() {
			if (this.values == 2) {
				return true;
			} // end if
			return false;
		}// end isThreeNode

		public boolean isFourNode() {
			if (this.values == 3) {
				return true;
			} // end if
			return false;
		}// end isFourNode

		public boolean isRoot() {
			if (parent == null) {
				return true;
			} // end if
			return false;
		}// end isRoot

		// constructors
		public TwoFourTreeItem(int value1) {
			this.value1 = value1;
		}// end 2node

		public TwoFourTreeItem(int value1, int value2) {
			this.value1 = value1;
			this.value2 = value2;
			this.values = 2;
		}// end 3node

		public TwoFourTreeItem(int value1, int value2, int value3) {
			this.value1 = value1;
			this.value2 = value2;
			this.value3 = value3;
			this.values = 3;
		}// end 4node

		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! print
		// methods (DO NOT TOUCH)
		private void printIndents(int indent) {
			for (int i = 0; i < indent; i++)
				System.out.printf(" ");
		}

		public void printInOrder(int indent) {
			if (!isLeaf)
				leftChild.printInOrder(indent + 1);
			printIndents(indent);
			System.out.printf("%d\n", value1);
			if (isThreeNode()) {
				if (!isLeaf)
					centerChild.printInOrder(indent + 1);
				printIndents(indent);
				System.out.printf("%d\n", value2);
			}

			else if (isFourNode()) {
				if (!isLeaf)
					centerLeftChild.printInOrder(indent + 1);
				printIndents(indent);
				System.out.printf("%d\n", value2);
				if (!isLeaf)
					centerRightChild.printInOrder(indent + 1);
				printIndents(indent);
				System.out.printf("%d\n", value3);
			}
			if (!isLeaf)
				rightChild.printInOrder(indent + 1);
		}
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}// end TwoFourTreeItem private class

	// global root
	TwoFourTreeItem root = null;

	// global left and right references
	TwoFourTreeItem leftNode = null;
	TwoFourTreeItem rightNode = null;

	// 7 helper methods splitRoot, splitLeafRoot, SplitNode, SplitLeafNode,
	// Evolve2Node, Evolve3Node, EvolveLeafNode

	// This method takes in a leaf-4node and splits it into two leaf-2nodes. It then
	// returns
	// the middle value of the split node.
	// After splitNode is called, global references leftNode and rightNode will be
	// updated to the newly created nodes.
	public int splitLeafNode(TwoFourTreeItem node) {

		// creating leftNode
		leftNode = new TwoFourTreeItem(node.value1);
		leftNode.parent = node.parent;

		// creating RightNode
		rightNode = new TwoFourTreeItem(node.value3);
		rightNode.parent = node.parent;

		// return middle value to be used by evolution methods
		return node.value2;
	}// end split leaf Node

	// This method takes in a 4node and splits it into two 2nodes. It then returns
	// the middle value of the split node.
	// After splitNode is called, global references leftNode and rightNode will be
	// updated to the newly created nodes.
	public int splitNode(TwoFourTreeItem node) {

		// creating leftNode and initializing pointers
		leftNode = new TwoFourTreeItem(node.value1);
		leftNode.isLeaf = false;
		leftNode.parent = node.parent;
		leftNode.leftChild = node.leftChild;
		leftNode.rightChild = node.centerLeftChild;
		
		node.leftChild.parent = leftNode;
		node.centerLeftChild.parent = leftNode;
		
		// creating rightNode and initializing pointers
		rightNode = new TwoFourTreeItem(node.value3);
		rightNode.isLeaf = false;
		rightNode.parent = node.parent;
		rightNode.leftChild = node.centerRightChild;
		rightNode.rightChild = node.rightChild;
		
		node.centerRightChild.parent = rightNode;
		node.rightChild.parent = rightNode;
		
		
		// return middle value to be used by evolution methods
		return node.value2;
	}// end split Node

	public TwoFourTreeItem splitRoot(TwoFourTreeItem oldRoot, int value) {
		TwoFourTreeItem newRoot = new TwoFourTreeItem(oldRoot.value2);
		newRoot.isLeaf = false;
		leftNode = new TwoFourTreeItem(oldRoot.value1);
		leftNode.parent = newRoot;
		leftNode.isLeaf = false;
		rightNode = new TwoFourTreeItem(oldRoot.value3);
		rightNode.parent = newRoot;
		rightNode.isLeaf = false;

		newRoot.leftChild = leftNode;
		newRoot.rightChild = rightNode;

		leftNode.leftChild = root.leftChild;
		leftNode.rightChild = root.centerLeftChild;
		root.leftChild.parent = leftNode;
		root.centerLeftChild.parent = leftNode;
		
		rightNode.leftChild = root.centerRightChild;
		rightNode.rightChild = root.rightChild;
		
		root.centerRightChild.parent = rightNode;
		root.rightChild.parent = rightNode;
	
		
		root = newRoot;

		if (value > newRoot.value1) {
			return rightNode;
		} // traverse right
		else {
			return leftNode;
		} // traverse left
	}// end split non leaf root

	// this method splits the root of a 2-4Tree by creating 3 new 2nodes. 1 for the
	// new root, and 2 and 3 to be leftNode and rightNode respectively.
	public TwoFourTreeItem splitLeafRoot(TwoFourTreeItem oldRoot, int value) {

		// creating a new root, this root is not a leaf node
		TwoFourTreeItem newRoot = new TwoFourTreeItem(oldRoot.value2);
		newRoot.isLeaf = false;

		// creating LeftNode and RightNode reference
		leftNode = new TwoFourTreeItem(oldRoot.value1);
		rightNode = new TwoFourTreeItem(oldRoot.value3);

		// setting up pointers/references
		newRoot.leftChild = leftNode;
		newRoot.rightChild = rightNode;
		leftNode.parent = newRoot;
		rightNode.parent = newRoot;

		// after links have been made we move the reference to the new root to allow GC
		// to clean up old root
		root = newRoot;

		// figure out where current reference should go next
		if (value < newRoot.value1) {
			return leftNode;
		} // traverse left after split
		else {
			return rightNode;
		} // traverse right after split

	}// end split leaf root

	// evolves a three 2node returns the node that cur should go to next in order to
	// keep traversing
	public TwoFourTreeItem evolve2Node(TwoFourTreeItem node, int v2, int insertValue) {

		// evolve a 2node

		// case 1 v2 > node.v1
		if (v2 > node.value1) {

			node.values = 2;
			node.value2 = v2;
			node.centerChild = leftNode;
			node.rightChild = rightNode;

		} // end case 1
			// case 2 v2 < node.v1
		else {

			node.values = 2;
			node.value2 = node.value1;
			node.value1 = v2;

			node.leftChild = leftNode;
			node.centerChild = rightNode;
		} // end case 2

		leftNode.parent = node;
		rightNode.parent = node;

		if (insertValue < v2) {
			return leftNode;
		} // send cur left
		else {
			return rightNode;
		} // send cur right

	}// end evolve 2node

	// evolves a 3node returns the node that cur should go to next in order to keep
	// traversing
	public TwoFourTreeItem evolve3Node(TwoFourTreeItem node, int v2, int insertValue) {

		// evolve a 3node

		// case 1 v2 > node.v2
		if (v2 > node.value2) {

			node.value3 = v2;
			node.values = 3;

			node.centerLeftChild = node.centerChild;
			node.centerRightChild = leftNode;
			node.rightChild = rightNode;

		} // end case 1
		else if (v2 > node.value1 && v2 < node.value2) {

			node.value3 = node.value2;
			node.value2 = v2;
			node.values = 3;

			node.centerLeftChild = leftNode;
			node.centerRightChild = rightNode;

		} // end case 2
		else if (v2 < node.value1) {
			node.value3 = node.value2;
			node.value2 = node.value1;
			node.value1 = v2;
			node.values = 3;

			node.leftChild = leftNode;
			node.centerLeftChild = rightNode;

		} // end case 3

		leftNode.parent = node;
		rightNode.parent = node;

		if (insertValue > v2) {
			return rightNode;
		} // traverse right after split
		else {
			return leftNode;
		} // traverse left after split

	}// end evolve 3node

	public void evolveLeafNode(TwoFourTreeItem node, int item) {

		// figure out what kind of node it is
		if (node.isTwoNode()) {

			node.values = 2;

			// case 1 if item > v1
			if (item > node.value1) {
				node.value2 = item;
			} // end if
				// case 2 if item < v1
			else {
				node.value2 = node.value1;
				node.value1 = item;
			} // end else
		} // end 2node case
		else if (node.isThreeNode()) {

			node.values = 3;

			// case 1 if item > v2
			if (item > node.value2) {
				node.value3 = item;
			} // end case 1
				// case 2 of v1 < item < v2
			else if (item > node.value1 && item < node.value2) {
				node.value3 = node.value2;
				node.value2 = item;
			} // end case 2
			else if (item < node.value1) {
				node.value3 = node.value2;
				node.value2 = node.value1;
				node.value1 = item;

			} // end case 3
		} // end 3node case

	}// end evolve leaf Node

	// CREATE INSERTION METHOD
	public boolean addValue(int value) {

		// null case
		if (root == null) {
			root = new TwoFourTreeItem(value);
			return true;
		} // end null check
			// root is still a leaf
		else if (!root.isFourNode() && root.isLeaf) {
			evolveLeafNode(root, value);
			return true;
		} // end leaf case

		// creating a reference to the head
		TwoFourTreeItem cur = root;

		if (root.isFourNode() && root.isLeaf) {
			cur = splitLeafRoot(root, value);
		} // splitting a leaf node

		// while the reference isn't a leaf node, traverse the 2-3-4tree and "pop" any
		// 4nodes we encounter on the way down towards the proper leaf to insert
		while (!cur.isLeaf) {

			// 2node case
			if (cur.isTwoNode()) {
				if (value < cur.value1) {
					cur = cur.leftChild;
				} // traverse left
				else {
					cur = cur.rightChild;
				} // traverse left
			} // end 2node case

			// 3node case
			else if (cur.isThreeNode()) {
				if (value < cur.value1) {
					cur = cur.leftChild;
				} // traverse left
				else if (value < cur.value2) {
					cur = cur.centerLeftChild;
				} // traverse mid
				else {
					cur = cur.rightChild;
				} // traverse right
			} // end if three node

			// 4node case
			else if (cur.isFourNode()) {

				// node to split is the root
				if (cur.isRoot()) {
					cur = splitRoot(root, value);
				} // end splitting non leaf root
					// if parent of node to split is a 2node
				else if (cur.parent.isTwoNode()) {
					int middleValue = splitNode(cur);
					cur = evolve2Node(cur.parent, middleValue, value);
				} // end 2node case
				else if (cur.parent.isThreeNode()) {
					int middleValue = splitNode(cur);
					cur = evolve3Node(cur.parent, middleValue, value);
				} // end 3node case

			} // end else if

		} // end while, cur should be at a leaf node

		// we are now at a leaf node
		if (!cur.isFourNode()) {
			evolveLeafNode(cur, value);
			return true;
		}//end if
		else {
			if(cur.parent.isTwoNode()) {
				int middleValue = splitLeafNode(cur);
				cur = evolve2Node(cur.parent, middleValue, value);
				evolveLeafNode(cur,value);
			}//end if
			else if(cur.parent.isThreeNode()) {
				int middleValue = splitLeafNode(cur);
				cur = evolve3Node(cur.parent, middleValue, value);
				evolveLeafNode(cur, value);
			}//end else if
		}//end else

		return false;

	}// end insert method

	// CREATE HAS VALUE METHOD
	public boolean hasValue(int value) {

		// creating a reference to move around
		TwoFourTreeItem cur = root;

		// while reference isn't null
		while (cur != null) {

			// 2node case
			if (cur.isTwoNode()) {
				if (value == cur.value1) {
					return true;
				} // end if
				else {
					if (value < cur.value1) {
						cur = cur.leftChild;
					} // traverse left
					else {
						cur = cur.rightChild;
					} // traverse left
				} // end else
			} // end if two node

			// 3node case
			else if (cur.isThreeNode()) {
				if (value == cur.value1 || value == cur.value2) {
					return true;
				} // end if
				else {
					if (value < cur.value1) {
						cur = cur.leftChild;
					} // traverse left
					else if (value < cur.value2) {
						cur = cur.centerLeftChild;
					} // traverse mid
					else {
						cur = cur.rightChild;
					} // traverse right
				} // end else
			} // end if three node

			// 4node case
			else if (cur.isFourNode()) {
				if (value == cur.value1 || value == cur.value2 || value == cur.value3) {
					return true;
				} // end if
				else {
					if (value < cur.value1) {
						cur = cur.leftChild;
					} // traverse left
					else if (value < cur.value2) {
						cur = cur.centerLeftChild;
					} // traverse leftMid
					else if (value < cur.value3) {
						cur = cur.centerRightChild;
					} // traverse rightMid
					else {
						cur = cur.rightChild;
					} // traverse right
				} // end else
			} // end else if
		} // end while

		return false;
	}

	// CREATE DELETE METHOD
	public boolean deleteValue(int value) {

		return false;
	}

	// print method (DONT TOUCH)
	public void printInOrder() {

		if (root != null)
			root.printInOrder(0);
	}

	// constructor
	public TwoFourTree() {
		// default constructor is disabled
	}

}// end class
