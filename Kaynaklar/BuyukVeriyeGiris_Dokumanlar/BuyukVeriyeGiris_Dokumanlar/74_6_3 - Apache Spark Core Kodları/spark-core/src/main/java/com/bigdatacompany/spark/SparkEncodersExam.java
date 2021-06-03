package com.bigdatacompany.spark;

import com.bigdatacompany.spark.model.Product;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public class SparkEncodersExam {
    public static void main(String[] args) throws AnalysisException {
        System.setProperty("hadoop.home.dir","C:\\hadoop-common-2.2.0-bin-master");

        SparkSession sparkSession = SparkSession.builder().master("local").appName("First Exam").getOrCreate();

        Encoder<Product> beanProduct = Encoders.bean(Product.class);

        Dataset<Product> productDS = sparkSession.read().option("multiline", true).json("C:\\Users\\talhaklc\\Desktop\\product.json").as(beanProduct);

        productDS.foreach(pr -> {
            System.out.println(pr.getFirst_name()+" "+pr.getLast_name());
        });

    }
}
