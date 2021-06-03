package com.bigdatacompany.spark;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

public class FirstExam {
    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir","C:\\hadoop-common-2.2.0-bin-master");
        JavaSparkContext javaSparkContext=new JavaSparkContext("local","First Exam Spark");

        //Data Load
        /*JavaRDD<String> firstData = javaSparkContext.textFile("C:\\Users\\talhaklc\\Desktop\\firstdata.txt");
        System.out.println(firstData.first());*/

        List<String> data = Arrays.asList("big data", "elasticsearch", "first", "spark", "hadoop");
        JavaRDD<String> secondData = javaSparkContext.parallelize(data);

        System.out.println("Toplam veri sayısı : "+ secondData.count());
        System.out.println("İlk veri : "+ secondData.first());
    }
}
