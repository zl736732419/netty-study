package com.zheng.netty.game.module.fuben.request;


import com.zheng.netty.serialize.CustomSerializer;

public class FightRequest extends CustomSerializer {
	/**
	 * 副本id
	 */
	private int fubenId;
	
	/**
	 * 次数
	 */
	private int count;

	public int getFubenId() {
		return fubenId;
	}

	public void setFubenId(int fubenId) {
		this.fubenId = fubenId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	protected void read() {
		this.fubenId = readInt();
		this.count = readInt();
	}

	@Override
	protected void write() {
		writeInt(fubenId);
		writeInt(count);
	}

	@Override
	public String toString() {
		return "FightRequest{" +
				"fubenId=" + fubenId +
				", count=" + count +
				'}';
	}
}
