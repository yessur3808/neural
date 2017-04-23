package cn.ms.neural.bypass;

public class Tree<T> {

	public TreeNode<T> root;

	/**
	 * 增加根节点
	 * 
	 * @param treeNode
	 * @param newTreeNode
	 */
	public void addNode(TreeNode<T> treeNode, T newTreeNode) {
		if (null == treeNode) {
			if (null == root) {
				root = new TreeNode<T>(newTreeNode);
			}
		} else {
			TreeNode<T> temp = new TreeNode<T>(newTreeNode);
			treeNode.nodes.add(temp);
		}
	}

	/**
	 * 查找newNode这个节点
	 * 
	 * @param treeNode
	 * @param newTreeNode
	 * @return
	 */
	public TreeNode<T> search(TreeNode<T> treeNode, T newTreeNode) {
		TreeNode<T> temp = null;
		if (treeNode.t.equals(newTreeNode)) {
			return treeNode;
		}

		for (int i = 0; i < treeNode.nodes.size(); i++) {
			temp = search(treeNode.nodes.get(i), newTreeNode);
			if (null != temp) {
				break;
			}
		}

		return temp;
	}

	public TreeNode<T> getNode(T newTreeNode) {
		return search(root, newTreeNode);
	}

}