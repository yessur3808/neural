package cn.ms.neural.bypass;

import cn.ms.neural.ResultType;
import cn.ms.neural.RouterResult;

/**
 * 流量分流
 * 
 * @author lry
 */
public class Bypass {

	volatile Tree<String, String> tree;

	public void notifys(Tree<String, String> tree) {
		this.tree = tree;
	}

	public RouterResult doBypass(String node) {
		if (tree == null) {
			return new RouterResult(ResultType.NONINITIALIZE);
		}

		TreeNode<String, String> treeNode = tree.getNode(node);
		if (treeNode == null) {
			return new RouterResult(ResultType.NOTFOUND);
		}

		return new RouterResult(ResultType.SUCCESS, treeNode.value);
	}

}