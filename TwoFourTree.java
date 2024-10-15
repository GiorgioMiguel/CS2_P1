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

	//deletion helper methods----------------
	
	// This method fuses 3 2nodes into a singular 4node root, and returns that new root.
	TwoFourTreeItem mergeRoot(TwoFourTreeItem oldRoot) {
		
		TwoFourTreeItem newRoot = new TwoFourTreeItem(oldRoot.leftChild.value1, oldRoot.value1, oldRoot.rightChild.value1);
		
		if(!oldRoot.leftChild.isLeaf) {
			newRoot.isLeaf = false;
			oldRoot.leftChild.leftChild.parent = newRoot;
			oldRoot.leftChild.rightChild.parent = newRoot;
			oldRoot.rightChild.leftChild.parent = newRoot;
			oldRoot.rightChild.rightChild.parent = newRoot;
		}//end if
		
		newRoot.leftChild = oldRoot.leftChild.leftChild;
		newRoot.centerLeftChild = oldRoot.leftChild.rightChild;
		newRoot.centerRightChild = oldRoot.rightChild.leftChild;
		newRoot.rightChild = oldRoot.rightChild.rightChild;
		
		return newRoot;
		
	}//end fuse root case
	
	// this method devolves a 3node and returns the value it is losing. Parameter value is v1 of the current node(cur). 
	// Parameter node is the node to be devolved. 
	// Using the value of v1 at cur we can know what kind of devolution it is. 
	public int devolve3node(TwoFourTreeItem node, int value) {
		
		node.values -= 1;
		int temp = 0;
		
		// if we are losing v1
		if(value < node.value1) {
			temp = node.value1;
			node.value1 = node.value2;
			node.value2 = 0;
			node.leftChild = node.centerChild;
			node.centerChild = null;
			return temp;
		}//end v1 case
		// we are losing v2
		else {
			temp = node.value2;
			node.value2 = 0;
			node.leftChild = node.centerChild;
			node.centerChild = null;
			return temp;
		}//end v2 case
		
	}//end devolve 3node
	
	//two assumptions, this isn't a 2node and it has value to delete
	public void devolveLeafNode(TwoFourTreeItem node, int value) {
		if(node.isThreeNode()) {
			if(node.value1 == value) {
				node.value1 = node.value2;
				node.value2 = 0;
				node.values = 1;
				
			}// end v1 case
			else {
				node.value2 = 0;
				node.values = 1;
			}// end v2 case
		}// end 3node case
		else {
			if(node.value1 == value) {
				node.value1 = node.value2;
				node.value2 = node.value3;
				node.value3 = 0;
				node.values = 2;
			}// end v1 case
			else if(root.value2 == value) {
				node.value2 = node.value3;
				node.value3 = 0;
				node.values = 2;
			}// end v2 case
			else {
				node.value3 = 0;
				node.values = 2;
			}// end v3 case
		}// end 4node case
	}// end devolve leaf node  
	
	public TwoFourTreeItem devolveLeafRoot(TwoFourTreeItem root, int value) {
		
		// there is only 1 value left to delete, we will then remove all references from the old root and set the new root to null
		if(root.isTwoNode()) {
			delNode = null;
			return null;
		}// end 2node case
		
		// else we check if it is 3node
		else if(root.isThreeNode()) {
			if(root.value1 == value) {
				root.value1 = root.value2;
				root.value2 = 0;
				root.values = 1;
				return root;
			}// end v1 case
			else {
				root.value2 = 0;
				root.values = 1;
				return root;
			}// end v2 case
		}// end 3node case
		
		// else it's a 4node
		else {
			if(root.value1 == value) {
				root.value1 = root.value2;
				root.value2 = root.value3;
				root.value3 = 0;
				root.values = 2;
				return root;
			}// end v1 case
			else if(root.value2 == value) {
				root.value2 = root.value3;
				root.value3 = 0;
				root.values = 2;
				return root;
			}// end v2 case
			else {
				root.value3 = 0;
				root.values = 2;
				return root;
			}// end v3 case
		}// end 4node case
	}//end leaf root devolution
	
	public boolean deleteValue(int value) {
		
		// check if the value is present
		if(hasValue(value)) {
			
			// the tree only has one node (root), this can return null if root is a 2node
			if(root.isLeaf) {
				root = devolveLeafRoot(root, value);
				return true;
			}// end leaf root case
			
			// we know the value exists and we want to delete it. Currently delNode is referencing the node the value is to be deleted in. 
			// we should first ask if we are in the special root merge case.
			if(root.leftChild.isTwoNode() && root.rightChild.isTwoNode() && root.isTwoNode()) {
				root = mergeRoot(root);
			}// end merge root case
			
			//next thing we need to know is if we are in a leaf node case or not. 
			if(delNode.isLeaf) {
				
				// the value we want to delete is at a leaf node, we will create a traversing reference and merge or rotate any 2nodes on the way towards delNode.
				TwoFourTreeItem cur = root;
				
				// First let's check if the root is a 2node, if it is, we should go left or right and start traversing from there
				// let's also check to to see if the value we want to delete is at the root or not
				
				// traversing until we hit delNode
				while(cur != delNode) {
					
					//traverse as normal if we are not at a 2node
					if(!cur.isTwoNode()) {
						// 3node case
						if(cur.isThreeNode()) {
							if(value < cur.value1) {
								cur = cur.leftChild;
							}// go left
							else if(value < cur.value2){
								cur = cur.centerChild;
							}// go mid
							else {
								cur = cur.rightChild;
							}// go right
						}// end 3node case
						else if(cur.isFourNode()) {
							if(value < cur.value1) {
								cur = cur.leftChild;
							}// go left
							else if(value < cur.value2) {
								cur = cur.centerLeftChild;
							}// go mid left
							else if(value < cur.value3) {
								cur = cur.centerRightChild;
							}// go mid right
							else {
								cur = cur.rightChild;
							}// go right
						}// end 4node case
					}//end !2node case
					
					//we've hit a 2node, we must either merge or rotate. 
					else {
						
					}
				}//end while ( traversal )
				
			}// end leaf case
			//else we are an internal node
			else {
				
			}
			
			
			return true;
		}
		
		return false;
	}

	public void printInOrder() {
		if (root != null)
			root.printInOrder(0);
	}

	public TwoFourTree() {

	}
}
