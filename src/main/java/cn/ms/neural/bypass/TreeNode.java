package cn.ms.neural.bypass;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {

	public T t;
	private TreeNode<T> parent;

	public List<TreeNode<T>> nodes;

	public TreeNode(T stype) {
		t = stype;
		parent = null;
		nodes = new ArrayList<TreeNode<T>>();
	}

	public TreeNode<T> getParent() {
		return parent;
	}

}