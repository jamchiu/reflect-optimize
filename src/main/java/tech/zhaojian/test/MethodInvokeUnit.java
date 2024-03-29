package tech.zhaojian.test;

import com.esotericsoftware.reflectasm.MethodAccess;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tech.zhaojian.domain.TestUser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 执行对象方法测试
 */
public class MethodInvokeUnit{
    public static final int INVOKE_TIME = 100000000;
    public static void main(String[] args) throws Exception{
        for (int i = 0; i < 5; i++) {
            invokeInNormal();
            invokeByReflect();
            invokeByReflectOptimize();
            invokeByReflectASM();
            System.out.println("===========");
        }
    }
    
    /**
     * 通过正常调用方式执行方法
     * 平均耗时：5ms
     */
    public static void invokeInNormal() {
        long startTime = System.currentTimeMillis();
        //start
        TestUser testUser = new TestUser();
        for (int i = 0; i < INVOKE_TIME; i++) {
            testUser.walk();
        }
        //end
        long endTime = System.currentTimeMillis();
        System.out.printf("%30s : %-6dms\n","invokeInNormal",endTime - startTime);
    }

    /**
     * 通过反射的方式执行方法
     * 平均耗时：350ms
     */
    public static void invokeByReflect() throws Exception {
        long startTime = System.currentTimeMillis();
        //start
        Class<?> testUserClazz = Class.forName("tech.zhaojian.domain.TestUser");
        TestUser testUser = (TestUser) testUserClazz.newInstance();
        Method walk = testUserClazz.getMethod("walk");
        for (int i = 0; i < INVOKE_TIME; i++) {
            walk.invoke(testUser);
        }
        //end
        long endTime = System.currentTimeMillis();
        System.out.printf("%30s : %-6dms\n","invokeByReflect",endTime - startTime);
    }

    /**
     * 通过反射 setAccessible=true 方式执行方法
     * 平均耗时：300ms
     */
    public static void invokeByReflectOptimize() throws Exception {
        long startTime = System.currentTimeMillis();
        //start
        Class<?> testUserClazz = Class.forName("tech.zhaojian.domain.TestUser");
        TestUser testUser = (TestUser) testUserClazz.newInstance();
        Method walk = testUserClazz.getMethod("walk");
        walk.setAccessible(true);
        for (int i = 0; i < INVOKE_TIME; i++) {
            walk.invoke(testUser);
        }
        //end
        long endTime = System.currentTimeMillis();
        System.out.printf("%30s : %-6dms\n","invokeByReflectOptimize",endTime - startTime);
    }

    /**
     * 通过ReflectASM包的方式执行方法
     * 平均耗时：800ms
     */
    public static void invokeByReflectASM() throws Exception {
        long startTime = System.currentTimeMillis();
        //start
        Class<?> testUserClazz = Class.forName("tech.zhaojian.domain.TestUser");

        TestUser testUser = (TestUser) testUserClazz.newInstance();
        MethodAccess access = MethodAccess.get(TestUser.class);

        for (int i = 0; i < INVOKE_TIME; i++) {
            access.invoke(testUser, "walk");
        }
        //end
        long endTime = System.currentTimeMillis();
        System.out.printf("%30s : %-6dms\n","invokeByReflectASM",endTime - startTime);
    }
}
