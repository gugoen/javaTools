package com.guo.tools.testClass;

public class TempClassify {
    private int id;
    private int min_temp;
    private int max_temp;
    private String classify;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", min_temp=" + min_temp +
                ", max_temp=" + max_temp +
                ", classity='" + classify + '\'' +
                '}';
    }
}
