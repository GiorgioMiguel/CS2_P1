package Assignment1;

public class TwoFourTree {
	private class TwoFourTreeItem {
		int values = 1;
		int value1 = 0; // always exists.
		int value2 = 0; // exists iff the node is a 3-node or 4-node.
		int value3 = 0; // exists iff the node is a 4-node.
		boolean isLeaf = true;

		TwoFourTreeItem parent = null; // parent exists iff the node is not root.
		TwoFourTreeItem leftChild = null; // left and right child exist iff the note is a non-leaf.
		TwoFourTreeItem rightChild = null;
		TwoFourTreeItem centerChild = null; // center child exists iff the node is a non-leaf 3-node.
		TwoFourTreeItem centerLeftChild = null; // center-left and center-right children exist iff the node is a
												// non-leaf 4-node.
		TwoFourTreeItem centerRightChild = null;

		public boolean isTwoNode() {

			if (this.values == 1) {
				return true;
			} // end if
			return false;
		}

		public boolean isThreeNode() {
			if (this.values == 2) {
				return true;
			} // end if
			return false;
		}

		public boolean isFourNode() {
			if (this.values == 3) {
				return true;
			} // end if
			return false;
		}

		public boolean isRoot() {
			if (parent == null) {
				return true;
			} // end if
			return false;
		}

		public TwoFourTreeItem(int value1) {
			this.value1 = value1;
		}

		public TwoFourTreeItem(int value1, int value2) {
			this.value1 = value1;
			this.value2 = value2;
			this.values = 2;
		}

		public TwoFourTreeItem(int value1, int value2, int value3) {
			this.value1 = value1;
			this.value2 = value2;
			this.value3 = value3;
			this.values = 3;
		}

		private void printIndents(int indent) {
			for (int i = 0; i < indent; i++)
				System.out.printf("  ");
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
			} else if (isFourNode()) {
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
	}// end item class

	TwoFourTreeItem root = null;

	// global left and right references
	TwoFourTreeItem leftNode = null;
	TwoFourTreeItem rightNode = null;

	// global delete reference
	TwoFourTreeItem delNode = null;

	// 7 helper methods for addValue(): splitRoot(), splitLeafRoot(), SplitNode(),
	// SplitLeafNode(), Evolve2Node(), Evolve3Node(), EvolveLeafNode()

	// takes in a 4node-leaf as an input parameter, splits the leaf-node into two
	// 2node-leaves and returns the middle value of the split 4node
	public int splitLeafNode(TwoFourTreeItem node) {

		// creating leftNode with v1
		leftNode = new TwoFourTreeItem(node.value1);
		leftNode.parent = node.parent;

		// creating RightNode with v3
		rightNode = new TwoFourTreeItem(node.value3);
		rightNode.parent = node.parent;

		// return middle value (v2) to be used by evolution methods
		return node.value2;
	}// end split leaf Node

	// takes in a 4node as an input parameter, splits the 4node into two 2nodes and
	// returns the middle value of the split 4node
	public int splitNode(TwoFourTreeItem node) {

		// creating leftNode and initializing pointers
		leftNode = new TwoFourTreeItem(node.value1);
		leftNode.isLeaf = false;
		leftNode.parent = node.parent;
		leftNode.leftChild = node.leftChild;
		leftNode.rightChild = node.centerLeftChild;

		// updating the children's parent
		node.leftChild.parent = leftNode;
		node.centerLeftChild.parent = leftNode;

		// creating rightNode and initializing pointers
		rightNode = new TwoFourTreeItem(node.value3);
		rightNode.isLeaf = false;
		rightNode.parent = node.parent;
		rightNode.leftChild = node.centerRightChild;
		rightNode.rightChild = node.rightChild;

		// updating the children's parent
		node.centerRightChild.parent = rightNode;
		node.rightChild.parent = rightNode;

		// return middle value to be used by evolution methods
		return node.value2;
	}// end split Node

	// takes in a 4node-root as an input parameter, splits the root into two 2nodes
	// and returns the middle value of the split root
	public TwoFourTreeItem splitRoot(TwoFourTreeItem oldRoot, int value) {

		// creating a new root with v2
		TwoFourTreeItem newRoot = new TwoFourTreeItem(oldRoot.value2);
		newRoot.isLeaf = false;

		// creating a leftNode for the split
		leftNode = new TwoFourTreeItem(oldRoot.value1);
		leftNode.parent = newRoot;
		leftNode.isLeaf = false;
		// creating a rightNode for the split
		rightNode = new TwoFourTreeItem(oldRoot.value3);
		rightNode.parent = newRoot;
		rightNode.isLeaf = false;

		// updating new root's children
		newRoot.leftChild = leftNode;
		newRoot.rightChild = rightNode;

		// updating leftNodes children and updating their children's parent to leftNode
		leftNode.leftChild = root.leftChild;
		leftNode.rightChild = root.centerLeftChild;
		root.leftChild.parent = leftNode;
		root.centerLeftChild.parent = leftNode;

		// updating rightNodes children and updating their children's parent to
		// rightNode
		rightNode.leftChild = root.centerRightChild;
		rightNode.rightChild = root.rightChild;
		root.centerRightChild.parent = rightNode;
		root.rightChild.parent = rightNode;

		root = newRoot;

		// returning the correct node to traverse to
		if (value > newRoot.value1) {
			return rightNode;
		} // traverse right
		else {
			return leftNode;
		} // traverse left
	}// end split non leaf root

	// used only by a leaf-root. Sort of redundant, may change into an if statement
	// in the above method.
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

		// figure out where cur reference should go next
		if (value < newRoot.value1) {
			return leftNode;
		} // traverse left after split
		else {
			return rightNode;
		} // traverse right after split
	}// end split leaf root

	// evolves a 2node into a 3node, returns the node that cur should traverse to
	// next.
	public TwoFourTreeItem evolve2Node(TwoFourTreeItem node, int v2, int insertValue) {

		node.values = 2; // Node now has two values

		if (v2 > node.value1) {
			node.value2 = v2;
			// Child pointers
			// node.leftChild remains the same (values < value1)
			node.centerChild = leftNode; // From splitting child
			node.rightChild = rightNode; // From splitting child
		} else {
			node.value2 = node.value1;
			node.value1 = v2;
			node.leftChild = leftNode; // From splitting child
			node.centerChild = rightNode; // From splitting child
			// node.rightChild remains the same (values > value2)
		}

		// Update parent pointers
		leftNode.parent = node;
		rightNode.parent = node;

		// Decide where to traverse next
		if (insertValue < v2) {
			return leftNode;
		} else {
			return rightNode;
		}
	}

	// evolves a 3node returns the node that cur should go to next in order to keep
	// traversing
	public TwoFourTreeItem evolve3Node(TwoFourTreeItem node, int v2, int insertValue) {

		// case 1: v2 > node.v2
		if (v2 > node.value2) {
			node.value3 = v2;
			node.values = 3;
			node.centerLeftChild = node.centerChild;
			node.centerRightChild = leftNode;
			node.rightChild = rightNode;
		} // end case 1

		// case 2: v2 < node.v2, but v2 > node.v1
		else if (v2 > node.value1 && v2 < node.value2) {
			node.value3 = node.value2;
			node.value2 = v2;
			node.values = 3;
			node.centerLeftChild = leftNode;
			node.centerRightChild = rightNode;
		} // end case 2

		// case 3: v2 < node.v1
		else if (v2 < node.value1) {
			node.value3 = node.value2;
			node.value2 = node.value1;
			node.value1 = v2;
			node.values = 3;
			node.leftChild = leftNode;
			node.centerLeftChild = rightNode;
			node.centerRightChild = node.centerChild;
		} // end case 3

		// updating newly created 2node's parent to the node that is currently evolving
		leftNode.parent = node;
		rightNode.parent = node;

		node.centerChild = null;

		// figure out where cur reference should go next
		if (insertValue > v2) {
			return rightNode;
		} // traverse right after split
		else {
			return leftNode;
		} // traverse left after split

	}// end evolve 3node

	// simple method to evolve a leaf node.
	public void evolveLeafNode(TwoFourTreeItem node, int item) {

		// 2node case
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

		// 3node case
		else if (node.isThreeNode()) {

			node.values = 3;

			// case 1
			if (item > node.value2) {
				node.value3 = item;
			} // end case 1

			// case 2
			else if (item > node.value1 && item < node.value2) {
				node.value3 = node.value2;
				node.value2 = item;
			} // end case 2

			// case 3
			else if (item < node.value1) {
				node.value3 = node.value2;
				node.value2 = node.value1;
				node.value1 = item;
			} // end case 3

		} // end 3node case
	}// end evolve leaf Node

	// CREATE INSERTION METHOD
	public boolean addValue(int value) {

		// null case, create a new node to begin the tree
		if (root == null) {
			root = new TwoFourTreeItem(value);
			return true;
		} // end null check

		// root is still a leaf and can take on more values
		else if (!root.isFourNode() && root.isLeaf) {
			evolveLeafNode(root, value);
			return true;
		} // end leaf case

		// root has some children, creating a reference to the head
		TwoFourTreeItem cur = root;

		// root is the first 4node to pop and its a leaf node
		if (root.isFourNode() && root.isLeaf) {
			cur = splitLeafRoot(root, value); // cur is either at left or right node after the split
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

			} // end if 4node

		} // end while, cur should be at a leaf node

		// we are now at a leaf node
		if (!cur.isFourNode()) {
			evolveLeafNode(cur, value);
			return true;
		} // end if
		else {
			if (cur.parent.isTwoNode()) {
				int middleValue = splitLeafNode(cur);
				cur = evolve2Node(cur.parent, middleValue, value);
				evolveLeafNode(cur, value);
			} // end if
			else if (cur.parent.isThreeNode()) {
				int middleValue = splitLeafNode(cur);
				cur = evolve3Node(cur.parent, middleValue, value);
				evolveLeafNode(cur, value);
			} // end else if
		} // end else

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
					delNode = cur;
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
					delNode = cur;
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
					delNode = cur;
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

		delNode = null;
		return false;
	} // end has value

	// deletion helper methods----------------

	// This method fuses 3 2nodes into a singular 4node root, and returns that new
	// root.
	TwoFourTreeItem mergeRoot(TwoFourTreeItem oldRoot) {

		TwoFourTreeItem newRoot = new TwoFourTreeItem(oldRoot.leftChild.value1, oldRoot.value1,
				oldRoot.rightChild.value1);

		if (!oldRoot.leftChild.isLeaf) {
			newRoot.isLeaf = false;
			oldRoot.leftChild.leftChild.parent = newRoot;
			oldRoot.leftChild.rightChild.parent = newRoot;
			oldRoot.rightChild.leftChild.parent = newRoot;
			oldRoot.rightChild.rightChild.parent = newRoot;
		} // end if

		newRoot.leftChild = oldRoot.leftChild.leftChild;
		newRoot.centerLeftChild = oldRoot.leftChild.rightChild;
		newRoot.centerRightChild = oldRoot.rightChild.leftChild;
		newRoot.rightChild = oldRoot.rightChild.rightChild;

		return newRoot;

	}// end fuse root case

	// this method devolves a 3node and returns the value it is losing. Parameter
	// value is v1 of the current node(cur).
	// Parameter node is the node to be devolved.
	// Using the value of v1 at cur we can know what kind of devolution it is.
	public int devolve3node(TwoFourTreeItem node, int value) {

		node.values -= 1;
		int temp = 0;

		// if we are losing v1
		if (value < node.value1) {
			temp = node.value1;
			node.value1 = node.value2;
			node.value2 = 0;
			node.leftChild = node.centerChild;
			node.centerChild = null;
			return temp;
		} // end v1 case
			// we are losing v2
		else {
			temp = node.value2;
			node.value2 = 0;
			node.leftChild = node.centerChild;
			node.centerChild = null;
			return temp;
		} // end v2 case

	}// end devolve 3node

	// two assumptions, this isn't a 2node and it has value to delete
	public void devolveLeafNode(TwoFourTreeItem node, int value) {
		if (node.isThreeNode()) {
			if (node.value1 == value) {
				node.value1 = node.value2;
				node.value2 = 0;
				node.values = 1;

			} // end v1 case
			else {
				node.value2 = 0;
				node.values = 1;
			} // end v2 case
		} // end 3node case
		else {
			if (node.value1 == value) {
				node.value1 = node.value2;
				node.value2 = node.value3;
				node.value3 = 0;
				node.values = 2;
			} // end v1 case
			else if (root.value2 == value) {
				node.value2 = node.value3;
				node.value3 = 0;
				node.values = 2;
			} // end v2 case
			else {
				node.value3 = 0;
				node.values = 2;
			} // end v3 case
		} // end 4node case
	}// end devolve leaf node

	public TwoFourTreeItem devolveLeafRoot(TwoFourTreeItem root, int value) {

		// there is only 1 value left to delete, we will then remove all references from
		// the old root and set the new root to null
		if (root.isTwoNode()) {
			delNode = null;
			return null;
		} // end 2node case

		// else we check if it is 3node
		else if (root.isThreeNode()) {
			if (root.value1 == value) {
				root.value1 = root.value2;
				root.value2 = 0;
				root.values = 1;
				return root;
			} // end v1 case
			else {
				root.value2 = 0;
				root.values = 1;
				return root;
			} // end v2 case
		} // end 3node case

		// else it's a 4node
		else {
			if (root.value1 == value) {
				root.value1 = root.value2;
				root.value2 = root.value3;
				root.value3 = 0;
				root.values = 2;
				return root;
			} // end v1 case
			else if (root.value2 == value) {
				root.value2 = root.value3;
				root.value3 = 0;
				root.values = 2;
				return root;
			} // end v2 case
			else {
				root.value3 = 0;
				root.values = 2;
				return root;
			} // end v3 case
		} // end 4node case
	}// end leaf root devolution

	// all merge cases

	// this method merges a 2node with a 3node parent
	public void merge2NodeFrom3Node(TwoFourTreeItem node) {

		if (node == node.parent.leftChild || node == node.parent.centerChild) {
			// leaf case no parent pointers need to be reassigned for children of node
			if (node.isLeaf) {

				node.values = 3;
				node.value2 = node.parent.value1;
				node.value3 = node.parent.centerChild.value1;
				node.centerLeftChild = node.rightChild;
				node.centerRightChild = node.parent.centerChild.leftChild;
				node.rightChild = node.parent.centerChild.rightChild;
				node.parent.value1 = node.parent.value2;
				node.parent.value2 = 0;
				node.parent.values = 1;
				node.parent.centerChild = null;
			} // end leaf case
			else {

				node.values = 3;
				node.value2 = node.parent.value1;
				node.value3 = node.parent.centerChild.value1;
				node.centerLeftChild = node.rightChild;
				node.centerRightChild = node.parent.centerChild.leftChild;
				node.rightChild = node.parent.centerChild.rightChild;
				node.parent.centerChild.leftChild.parent = node;
				node.parent.centerChild.rightChild.parent = node;
				node.parent.value1 = node.parent.value2;
				node.parent.value2 = 0;
				node.parent.values = 1;
				node.parent.centerChild = null;
			} // end non leaf case
		} // end left/center merge case
		else {

			if (node.isLeaf) {

				node.values = 3;
				node.value3 = node.value1;
				node.value2 = node.parent.value2;
				node.value1 = node.parent.centerChild.value1;
				node.leftChild = node.parent.centerChild.leftChild;
				node.centerLeftChild = node.parent.centerChild.rightChild;
				node.centerRightChild = node.leftChild;
				node.parent.value2 = 0;
				node.parent.values = 1;
				node.parent.centerChild = null;
			} // end leaf case
			else {

				node.values = 3;
				node.value3 = node.value1;
				node.value2 = node.parent.value2;
				node.value1 = node.parent.centerChild.value1;
				node.leftChild = node.parent.centerChild.leftChild;
				node.centerLeftChild = node.parent.centerChild.rightChild;
				node.parent.centerChild.leftChild.parent = node;
				node.parent.centerChild.rightChild.parent = node;
				node.centerRightChild = node.leftChild;
				node.parent.value2 = 0;
				node.parent.values = 1;
				node.parent.centerChild = null;
			} // end non leaf case
		} // end right merge case
	}// end merge 3node

	// this method merges a 2node with a 4node parent
	public void merge2NodeFrom4Node(TwoFourTreeItem node) {

		if (node == node.parent.leftChild) {

			if (node.isLeaf) {

				node.values = 3;
				node.value2 = node.parent.value1;
				node.value3 = node.parent.centerLeftChild.value1;
				node.centerLeftChild = node.rightChild;
				node.centerRightChild = node.parent.centerLeftChild.leftChild;
				node.rightChild = node.parent.centerLeftChild.rightChild;
				node.parent.value1 = node.parent.value2;
				node.parent.value2 = node.parent.value3;
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node.parent.centerRightChild;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;
			} // end leaf case
			else {

				node.values = 3;
				node.value2 = node.parent.value1;
				node.value3 = node.parent.centerLeftChild.value1;
				node.centerLeftChild = node.rightChild;
				node.centerRightChild = node.parent.centerLeftChild.leftChild;
				node.rightChild = node.parent.centerLeftChild.rightChild;
				node.parent.centerLeftChild.leftChild.parent = node;
				node.parent.centerLeftChild.rightChild.parent = node;
				node.parent.value1 = node.parent.value2;
				node.parent.value2 = node.parent.value3;
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node.parent.centerRightChild;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;
			} // end non leaf case
		} // end left merge case
		else if (node == node.parent.centerLeftChild || node == node.parent.centerRightChild) {

			if (node.isLeaf) {

				node.values = 3;
				node.value2 = node.parent.value2;
				node.value3 = node.parent.centerRightChild.value1;
				node.centerLeftChild = node.rightChild;
				node.centerRightChild = node.parent.centerRightChild.leftChild;
				node.rightChild = node.parent.centerRightChild.rightChild;
				node.parent.value2 = node.parent.value3;
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;
			} // end leaf case
			else {

				node.values = 3;
				node.value2 = node.parent.value2;
				node.value3 = node.parent.centerRightChild.value1;
				node.centerLeftChild = node.rightChild;
				node.centerRightChild = node.parent.centerRightChild.leftChild;
				node.rightChild = node.parent.centerRightChild.rightChild;
				node.parent.centerRightChild.leftChild.parent = node;
				node.parent.centerRightChild.rightChild.parent = node;
				node.parent.value2 = node.parent.value3;
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;
			} // end non leaf case
		} // end center left/right merge cases
		else {

			if (node.isLeaf) {

				node.values = 3;
				node.value3 = node.value1;
				node.value2 = node.parent.value3;
				node.value1 = node.parent.centerRightChild.value1;
				node.centerRightChild = node.leftChild;
				node.centerLeftChild = node.parent.centerRightChild.rightChild;
				node.leftChild = node.parent.centerRightChild.leftChild;
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node.parent.centerLeftChild;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;
			} // end leaf case
			else {

				node.values = 3;
				node.value3 = node.value1;
				node.value2 = node.parent.value3;
				node.value1 = node.parent.centerRightChild.value1;
				node.centerRightChild = node.leftChild;
				node.centerLeftChild = node.parent.centerRightChild.rightChild;
				node.leftChild = node.parent.centerRightChild.leftChild;
				node.parent.centerRightChild.rightChild.parent = node;
				node.parent.centerRightChild.leftChild.parent = node;
				node.parent.value3 = 0;
				node.parent.values = 2;
				node.parent.centerChild = node.parent.centerLeftChild;
				node.parent.centerLeftChild = null;
				node.parent.centerRightChild = null;
			} // end non leaf case
		} // end right merge case
	}// 4node merge case

	// all rotation cases

	// this method is responsible for the rotation case when the parent is a two
	// node and the sibling is a 3node
	public void rotate2Node3Node(TwoFourTreeItem node) {

		// we are in a left rotate case
		if (node == node.parent.leftChild) {

			// first the node current is at will grow from a 2node to a 3node
			node.values = 2;
			node.value2 = node.parent.value1;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.rightChild.leftChild;

			// now change the value at the parent
			node.parent.value1 = node.parent.rightChild.value1;

			// now devolve the sibling into a 2node
			node.parent.rightChild.value1 = node.parent.rightChild.value2;
			node.parent.rightChild.value2 = 0;
			node.parent.rightChild.values = 1;
			node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
			node.parent.centerChild = null;

			// if we arent a leaf we will need to updatechildren's parent
			if (!node.isLeaf) {
				node.parent.rightChild.leftChild.parent = node;
			} // end parent update case

		} // end case left
			// we are at a right rotate case
		else {

			// first the node current is at will grow from a 2node to a 3node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value1;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.leftChild.rightChild;

			// now change the value at the parent
			node.parent.value1 = node.parent.leftChild.value2;

			// now devolve the sibling into a 2node
			node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
			node.parent.leftChild.value2 = 0;
			node.parent.leftChild.values = 1;
			node.parent.centerChild = null;

			// if we arent a leaf we will need to updatechildren's parent
			if (!node.isLeaf) {
				node.parent.leftChild.rightChild.parent = node;
			} // end parent update case

		} // end case right
	}// end rotate 2node from 3Node

	// this method rotates a value in to a 2node from a 4node sibling, when the
	// parent is a 2node
	public void rotate2Node4Node(TwoFourTreeItem node) {

		// rotating to the left
		if (node == node.parent.leftChild) {

			// evolve node
			node.values = 2;
			node.value2 = node.parent.value1;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.leftChild.leftChild;

			// edit parent value
			node.parent.value1 = node.parent.rightChild.value1;

			// devolve sibling
			node.parent.rightChild.values = 2;
			node.parent.rightChild.value1 = node.parent.rightChild.value2;
			node.parent.rightChild.value2 = node.parent.rightChild.value3;
			node.parent.rightChild.value3 = 0;
			node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
			node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
			node.parent.rightChild.centerLeftChild = null;
			node.parent.rightChild.centerRightChild = null;

			// node isnt a leaf, thus parents need to be updated.
			if (!node.isLeaf) {
				node.parent.leftChild.leftChild.parent = node;
			} // end leaf case

		} // end rotate left
			// rotating from the right
		else {

			// evolve node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value1;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.leftChild.rightChild;

			// update parent value
			node.parent.value1 = node.parent.leftChild.value3;

			// devolve sibling
			node.parent.leftChild.values = 2;
			node.parent.leftChild.value3 = 0;
			node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
			node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
			node.parent.leftChild.centerLeftChild = null;
			node.parent.leftChild.centerRightChild = null;

			// node isnt a leaf, thus parents need to be updated.
			if (!node.isLeaf) {
				node.parent.leftChild.rightChild.parent = node;
			} // end leaf case
		} // end rotate right
	}// end rotate 2node from 4node

	// this method rotates a value from a 3node to a 2node with a 3node parent
	public void rotate3Node3node(TwoFourTreeItem node) {

		// left rotate case
		if (node == node.parent.leftChild) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.parent.value1;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.centerChild.leftChild;

			// update parent value
			node.parent.value1 = node.parent.centerChild.value1;

			// devolve sibling
			node.parent.centerChild.values = 1;
			node.parent.centerChild.value1 = node.centerChild.value2;
			node.parent.centerChild.value2 = 0;
			node.parent.leftChild = node.parent.centerChild;
			node.parent.centerChild = null;

			// update children's parent
			if (!node.isLeaf) {
				node.parent.centerChild.leftChild.parent = node;
			} // end parent update

		} // end left case
		else if (node == node.parent.centerChild && node.parent.leftChild.isThreeNode()) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value1;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.leftChild.rightChild;

			// update parent value
			node.parent.value1 = node.parent.leftChild.value2;

			// devolve sibling
			node.parent.leftChild.values = 1;
			node.parent.leftChild.value2 = 0;
			node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
			node.parent.leftChild.centerChild = null;

			// update children's parent
			if (!node.isLeaf) {
				node.parent.leftChild.rightChild.parent = node;
			} // end parent update

		} // end centerleft case
		else if (node == node.parent.centerChild && node.parent.rightChild.isThreeNode()) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.parent.value2;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.rightChild.leftChild;

			// update parent value
			node.parent.value2 = node.parent.rightChild.value1;

			// devolve sibling
			node.parent.rightChild.values = 1;
			node.parent.rightChild.value1 = node.parent.rightChild.value2;
			node.parent.rightChild.value2 = 0;
			node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
			node.parent.rightChild.centerChild = null;

			// update children's parent
			if (!node.isLeaf) {
				node.parent.rightChild.leftChild.parent = node;
			} // end parent update

		} // end centerRight case
		else if (node == node.parent.rightChild) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value2;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.centerChild.rightChild;

			// update parent value
			node.parent.value2 = node.parent.centerChild.value2;

			// devolve sibling
			node.parent.centerChild.values = 1;
			node.parent.centerChild.value2 = 0;
			node.parent.centerChild.rightChild = node.parent.centerChild.centerChild;
			node.parent.centerChild.centerChild = null;

			// update children's parent
			if (!node.isLeaf) {
				node.parent.centerChild.rightChild.parent = node;
			} // end parent update

		} // end right case
	}// end rotate3Node3Node

	public void rotate3node4node(TwoFourTreeItem node) {

		// left rotate case
		if (node == node.parent.leftChild) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.parent.value1;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.centerChild.leftChild;

			// update parent value
			node.parent.value1 = node.parent.centerChild.value1;
			
			// devolve sibling
			node.parent.centerChild.values = 2;
			node.parent.centerChild.value1 = node.parent.centerChild.value2;
			node.parent.centerChild.value2 = node.parent.centerChild.value3;
			node.parent.centerChild.centerChild = node.parent.centerChild.centerRightChild;
			node.parent.centerChild.leftChild = node.parent.centerChild.centerLeftChild;
			node.parent.centerChild.centerLeftChild = null;
			node.parent.centerChild.centerRightChild = null;
			
			// update children's parent
			if (!node.isLeaf) {
				node.parent.centerChild.leftChild.parent = node;
			} // end parent update

		} // end left case
		else if (node == node.parent.centerChild && node.parent.leftChild.isFourNode()) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value1;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.leftChild.rightChild;
			
			// update parent value
			node.parent.value1 = node.parent.leftChild.value3;
			
			// devolve sibling
			node.parent.leftChild.values = 2;
			node.parent.leftChild.value3 = 0;
			node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
			node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
			node.parent.leftChild.centerLeftChild = null;
			node.parent.leftChild.centerRightChild = null;
			
			// update children's parent
			if (!node.isLeaf) {
				node.parent.leftChild.rightChild.parent = node;
			} // end parent update

		} // end centerleft case
		else if (node == node.parent.centerChild && node.parent.rightChild.isFourNode()) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.parent.value2;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.rightChild.leftChild;
			
			// update parent value
			node.parent.value2 = node.parent.rightChild.value1;
			
			// devolve sibling
			node.parent.rightChild.values = 2;
			node.parent.rightChild.value1 = node.parent.rightChild.value2;
			node.parent.rightChild.value2 = node.parent.rightChild.value3;
			node.parent.rightChild.value3 = 0;
			node.parent.rightChild.centerChild = node.parent.rightChild.centerRightChild;
			node.parent.rightChild.leftChild = node.parent.rightChild.centerLeftChild;
			node.parent.rightChild.centerLeftChild = null;
			node.parent.rightChild.centerRightChild = null;
			
			// update children's parent
			if (!node.isLeaf) {
				node.parent.rightChild.leftChild.parent = node;
			} // end parent update

		} // end centerRight case
		else if (node == node.parent.rightChild) {

			// evolve node from 2node to 3node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value2;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.rightChild.rightChild;
			
			// update parent value
			node.parent.value2 = node.parent.centerChild.value3;
			
			// devolve sibling
			node.parent.centerChild.values = 2;
			node.parent.centerChild.value3 = 0;
			node.parent.centerChild.centerChild = node.parent.centerChild.centerLeftChild;
			node.parent.centerChild.rightChild = node.parent.centerChild.centerRightChild;
			node.parent.centerChild.centerLeftChild = null;
			node.parent.centerChild.centerRightChild = null;
		
			// update children's parent
			if (!node.isLeaf) {
				node.parent.rightChild.rightChild.parent = node;
			} // end parent update
			
		} // end right case
		
	}// end rotate3node4node

	// this method rotates a value in from a 3node to a 2node with a 4node parent
	public void rotate4Node3Node(TwoFourTreeItem node) {
		
		if(node == node.parent.leftChild) {
			
			// evolve node
			node.values = 2;
			node.value2 = node.parent.value1;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.centerLeftChild.leftChild;
			
			// update parent value
			node.parent.value1 = node.parent.centerLeftChild.value1;
			
			// devlove sibling
			node.parent.centerLeftChild.values = 1;
			node.parent.centerLeftChild.value1 = node.parent.centerLeftChild.value2;
			node.parent.centerLeftChild.value2 = 0;
			node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerChild;
			node.parent.centerLeftChild.centerChild = null;
			
			// non leafcase
			if(!node.isLeaf) {
				node.parent.centerLeftChild.leftChild.parent = node;
			}//end leaf case
			
		}// rotate left case
		else if(node == node.parent.centerLeftChild && node.parent.leftChild.isThreeNode()) {
			
			// evolve node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value1;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.leftChild.rightChild;
			
			// update parent value
			node.parent.value1 = node.parent.leftChild.value2;
			
			// devolve sibling
			node.parent.leftChild.values = 1;
			node.parent.leftChild.value2 = 0;
			node.parent.leftChild.rightChild = node.parent.leftChild.centerChild;
			node.parent.leftChild.centerChild = null;
			
			// non leafcase
			if(!node.isLeaf) {
				node.parent.leftChild.rightChild.parent = node;
			}//end nonleaf case
		}// rotate mid left case1
		else if(node == node.parent.centerLeftChild && node.parent.centerRightChild.isThreeNode()) {
			
			// evolve node
			node.values = 2;
			node.value2 = node.parent.value2;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.centerRightChild.leftChild;
			
			// update parent value
			node.parent.value2 = node.parent.centerRightChild.value1;
			
			// devolve sibling
			node.parent.centerRightChild.values = 1;
			node.parent.centerRightChild.value1 = node.parent.centerRightChild.value2;
			node.parent.centerRightChild.value2 = 0;
			node.parent.centerRightChild.leftChild = node.parent.centerRightChild.centerChild;
			
			// nonleaf case
			if(!node.isLeaf) {
				node.parent.centerLeftChild.leftChild.parent = null;
			}//end nonleaf case
			
		}// rotate mid left case2
		else if(node == node.parent.centerRightChild && node.parent.centerLeftChild.isThreeNode()) {
			
			// evolve node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value2;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.centerLeftChild.rightChild;
			
			// update parent value
			node.parent.value2 = node.parent.centerLeftChild.value2;
			
			// devlove sibling
			node.parent.centerLeftChild.values = 1;
			node.parent.centerLeftChild.value2 = 0;
			node.parent.centerLeftChild.rightChild = node.parent.centerLeftChild.centerChild;
			node.parent.centerLeftChild.centerChild = null;
			
			// non leafcase
			if(!node.isLeaf) {
				node.parent.centerLeftChild.rightChild.parent = node;
			}//end non leaf
			
		}// rotate mid right case1
		else if(node == node.parent.centerRightChild && node.parent.rightChild.isThreeNode()) {
			
			// evolve node
			node.values = 2;
			node.value2 = node.parent.value3;
			node.centerChild = node.rightChild;
			node.rightChild = node.parent.rightChild.leftChild;
			
			// update parent value
			node.parent.value3 = node.parent.rightChild.value1;
			
			// devlove sibling
			node.parent.rightChild.values = 1;
			node.parent.rightChild.value1 = node.parent.rightChild.value2;
			node.parent.rightChild.value2 = 0;
			node.parent.rightChild.leftChild = node.parent.rightChild.centerChild;
			node.parent.rightChild.centerChild = null;
			
			// non leafcase
			if(!node.isLeaf) {
				node.parent.rightChild.leftChild.parent = node;
			}// end nonleaf case
		}// rotate mid right case2
		else if(node == node.parent.rightChild) {
			
			// evolve node
			node.values = 2;
			node.value2 = node.value1;
			node.value1 = node.parent.value3;
			node.centerChild = node.leftChild;
			node.leftChild = node.parent.centerRightChild.rightChild;
			
			// update parent value
			node.parent.value3 = node.parent.centerRightChild.value2;
			
			// devlove sibling
			node.parent.centerRightChild.values = 1;
			node.parent.centerRightChild.value2 = 0;
			node.parent.centerRightChild.rightChild = node.parent.centerRightChild.centerChild;
			node.parent.centerRightChild.centerChild = null;
			
			// non leafcase
			if(!node.isLeaf) {
				node.parent.centerRightChild.rightChild.parent = node;
			}//end nonleaf case
		}//rotate right case
		
	}//end rotate4Node3node
	
	// this method rotates a value in from a 4node to a 2node with a 4node parent
		public void rotate4Node4Node(TwoFourTreeItem node) {
			
			if(node == node.parent.leftChild) {
				
				// evolve node
				node.values = 2;
				node.value2 = node.parent.value1;
				node.centerChild = node.rightChild;
				node.rightChild = node.parent.centerLeftChild.leftChild;
				
				// update parent value
				node.parent.value1 = node.parent.centerLeftChild.value1;
				
				// devlove sibling
				node.parent.centerLeftChild.values = 2;
				node.parent.centerChild = node.parent.centerLeftChild.centerRightChild;
				node.parent.centerLeftChild.leftChild = node.parent.centerLeftChild.centerLeftChild;
				node.parent.centerLeftChild.centerLeftChild = null;
				node.parent.centerLeftChild.centerRightChild = null;
				
				// non leafcase
				if(!node.isLeaf) {
					node.parent.centerLeftChild.leftChild.parent = node;
				}// nonleaf case
			}// rotate left case
			else if(node == node.parent.centerLeftChild && node.parent.leftChild.isFourNode()) {
				
				// evolve node
				node.values = 2;
				node.value2 = node.value1;
				node.value1 = node.parent.value1;
				node.centerChild = node.leftChild;
				node.leftChild = node.parent.leftChild.rightChild;
				
				// update parent value
				node.parent.value1 = node.parent.leftChild.value3;
				
				// devlove sibling
				node.parent.leftChild.values = 2;
				node.parent.leftChild.value3 = 0;
				node.parent.leftChild.centerChild = node.parent.leftChild.centerLeftChild;
				node.parent.leftChild.rightChild = node.parent.leftChild.centerRightChild;
				node.parent.leftChild.centerLeftChild = null;
				node.parent.leftChild.centerRightChild = null;
				
				// non leafcase
				if(!node.isLeaf) {
					node.parent.leftChild.rightChild.parent = node;
				}// end nonleaf case
			}// rotate mid left case1
			else if(node == node.parent.centerLeftChild && node.parent.centerRightChild.isFourNode()) {
				
				// evolve node
				node.values = 2;
				node.value2 = node.parent.value2;
				node.centerChild = node.rightChild;
				node.rightChild = node.parent.centerRightChild.leftChild;
				
				// update parent value
				
				
				// devlove sibling
							
				// non leafcase
				if(!node.isLeaf) {
					node.parent.centerRightChild.leftChild.parent = node;
				}// end nonleaf case
			}// rotate mid left case2
			else if(node == node.parent.centerRightChild && node.parent.centerLeftChild.isFourNode()) {
				// evolve node
				
				// update parent value
							
				// devlove sibling
							
				// non leafcase
				if(!node.isLeaf) {
					
				}// end nonleaf case
			}// rotate mid right case1
			else if(node == node.parent.centerRightChild && node.parent.rightChild.isFourNode()) {
				// evolve node
				
				// update parent value
							
				// devlove sibling
							
				// non leafcase
				if(!node.isLeaf) {
					
				}// end nonleaf case
			}// rotate mid right case2
			else if(node == node.parent.rightChild) {
				// evolve node
				
				// update parent value
							
				// devlove sibling
							
				// non leafcase
				if(!node.isLeaf) {
					
				}// end nonleaf case
			}//rotate right case
			
		}//end rotate4Node4node
	
	public boolean deleteValue(int value) {

		// check if the value is present
		if (hasValue(value)) {

			// the tree only has one node (root), this can return null if root is a 2node
			if (root.isLeaf) {
				root = devolveLeafRoot(root, value);
				return true;
			} // end leaf root case

			// the value we want to delete is contained in a 3node or 4node leaf node. We
			// can simply delete the value and maintain a valid b-tree
			if (delNode.isLeaf && !delNode.isTwoNode()) {
				devolveLeafNode(delNode, value);
			} // end easy leaf case

			// we know the value exists and we want to delete it. Currently delNode is
			// referencing the node the value is to be deleted in.
			// we should first ask if we are in the special root merge case.
			if (root.leftChild.isTwoNode() && root.rightChild.isTwoNode() && root.isTwoNode()) {
				root = mergeRoot(root);

				// we might have just created a new leaf root
				if (root.isLeaf) {
					root = devolveLeafRoot(root, value);
					return true;
				} // end newly created root leaf case

			} // end merge root case

			// Let's create a traversing reference and merge or rotate any 2nodes on the way
			// towards delNode.
			TwoFourTreeItem cur = root;

			// next thing we need to know is if we are in a 2node leaf case or not.
			if (delNode.isLeaf) {

				// we know the root is not a leaf node and that the value we want to delete is
				// at a 2node leaf case
				// since we dont want to start at the root and prematurely exit our loop, lets
				// traverse to a child of the root, and start traversing from there.

				if (root.isTwoNode()) {
					if (value < root.value1) {
						cur = root.leftChild;
					} // go left
					else {
						cur = root.rightChild;
					} // go right
				} // end 2node case
				else if (root.isThreeNode()) {
					if (value < root.value1) {
						cur = root.leftChild;
					} // go left
					else if (value < root.value2) {
						cur = root.centerChild;
					} // go mid
					else {
						cur = root.rightChild;
					} // go right
				} // end 3node case
				else {
					if (value < root.value1) {
						cur = root.leftChild;
					} // go left
					else if (value < root.value2) {
						cur = root.centerLeftChild;
					} // go left mid
					else if (value < root.value3) {
						cur = root.centerRightChild;
					} // go right mid
					else {
						cur = root.rightChild;
					} // go right
				} // end 4node case

				// traversing until we hit delNode
				while (cur != delNode) {

					// traverse as normal if we are not at a 2node
					if (!cur.isTwoNode()) {
						// 3node case
						if (cur.isThreeNode()) {
							if (value < cur.value1) {
								cur = cur.leftChild;
							} // go left
							else if (value < cur.value2) {
								cur = cur.centerChild;
							} // go mid
							else {
								cur = cur.rightChild;
							} // go right
						} // end 3node case
						else if (cur.isFourNode()) {
							if (value < cur.value1) {
								cur = cur.leftChild;
							} // go left
							else if (value < cur.value2) {
								cur = cur.centerLeftChild;
							} // go mid left
							else if (value < cur.value3) {
								cur = cur.centerRightChild;
							} // go mid right
							else {
								cur = cur.rightChild;
							} // go right
						} // end 4node case
					} // end !2node case

					// we've hit a 2node, we must either merge or rotate.
					else {

					}
				} // end while ( traversal )

			} // end leaf case
				// else we are an internal node
			else {

				// First let's check if the root is a 2node, if it is, we should go left or
				// right and start traversing from there
				// let's also check to to see if the value we want to delete is at the root or
				// not

			} // end internal node case

			return true;
		} // end if has value

		return false;
	}

	public void printInOrder() {
		if (root != null)
			root.printInOrder(0);
	}

	public TwoFourTree() {

	}
}
