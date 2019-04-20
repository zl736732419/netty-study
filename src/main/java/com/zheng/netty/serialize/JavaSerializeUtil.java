package com.zheng.netty.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @Author zhenglian
 * @Date 2019/4/16
 */
public class JavaSerializeUtil {
    
    public static <T> byte[] serialize(T bean) {
        if (null == bean) {
            return null;
        }
        ObjectOutputStream objOutput = null;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            objOutput = new ObjectOutputStream(output);
            objOutput.writeObject(bean);
            return output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != objOutput) {
                try {
                    objOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    public static <T> T deserialize(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return null;
        }
        ObjectInputStream objInput = null;
        try {
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);
            objInput = new ObjectInputStream(input);
            return (T) objInput.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != objInput) {
                try {
                    objInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
}
