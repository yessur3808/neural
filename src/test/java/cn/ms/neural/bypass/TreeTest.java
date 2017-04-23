package cn.ms.neural.bypass;

import org.junit.Assert;
import org.junit.Test;

public class TreeTest {

	private Tree<String, String> newTree() {
		Tree<String, String> tree = new Tree<String, String>();
		tree.addNode("bypass");
		tree.addNode(tree.getNode("bypass"), "weixin");
		tree.addNode(tree.getNode("bypass"), "app");

		tree.addNode(tree.getNode("weixin"), "weixinQuery", "S01");
		tree.addNode(tree.getNode("weixin"), "weixinAdd", "S02");

		tree.addNode(tree.getNode("app"), "appQuery", "S02");
		tree.addNode(tree.getNode("app"), "appAdd", "S01");

		return tree;
	}

	@Test
	public void testSearchTree() throws Exception {
		Tree<String, String> tree = newTree();
		TreeNode<String, String> treeNode = tree.search(tree.root, "appQuery");
		Assert.assertNotNull(treeNode);
		Assert.assertEquals("appQuery", treeNode.key);
		Assert.assertEquals("S02", treeNode.value);
	}

	@Test
	public void testGetTree() throws Exception {
		Tree<String, String> tree = newTree();
		TreeNode<String, String> treeNode = tree.getNode("weixinQuery");
		Assert.assertNotNull(treeNode);
		Assert.assertEquals("weixinQuery", treeNode.key);
		Assert.assertEquals("S01", treeNode.value);
	}

}