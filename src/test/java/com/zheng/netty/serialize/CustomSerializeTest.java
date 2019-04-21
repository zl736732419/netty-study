package com.zheng.netty.serialize;

import com.zheng.netty.serialize.protobuf.PBPerson;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @Author zhenglian
 * @Date 2019/4/20
 */
public class CustomSerializeTest {
    
    @Test
    public void test() throws Exception {
        int id = 10;
        int age = 20;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(int2Bytes(id));
        output.write(int2Bytes(age));
        byte[] bytes = output.toByteArray();
        System.out.println(Arrays.toString(bytes));
        output.close();
        System.out.println("==========================");
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        byte[] idBytes = new byte[4];
        input.read(idBytes);
        System.out.println("id:" + bytes2Int(idBytes));
        byte[] ageBytes = new byte[4];
        input.read(ageBytes);
        System.out.println("age:" + bytes2Int(ageBytes));
        input.close();
    }

    /**
     * nio ByteBuffer，隐藏位运算，但byte大小是固定的，无法自动扩容
     */
    @Test
    public void test2() {
        int id = 10;
        int age = 20;
        // 需要指定容量大小，超出容量将报错
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putInt(id);
        byteBuffer.putInt(age);
        byte[] bytes = byteBuffer.array();
        System.out.println(Arrays.toString(bytes));
        System.out.println("=============================");
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(bytes);
        System.out.println("id:" + byteBuffer1.getInt());
        System.out.println("age:" + byteBuffer1.getInt());
    }

    /**
     * netty ChannelBuffer,结合io,nio有点，隐藏位运算，实现自动扩容
     */
    @Test
    public void test3() {
        int id = 10;
        int age = 20;
        ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer();
        channelBuffer.writeInt(id);
        channelBuffer.writeInt(age);
        int index = channelBuffer.writerIndex();
        byte[] bytes = new byte[index];
        channelBuffer.readBytes(bytes);
        System.out.println(Arrays.toString(bytes));
        System.out.println("============================");
        ChannelBuffer channelBuffer1 = ChannelBuffers.wrappedBuffer(bytes);
        System.out.println("id:" + channelBuffer1.readInt());
        System.out.println("age:" + channelBuffer1.readInt());
        
    }
    
    @Test
    public void test4() throws Exception {
        PBPerson.Person person = PBPerson.Person.newBuilder()
                .setId(10)
                .setAge(20)
                .build();
        byte[] bytes = person.toByteArray();
        System.out.println(Arrays.toString(bytes));
        System.out.println("==========================");
        PBPerson.Person person1 = PBPerson.Person.parseFrom(bytes);
        System.out.println(person1);
    }
    
    /**
     * 大端字节序列，高位在前，低位在后
     * @param a
     * @return
     */
    private byte[] int2Bytes(int a) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (a >> 3*8);
        bytes[1] = (byte) (a >> 2*8);
        bytes[2] = (byte) (a >> 1*8);
        bytes[3] = (byte) (a >> 0*8);
        return bytes;
    }
    
    private int bytes2Int(byte[] bytes) {
        return (bytes[0] << 3*8) |
                (bytes[1] << 2*8) |
                (bytes[2] << 1*8) |
                (bytes[3] << 0*8);
    }
}
