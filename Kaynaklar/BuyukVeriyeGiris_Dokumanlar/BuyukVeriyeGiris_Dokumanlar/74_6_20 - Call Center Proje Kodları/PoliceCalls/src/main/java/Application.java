import com.mongodb.spark.MongoSpark;
import javafx.scene.chart.PieChart;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.*;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

public class Application {
    public static void main(String[] args) {

        System.setProperty("hadoop.home.dir","C:\\hadoop-common-2.2.0-bin-master");

        StructType schema = new StructType()
                .add("recordid", DataTypes.IntegerType)
                .add("calldatetime", DataTypes.StringType)
                .add("priority", DataTypes.StringType)
                .add("district", DataTypes.StringType)
                .add("description", DataTypes.StringType)
                .add("callnumber", DataTypes.StringType)
                .add("incidentlocation", DataTypes.StringType)
                .add("location", DataTypes.StringType);

        SparkSession sparkSession = SparkSession.
                builder().master("local")
                .config("spark.mongodb.output.uri", "mongodb://127.0.0.1/police.callcenter")
                .appName("Police Call Service").getOrCreate();

        Dataset<Row> rawData = sparkSession.read().option("header",true).schema(schema).csv("C:\\police911.csv");

        Dataset<Row> data = rawData.filter(rawData.col("recordid").isNotNull());

        //data.groupBy("incidentlocation").count().sort(functions.desc("count")).show();

        Dataset<Row> descriptionDS = data.filter(data.col("description").notEqual("911/NO  VOICE"));
        Dataset<Row> resultDS = descriptionDS.groupBy("incidentlocation", "description").count().sort(functions.desc("count"));

        //MongoSpark.write(resultDS).mode("overwrite").save();

        resultDS.write().option("path","hdfs://localhost:8020/data/test").
                saveAsTable("default.test2");

    }
}
