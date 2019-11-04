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
public class MethodInvokeUnit {
    public static long startTime;
    public static long endTime;

    @Before
    public void initStartTime() {
        ObjectCreateUnit.startTime = System.currentTimeMillis();
    }

    @After
    public void initEndTime() {
        ObjectCreateUnit.endTime = System.currentTimeMillis();
        System.out.println(">>>>>>   " + (ObjectCreateUnit.endTime - ObjectCreateUnit.startTime) + "ms   <<<<<<");
    }

    @Test
    /**
     * 通过正常调用方式执行方法
     * 平均耗时：12ms
     */
    public void invokeInNormal() {
        TestUser testUser = new TestUser();
        for (int i = 0; i < 100000000; i++) {
            testUser.walk();
        }
    }

    @Test
    /**
     * 通过反射的方式执行方法
     * 平均耗时：380ms
     */
    public void invokeByReflect() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> testUserClazz = Class.forName("tech.zhaojian.domain.TestUser");
        TestUser testUser = (TestUser) testUserClazz.newInstance();
        //Method say = testUserClazz.getMethod("say",String.class);
        Method walk = testUserClazz.getMethod("walk");
        for (int i = 0; i < 100000000; i++) {
            //say.invoke(testUser, "hello");
            walk.invoke(testUser);
        }
    }

    @Test
    /**
     * 通过反射 setAccessible=true 方式执行方法
     * 理论上来说关闭安全检查反射的效率会更高，但是实测下来，反而是慢了将近3倍
     * 平均耗时：900ms
     */
    public void invokeByReflectOptimize() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> testUserClazz = Class.forName("tech.zhaojian.domain.TestUser");
        TestUser testUser = (TestUser) testUserClazz.newInstance();
        //Method say = testUserClazz.getMethod("say",String.class);
        Method walk = testUserClazz.getMethod("walk");
        //say.setAccessible(true);
        walk.setAccessible(true);
        for (int i = 0; i < 100000000; i++) {
            //say.invoke(testUser, "hello");
            walk.invoke(testUser);
        }
    }

    @Test
    /**
     * 通过ReflectASM包的方式执行方法
     * 平均耗时：380ms
     */
    public void invokeByReflectASM() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> testUserClazz = Class.forName("tech.zhaojian.domain.TestUser");

        TestUser testUser = (TestUser) testUserClazz.newInstance();
        MethodAccess access = MethodAccess.get(TestUser.class);

        for (int i = 0; i < 100000000; i++) {
            access.invoke(testUser, "walk");
        }
    }
}
