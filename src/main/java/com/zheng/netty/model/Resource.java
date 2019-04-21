package com.zheng.netty.model;

import com.zheng.netty.serialize.CustomSerializer;

/**
 * @Author zhenglian
 * @Date 2019/4/21
 */
public class Resource extends CustomSerializer {
    private int gold;

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    @Override
    protected void write() {
        writeInt(gold);
    }

    @Override
    protected void read() {
        gold = readInt();
    }

    @Override
    public String toString() {
        return "Resource{" +
                "gold=" + gold +
                '}';
    }
}
