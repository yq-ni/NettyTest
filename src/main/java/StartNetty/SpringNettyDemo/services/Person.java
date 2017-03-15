package StartNetty.SpringNettyDemo.services;

import java.io.Serializable;

/**
 * Created by nyq on 2017/3/13.
 */
public class Person implements Serializable {
    private int id;
    private String name;
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{ id: " + id + ", name: " + name + ", age: " + age + "}";
    }
}
