package com.zheng.netty.protobuf;

import com.zheng.netty.serialize.protobuf.DepartmentModule;
import com.zheng.netty.serialize.protobuf.DepartmentModule2;
import org.junit.Test;

/**
 * @Author zhenglian
 * @Date 2019/4/19
 */
public class DepartmentTest {
    @Test
    public void test() throws Exception {
        DepartmentModule.Department.Person p1 = DepartmentModule.Department.Person.newBuilder()
                .setName("zhangsan")
                .build();
        DepartmentModule.Department.Person p2 = DepartmentModule.Department.Person.newBuilder()
                .setName("lisi")
                .build();
        DepartmentModule.Department department = DepartmentModule.Department.newBuilder()
                .addPersons(p1)
                .addPersons(p2)
                .build();
        byte[] bytes = department.toByteArray();
        DepartmentModule2.Department2 department2 = DepartmentModule2.Department2.parseFrom(bytes);
        System.out.println(department2);
    }
}
