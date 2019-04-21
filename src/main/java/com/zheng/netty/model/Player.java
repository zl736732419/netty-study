package com.zheng.netty.model;

import com.zheng.netty.serialize.CustomSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhenglian
 * @Date 2019/4/21
 */
public class Player extends CustomSerializer {
    private Long playerId;
    private String name;
    private List<Integer> skills = new ArrayList<>();
    private Resource resource;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getSkills() {
        return skills;
    }

    public void setSkills(List<Integer> skills) {
        this.skills = skills;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    protected void write() {
        writeLong(playerId);
        writeString(name);
        writeList(skills);
        writeObject(resource);
    }

    @Override
    protected void read() {
        playerId = readLong();
        name = readString();
        skills = readList(Integer.class);
        read(Resource.class);
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", name='" + name + '\'' +
                ", skills=" + skills +
                '}';
    }
}
