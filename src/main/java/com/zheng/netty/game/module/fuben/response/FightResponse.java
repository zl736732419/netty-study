package com.zheng.netty.game.module.fuben.response;


import com.zheng.netty.serialize.CustomSerializer;

public class FightResponse extends CustomSerializer {
	/**
	 * 获取金币
	 */
	private int gold;

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	@Override
	protected void read() {
		this.gold = readInt();
	}

	@Override
	protected void write() {
		writeInt(gold);
	}

	@Override
	public String toString() {
		return "FightResponse{" +
				"gold=" + gold +
				'}';
	}
}
