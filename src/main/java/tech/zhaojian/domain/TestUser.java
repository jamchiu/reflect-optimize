package tech.zhaojian.domain;

public class TestUser {
    private long id;
    private String name;

    public TestUser() {
    }

    public TestUser(long id, String name) {
        this.id = id;
        this.name = name;
    }

    //有参函数
    public void say(String words) {
        //System.out.println("the user say:" + words);
    }

    //无参函数
    public void walk() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
