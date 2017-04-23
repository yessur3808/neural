package cn.ms.neural.bypass;

public class Tree<K, V> {

	public TreeNode<K, V> root;

	public void addNode(K newTreeNode) {
		this.addNode(null, newTreeNode);
	}

	public void addNode(TreeNode<K, V> treeNode, K newTreeNode) {
		this.addNode(treeNode, newTreeNode, null);
	}

	public void addNode(TreeNode<K, V> treeNode, K newTreeNode, V value) {
		if (null == treeNode) {
			if (null == root) {
				root = new TreeNode<K, V>(newTreeNode, value);
			}
		} else {
			TreeNode<K, V> tempTreeNode = this.search(root, newTreeNode);
			if (tempTreeNode != null) {
				throw new IllegalArgumentException(newTreeNode + "重复");
			}
			TreeNode<K, V> temp = new TreeNode<K, V>(newTreeNode, value);
			treeNode.nodes.add(temp);
		}
	}

	public TreeNode<K, V> search(TreeNode<K, V> treeNode, K newTreeNode) {
		TreeNode<K, V> temp = null;
		if (treeNode.key.equals(newTreeNode)) {
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

	public TreeNode<K, V> getNode(K newTreeNode) {
		return search(root, newTreeNode);
	}

}