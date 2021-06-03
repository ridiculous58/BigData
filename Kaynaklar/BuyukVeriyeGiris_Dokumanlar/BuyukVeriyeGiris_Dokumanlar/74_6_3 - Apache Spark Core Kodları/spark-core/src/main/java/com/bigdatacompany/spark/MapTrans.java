package com.bigdatacompany.spark;

import com.bigdatacompany.spark.model.Person;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MapTrans {
    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir","C:\\hadoop-common-2.2.0-bin-master");
        JavaSparkContext javaSparkContext=new JavaSparkContext("local","Map Transformation Spark");

        JavaRDD<String> rawdata = javaSparkContext.textFile("C:\\Users\\talhaklc\\Desktop\\person.csv");

        /*Distinct Kullanımı
        System.out.println(rawdata.count());
        JavaRDD<String> distData = rawdata.distinct();
        System.out.println(distData.count());*/


        /*Flatmap Kullanımı
        JavaRDD<String> stringJavaRDD = rawdata.flatMap(new FlatMapFunction<String, String>() {
            public Iterator<String> call(String s) throws Exception {
                return Arrays.asList(s.split(",")).iterator();
            }
        });
        System.out.println(stringJavaRDD.count());*/


        //Map Kullanımı
        JavaRDD<Person> loadPerson = rawdata.map(new Function<String, Person>() {
            public Person call(String line) throws Exception {
                String[] data = line.split(",");
                Person p = new Person();
                p.setFirst_name(data[0]);
                p.setLast_name(data[1]);
                p.setEmail(data[2]);
                p.setGender(data[3]);
                p.setCountry(data[4]);
                p.setAge(Integer.parseInt(data[5]));
                return p;
            }
        });

        JavaPairRDD<String, String> pairRdd = loadPerson.mapToPair(new PairFunction<Person, String, String>() {
            public Tuple2<String, String> call(Person person) throws Exception {
                return new Tuple2<String, String>(person.getFirst_name(), person.getLast_name());
            }
        });
        pairRdd.saveAsTextFile("C:\\result\\");
       /* pairRdd.foreach(new VoidFunction<Tuple2<String, String>>() {
            public void call(Tuple2<String, String> data) throws Exception {
                System.out.println("Key : " + data._1+" -- Value : "+data._2);
            }
        });
       /* JavaPairRDD<String, Person> pairRdd = loadPerson.mapToPair(new PairFunction<Person, String, Person>() {
            public Tuple2<String, Person> call(Person person) throws Exception {
                return new Tuple2<String, Person>(person.getCountry(), person);
            }
        });

        JavaPairRDD<String, Iterable<Person>> groupedData = pairRdd.groupByKey();

        groupedData.foreach(new VoidFunction<Tuple2<String, Iterable<Person>>>() {
            public void call(Tuple2<String, Iterable<Person>> data) throws Exception {
                System.out.println("Key : "+data._1+" Count : "+ Iterables.size(data._2));
            }
        });*/
        /* Foreach ile ekrana yazdırma
        loadPerson.foreach(new VoidFunction<Person>() {
            public void call(Person person) throws Exception {
                System.out.println("Adı : "+ person.getFirst_name()+" Soyadı: "+person.getLast_name());
            }
        });*/

        // Filter Kullanımı
        /*JavaRDD<Person> personFromCanada = loadPerson.filter(new Function<Person, Boolean>() {
            public Boolean call(Person person) throws Exception {
                return person.getAge() > 35;
            }
        });*/

        /*List<Person> collectList = loadPerson.collect();

        for(Person p : collectList)
            System.out.println(p.getFirst_name()+" "+p.getLast_name());*/

       /* List<Person> take = loadPerson.take(5);
        for(Person p : take)
            System.out.println(p.getFirst_name()+" "+p.getLast_name());*/




    }
}
