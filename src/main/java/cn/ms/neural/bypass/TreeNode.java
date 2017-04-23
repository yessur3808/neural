package cn.ms.neural.bypass;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<K, V> {

	public K key;
	public V value;
	private TreeNode<K, V> parent;
	public List<TreeNode<K, V>> nodes;

	public TreeNode(K key, V value) {
		this.key = key;
		this.value = value;
		parent = null;
		nodes = new ArrayList<TreeNode<K, V>>();
	}

	public TreeNode<K, V> getParent() {
		return parent;
	}

}