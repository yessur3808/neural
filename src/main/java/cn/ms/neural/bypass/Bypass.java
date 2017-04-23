package cn.ms.neural.bypass;

import cn.ms.neural.ResultType;
import cn.ms.neural.Result;

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

	public Result doBypass(String node) {
		if (tree == null) {
			return new Result(ResultType.NONINITIALIZE);
		}

		TreeNode<String, String> treeNode = tree.getNode(node);
		if (treeNode == null) {
			return new Result(ResultType.NOTFOUND);
		}

		return new Result(ResultType.SUCCESS, treeNode.value);
	}

}