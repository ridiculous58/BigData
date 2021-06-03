import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class BasketbolApplication {
    public static void main(String[] args) {
        SparkSession sparkSession=SparkSession.builder().appName("spark-mllib").master("local").getOrCreate();

        Dataset<Row> raw_data = sparkSession.read().format("csv")
                .option("header", "true")
                .option("inferSchema", "true")
                .load("C:\\Users\\talhaklc\\Desktop\\basketbol.csv");

        StringIndexer stringHava = new StringIndexer().setInputCol("hava").setOutputCol("hava_cat");
        StringIndexer stringSicaklik = new StringIndexer().setInputCol("sicaklik").setOutputCol("sicaklik_cat");
        StringIndexer stringNem = new StringIndexer().setInputCol("nem").setOutputCol("nem_cat");
        StringIndexer stringRuzgar = new StringIndexer().setInputCol("ruzgar").setOutputCol("ruzgarcat");

        StringIndexer stringBasketbol = new StringIndexer().setInputCol("basketbol").setOutputCol("label");

        Dataset<Row> transform_data = stringHava.fit(raw_data).transform(raw_data);
        Dataset<Row> transform_data3 = stringSicaklik.fit(transform_data).transform(transform_data);
        Dataset<Row> transform_data4 = stringNem.fit(transform_data3).transform(transform_data3);
        Dataset<Row> transform_data5 = stringRuzgar.fit(transform_data4).transform(transform_data4);
        Dataset<Row> transform_data2 = stringBasketbol.fit(transform_data5).transform(transform_data5);

        VectorAssembler features_vector = new VectorAssembler().setInputCols(new String[]{"hava_cat","sicaklik_cat"
                ,"nem_cat"
                ,"ruzgarcat","label"})
                .setOutputCol("features");

        Dataset<Row> transform = features_vector.transform(transform_data2);
        Dataset<Row> final_data = transform.select("features", "label");


        final_data.show();

        Dataset<Row>[] datasets = final_data.randomSplit(new double[]{0.7, 0.3});
        Dataset<Row> train_data = datasets[0];
        Dataset<Row> test_data = datasets[1];

        System.out.println(train_data.count());
        System.out.println(test_data.count());

        NaiveBayes nb = new NaiveBayes();
        nb.setSmoothing(1);
        //nb.setModelType("multinomial");
        NaiveBayesModel model = nb.fit(train_data);
        Dataset<Row> predictions = model.transform(test_data);

        predictions.show();

        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("label")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        double accuracy = evaluator.evaluate(predictions);
        System.out.println("Test set accuracy = " + accuracy);
    }
}
