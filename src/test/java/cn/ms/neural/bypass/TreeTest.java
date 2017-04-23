package cn.ms.neural.bypass;

import com.alibaba.fastjson.JSON;

public class TreeTest {

	public static void main(String[] args) {
		Tree<String> tree = new Tree<String>();
		tree.addNode(null, "string");
		tree.addNode(tree.getNode("string"), "hello");
		tree.addNode(tree.getNode("string"), "world");
		tree.addNode(tree.getNode("hello"), "sinny");
		tree.addNode(tree.getNode("hello"), "fredric");
		tree.addNode(tree.getNode("world"), "Hi");
		tree.addNode(tree.getNode("world"), "York");
		System.out.println(JSON.toJSONString(tree));
	}

}