package com.bigdatacompany.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.*;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public class SparkProductExam {
    public static void main(String[] args) throws AnalysisException {
        System.setProperty("hadoop.home.dir","C:\\hadoop-common-2.2.0-bin-master");

        StructType schema = new StructType().add("first_name", DataTypes.StringType)
                .add("last_name", DataTypes.StringType)
                .add("email", DataTypes.StringType)
                .add("country", DataTypes.StringType)
                .add("price", DataTypes.DoubleType)
                .add("product", DataTypes.StringType);

        SparkSession sparkSession = SparkSession.builder().master("local").appName("First Exam").getOrCreate();

        Dataset<Row> rawDS = sparkSession.read().schema(schema).option("multiline",true).json("C:\\Users\\talhaklc\\Desktop\\product.json");

        /*Dataset<Row> counPriceDS = rawDS.groupBy("country").avg("price");

        counPriceDS.show();*/

        rawDS.createOrReplaceTempView("product");

        Dataset<Row> sqlDS = sparkSession.sql("select first_name,email,country from product where country = 'France' or country = 'Brazil'");

        sqlDS.show();


    }
}
