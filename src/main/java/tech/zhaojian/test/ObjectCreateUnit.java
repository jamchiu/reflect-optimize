package tech.zhaojian.test;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tech.zhaojian.domain.TestUser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 创建对象测试
 */
public class ObjectCreateUnit {
    public static long startTime;
    public static long endTime;

    @Before
    public void initStartTime() {
        ObjectCreateUnit.startTime = System.currentTimeMillis();
    }

    @After
    public void initEndTime(){
        ObjectCreateUnit.endTime = System.currentTimeMillis();
        System.out.println(">>>>>>   " + (ObjectCreateUnit.endTime - ObjectCreateUnit.startTime) + "ms   <<<<<<");
    }

    @Test
    /**
     * 通过正常new的方式创建对象
     * 平均耗时：5ms
     */
    public void createInNormal() {
        for (int i = 0; i < 1000000; i++) {
            new TestUser();
        }
    }

    @Test
    /**
     * 通过反射的方式创建对象
     * 平均耗时：800ms
     */
    public void createByReflect() throws Exception {
        for (int i = 0; i < 1000000; i++) {
            Class.forName("tech.zhaojian.domain.TestUser").newInstance();
        }
    }

    @Test
    /**
     * 通过正常new的方式创建对象
     * 平均耗时：50ms
     */
    public void createArgsInNormal() {
        for (int i = 0; i < 1000000; i++) {
            new TestUser(i,"hello");
        }
    }

    @Test
    /**
     * 通过反射的方式创建对象
     * 平均耗时：1300ms
     */
    public void createArgsByReflect() throws Exception {
        for (int i = 0; i < 1000000; i++) {
            Class<?> testUserClazz = Class.forName("tech.zhaojian.domain.TestUser");
            Constructor<?> constructor = testUserClazz.getDeclaredConstructor(new Class[]{long.class,String.class});
            constructor.newInstance(i, "hello");
        }
    }

    @Test
    /**
     * 通过反射缓存优化的方式创建对象，由此可见Class.forName("xxx")方法比较耗时
     * 平均耗时：50ms
     */
    public void createByReflectOptimize() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> testUserClazz = Class.forName("tech.zhaojian.domain.TestUser");
        for (int i = 0; i < 1000000; i++) {
            testUserClazz.newInstance();
        }
    }

    @Test
    /**
     * 通过ReflectASM包的方式创建对象
     * 平均耗时：20ms
     */
    public void invokeByReflectASM(){
        ConstructorAccess<TestUser> access = ConstructorAccess.get(TestUser.class);
        for (int i = 0; i < 1000000; i++) {
            access.newInstance();
        }
    }
}
