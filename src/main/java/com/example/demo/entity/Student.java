package com.example.demo.entity;

/**
 *
 *  测试导出excel 实例类
 *
 * @version 1.0
 * @since JDK1.7
 * @date 2018年1月18日 下午1:49:28
 */
public class Student {

    private int empno;

    private String name;

    private String gender;

    private int age;

    public int getEmpno() {
        return empno;
    }

    public Student() {
        super();
    }

    public Student(int empno, String name, String gender, int age) {
        super();
        this.empno = empno;
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public void setEmpno(int empno) {
        this.empno = empno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student [empno=" + empno + ", name=" + name + ", gender=" + gender + ", age=" + age + "]";
    }


}
