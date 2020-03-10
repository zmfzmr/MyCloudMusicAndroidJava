package com.ixuea.courses.mymusic.domain.test;

import io.realm.RealmObject;

/**
 * 只是测试用的
 * <p>
 * <p>
 * 人的模型
 * 该类在应用中无实际意义
 * 只是用来测试Realm ORM框架的
 * <p>
 * DiscoveryFragment的initDatum方法中测试：
 */
public class Person extends RealmObject {
    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private int age;

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
}
