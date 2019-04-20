package com.zheng.netty.serialize.protobuf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhenglian
 * @Date 2019/4/16
 */
public class Player implements Serializable {
    private Integer id;
    private Integer age;
    private String name;
    private List<Integer> skills = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", skills=" + skills +
                '}';
    }
}
