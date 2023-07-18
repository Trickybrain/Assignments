package bintree;

public class AVL<T extends Comparable> extends BST {
    public AVL(T rootValue){
        super(rootValue);
    }

    public AVL(){
        super();
    }

    /*
        Method to insert a new value into an AVL tree. You do not need to modify
        the insert method in any way. For this assignment, you are implementing the helper
        methods the insert function calls in order to ensure the tree is balanced.
     */
    @Override
    public void insert(Comparable newValue){
        int leftSkew = 0;
        int rightSkew = 0;

        super.insert(newValue); // Insert the value into the tree
        BSTNode nodeInserted = super.getNodeWithValue(root, newValue); // Get the node corresponding with the inserted value
        updateAugmentation(nodeInserted);
        BSTNode unbalancedNode = findLowestUnBalancedNode(nodeInserted); // find unbalanced node

        // Perform necessary rotations to maintain tree balance.
        if(unbalancedNode != null){
            if(unbalancedNode.left != null){
                leftSkew = skew(unbalancedNode.left);
            }
            if(unbalancedNode.right != null){
                rightSkew = skew(unbalancedNode.right);
            }

            if(rightSkew == 1){
                rotateLeft(unbalancedNode);
            } else if(leftSkew == -1){
                rotateRight(unbalancedNode);
            } else if(rightSkew == -1){
                rotateRight(unbalancedNode.right);
                rotateLeft(unbalancedNode);
            } else if(leftSkew == 1){
                rotateLeft(unbalancedNode.left);
                rotateRight(unbalancedNode);
            }
        }
    }

    // Perform a left rotation around nodeToRotate
    public void rotateLeft(BSTNode<T> nodeToRotate) {
        BSTNode<T> pivot = nodeToRotate.right;
        nodeToRotate.right = pivot.left;
        if (pivot.left != null) {
            pivot.left.parent = nodeToRotate;
        }
        pivot.parent = nodeToRotate.parent;
        if (nodeToRotate.parent == null) {
            root = pivot;
        } else if (nodeToRotate == nodeToRotate.parent.left) {
            nodeToRotate.parent.left = pivot;
        } else {
            nodeToRotate.parent.right = pivot;
        }
        pivot.left = nodeToRotate;
        nodeToRotate.parent = pivot;

        // Update heights
        nodeToRotate.height = height(nodeToRotate);
        pivot.height = height(pivot);
    }

    // Perform a right rotation around nodeToRotate
    public void rotateRight(BSTNode<T> nodeToRotate) {
        BSTNode<T> pivot = nodeToRotate.left;
        nodeToRotate.left = pivot.right;
        if (pivot.right != null) {
            pivot.right.parent = nodeToRotate;
        }
        pivot.parent = nodeToRotate.parent;
        if (nodeToRotate.parent == null) {
            root = pivot;
        } else if (nodeToRotate == nodeToRotate.parent.right) {
            nodeToRotate.parent.right = pivot;
        } else {
            nodeToRotate.parent.left = pivot;
        }
        pivot.right = nodeToRotate;
        nodeToRotate.parent = pivot;

        // Update heights
        nodeToRotate.height = height(nodeToRotate);
        pivot.height = height(pivot);
    }

    /*
        Find the lowest unbalanced node in the AVL tree after an insert operation.
        When this function is called by the insert method, the node passed in is the
        node that was most recently inserted.
     */
    public BSTNode<T> findLowestUnBalancedNode(BSTNode<T> currentNode) {
        if (currentNode == null) {
            return null;
        }

        int skew = skew(currentNode);

        if (skew > 1 || skew < -1) {
            // The current node is unbalanced, so return it
            return currentNode;
        }

        // Recursively check the left and right subtrees
        BSTNode<T> leftNode = findLowestUnBalancedNode(currentNode.left);
        BSTNode<T> rightNode = findLowestUnBalancedNode(currentNode.right);

        // Return the lowest unbalanced node found between left and right subtrees
        if (leftNode != null) {
            return leftNode;
        } else {
            return rightNode;
        }
    }

    // Maintain the subtree properties after a node is inserted
    public void updateAugmentation(BSTNode startingNode){
        if(startingNode == null){
            return;
        }
        startingNode.height = height(startingNode);
        updateAugmentation(startingNode.parent);
    }

    // Determine the skew of a node.
    public int skew(BSTNode node){
        int rightHeight = 0;
        int leftHeight = 0;

        if(node.right != null){
            rightHeight = 1 + height(node.right);
        }
        if(node.left != null){
            leftHeight = 1 + height(node.left);
        }
        return rightHeight - leftHeight;
    }

    // Determine the height of a node.
    public static int height(BSTNode node){
        if(node == null){
            return 0;
        }else if(node.isLeaf()){
            return 0;
        } else if(node.left == null){
            return 1 + node.right.height;
        } else if(node.right == null){
            return 1 + node.left.height;
        }
        return 1 + Math.max(node.left.height, node.right.height);
    }
}
