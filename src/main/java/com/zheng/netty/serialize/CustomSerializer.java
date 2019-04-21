package com.zheng.netty.serialize;

import org.jboss.netty.buffer.ChannelBuffer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhenglian
 * @Date 2019/4/21
 */
public abstract class CustomSerializer {
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private ChannelBuffer readBuffer;
    private ChannelBuffer writeBuffer;

    public CustomSerializer readFromBytes(byte[] bytes) {
        readBuffer = BufferFactory.getBuffer(bytes);
        read();
        readBuffer.clear();
        return this;
    }

    private CustomSerializer readFromBuffer(ChannelBuffer buffer) {
        readBuffer = buffer;
        read();
        readBuffer.clear();
        return this;
    }

    private CustomSerializer writeToLocalBuff() {
        writeBuffer = BufferFactory.getBuffer();
        write();
        return this;
    }

    private CustomSerializer writeToTargetBuff(ChannelBuffer buffer) {
        writeBuffer = buffer;
        write();
        return this;
    }

    public byte[] getBytes() {
        writeToLocalBuff();
        byte[] bytes;
        int index = writeBuffer.writerIndex();
        if (index == 0) {
            bytes = new byte[0];
        } else {
            bytes = new byte[index];
            writeBuffer.readBytes(bytes);
        }
        writeBuffer.clear();
        return bytes;
    }

    public byte readByte() {
        return readBuffer.readByte();
    }

    public short readShort() {
        return readBuffer.readShort();
    }

    public int readInt() {
        return readBuffer.readInt();
    }

    public long readLong() {
        return readBuffer.readLong();
    }

    public float readFloat() {
        return readBuffer.readFloat();
    }

    public double readDouble() {
        return readBuffer.readDouble();
    }

    public String readString() {
        int size = readBuffer.readShort();
        if (size <= 0) {
            return "";
        }

        byte[] bytes = new byte[size];
        readBuffer.readBytes(bytes);
        return new String(bytes, CHARSET);
    }

    public <T> List<T> readList(Class<T> clz) {
        List<T> list = new ArrayList<>();
        int size = readBuffer.readShort();
        for (int i = 0; i < size; i++) {
            list.add(read(clz));
        }
        return list;
    }

    public <K,V> Map<K,V> readMap(Class<K> keyClz, Class<V> valueClz) {
        Map<K,V> map = new HashMap<>();
        int size = readBuffer.readShort();
        for (int i = 0; i < size; i++) {
            K key = read(keyClz);
            V value = read(valueClz);
            map.put(key, value);
        }
        return map;
    }
    
    public <I> I read(Class<I> clz) {
        Object t = null;
        if ( clz == int.class || clz == Integer.class) {
            t = this.readInt();
        } else if (clz == byte.class || clz == Byte.class){
            t = this.readByte();
        } else if (clz == short.class || clz == Short.class){
            t = this.readShort();
        } else if (clz == long.class || clz == Long.class){
            t = this.readLong();
        } else if (clz == float.class || clz == Float.class){
            t = readFloat();
        } else if (clz == double.class || clz == Double.class){
            t = readDouble();
        } else if (clz == String.class ){
            t = readString();
        } else if (CustomSerializer.class.isAssignableFrom(clz)){
            try {
                byte hasObject = this.readBuffer.readByte();
                if(hasObject == 1){
                    CustomSerializer temp = (CustomSerializer)clz.newInstance();
                    temp.readFromBuffer(this.readBuffer);
                    t = temp;
                }else{
                    t = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException(String.format("不支持类型:[%s]", clz));
        }
        return (I) t;
    }

    public CustomSerializer writeByte(Byte value) {
        writeBuffer.writeByte(value);
        return this;
    }

    public CustomSerializer writeShort(Short value) {
        writeBuffer.writeShort(value);
        return this;
    }

    public CustomSerializer writeInt(Integer value) {
        writeBuffer.writeInt(value);
        return this;
    }

    public CustomSerializer writeLong(Long value) {
        writeBuffer.writeLong(value);
        return this;
    }

    public CustomSerializer writeFloat(Float value) {
        writeBuffer.writeFloat(value);
        return this;
    }

    public CustomSerializer writeDouble(Double value) {
        writeBuffer.writeDouble(value);
        return this;
    }

    public CustomSerializer writeString(String value) {
        if (value == null || value.isEmpty()) {
            writeShort((short) 0);
            return this;
        }

        byte data[] = value.getBytes(CHARSET);
        short len = (short) data.length;
        writeBuffer.writeShort(len);
        writeBuffer.writeBytes(data);
        return this;
    }

    public CustomSerializer writeObject(Object object) {

        if(object == null){
            writeByte((byte)0);
        }else{
            if (object instanceof Integer) {
                writeInt((int) object);
                return this;
            }

            if (object instanceof Long) {
                writeLong((long) object);
                return this;
            }

            if (object instanceof Short) {
                writeShort((short) object);
                return this;
            }

            if (object instanceof Byte) {
                writeByte((byte) object);
                return this;
            }

            if (object instanceof String) {
                String value = (String) object;
                writeString(value);
                return this;
            }
            if (object instanceof CustomSerializer) {
                writeByte((byte)1);
                CustomSerializer value = (CustomSerializer) object;
                value.writeToTargetBuff(writeBuffer);
                return this;
            }

            throw new RuntimeException("不可序列化的类型:" + object.getClass());
        }

        return this;
    }

    public <T> CustomSerializer writeList(List<T> list) {
        if (isEmpty(list)) {
            writeBuffer.writeShort((short) 0);
            return this;
        }
        writeBuffer.writeShort((short) list.size());
        for (T item : list) {
            writeObject(item);
        }
        return this;
    }

    public <K,V> CustomSerializer writeMap(Map<K, V> map) {
        if (isEmpty(map)) {
            writeBuffer.writeShort((short) 0);
            return this;
        }
        writeBuffer.writeShort((short) map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            writeObject(entry.getKey());
            writeObject(entry.getValue());
        }
        return this;
    }
    
    private <T> boolean isEmpty(Collection<T> c) {
        return c == null || c.size() == 0;
    }
    public <K,V> boolean isEmpty(Map<K,V> c) {
        return c == null || c.size() == 0;
    }
    
    protected abstract void write();

    protected abstract void read();
}
