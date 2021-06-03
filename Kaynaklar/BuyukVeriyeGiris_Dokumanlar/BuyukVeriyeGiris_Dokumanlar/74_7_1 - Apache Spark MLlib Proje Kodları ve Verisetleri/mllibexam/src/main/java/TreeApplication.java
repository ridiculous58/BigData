import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.ml.regression.LinearRegressionTrainingSummary;
import org.apache.spark.mllib.tree.DecisionTree;
import org.apache.spark.mllib.tree.model.DecisionTreeModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Map;

public class TreeApplication {
    public static void main(String[] args) {
        SparkSession sparkSession=SparkSession.builder().appName("spark-mllib").master("local").getOrCreate();

        Dataset<Row> raw_data = sparkSession.read().format("csv")
                .option("header", "true")
                .option("inferSchema", "true")
                .load("C:\\Users\\talhaklc\\Desktop\\category.csv");

        StringIndexer stringIndexer = new StringIndexer().setInputCol("displayName").setOutputCol("display_cat");

        StringIndexer stringIndexer2 = new StringIndexer().setInputCol("labelCat").setOutputCol("label");

        Dataset<Row> transform_data = stringIndexer.fit(raw_data).transform(raw_data);
        Dataset<Row> transform_data2 = stringIndexer2.fit(transform_data).transform(transform_data);

        VectorAssembler features_vector = new VectorAssembler().setInputCols(new String[]{"display_cat","label"})
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
