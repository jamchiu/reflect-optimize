package tech.zhaojian.test;

import com.esotericsoftware.reflectasm.MethodAccess;
import tech.zhaojian.domain.TestUser;

import java.lang.reflect.Method;

/**
 * 执行对象方法测试
 */
public class ArgsMethodInvokeUnit {
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
            testUser.say("hello");
        }
        //end
        long endTime = System.currentTimeMillis();
        System.out.printf("%30s : %-6dms\n","invokeInNormal",endTime - startTime);
    }

    /**
     * 通过反射的方式执行方法
     * 平均耗时：400ms
     */
    public static void invokeByReflect() throws Exception {
        long startTime = System.currentTimeMillis();
        //start
        Class<?> testUserClazz = Class.forName("tech.zhaojian.domain.TestUser");
        TestUser testUser = (TestUser) testUserClazz.newInstance();
        Method say = testUserClazz.getMethod("say",String.class);
        for (int i = 0; i < INVOKE_TIME; i++) {
            say.invoke(testUser,"hello");
        }
        //end
        long endTime = System.currentTimeMillis();
        System.out.printf("%30s : %-6dms\n","invokeByReflect",endTime - startTime);
    }

    /**
     * 通过反射 setAccessible=true 方式执行方法
     * 平均耗时：390ms
     */
    public static void invokeByReflectOptimize() throws Exception {
        long startTime = System.currentTimeMillis();
        //start
        Class<?> testUserClazz = Class.forName("tech.zhaojian.domain.TestUser");
        TestUser testUser = (TestUser) testUserClazz.newInstance();
        Method say = testUserClazz.getMethod("say",String.class);
        say.setAccessible(true);
        for (int i = 0; i < INVOKE_TIME; i++) {
            say.invoke(testUser,"hello");
        }
        //end
        long endTime = System.currentTimeMillis();
        System.out.printf("%30s : %-6dms\n","invokeByReflectOptimize",endTime - startTime);
    }

    /**
     * 通过ReflectASM包的方式执行方法
     * 平均耗时：900ms
     */
    public static void invokeByReflectASM() throws Exception {
        long startTime = System.currentTimeMillis();
        //start
        Class<?> testUserClazz = Class.forName("tech.zhaojian.domain.TestUser");

        TestUser testUser = (TestUser) testUserClazz.newInstance();
        MethodAccess access = MethodAccess.get(TestUser.class);

        for (int i = 0; i < INVOKE_TIME; i++) {
            access.invoke(testUser, "say","hello");
        }
        //end
        long endTime = System.currentTimeMillis();
        System.out.printf("%30s : %-6dms\n","invokeByReflectASM",endTime - startTime);
    }
}
