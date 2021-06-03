import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationDiyabes {
    public static void main(String[] args) {

        SparkSession sparkSession=SparkSession.builder().master("local").appName("spark-mllib-naive-bayes").getOrCreate();

        Dataset<Row> loadData = sparkSession.read().format("csv")
                .option("header", "true")
                .option("inferSchema", "true")
                .load("C:\\Users\\talhaklc\\Desktop\\diabetes.csv");
        String[] vowels = {"Pregnancies","Glucose","BloodPressure","SkinThickness","Insulin","BMI","DiabetesPedigreeFunction","Age","Outcome"};

        List<String> headers = Arrays.asList(vowels);
        for(String h:headers){
            if(h.equals("Outcome")){
                StringIndexer tmpIndex = new StringIndexer().setInputCol(h).setOutputCol("label");
                loadData = tmpIndex.fit(loadData).transform(loadData);
            }
            else {
                StringIndexer tmpIndex = new StringIndexer().setInputCol(h).setOutputCol(h.toLowerCase() + "_cat");
                loadData = tmpIndex.fit(loadData).transform(loadData);
            }
        }


        VectorAssembler vectorAssembler=new VectorAssembler().setInputCols(toLowerList(headers))
                                                                .setOutputCol("features");
        Dataset<Row> transform = vectorAssembler.transform(loadData);

        Dataset<Row> final_data = transform.select("label", "features");

        Dataset<Row>[] datasets = final_data.randomSplit(new double[]{0.7, 0.3});
        Dataset<Row> train_data = datasets[0];
        Dataset<Row> test_data = datasets[1];

        NaiveBayes
                nb=new NaiveBayes();
        nb.setSmoothing(1);
        NaiveBayesModel model = nb.fit(train_data);
        Dataset<Row> predictions = model.transform(test_data);

        MulticlassClassificationEvaluator evaluator= new MulticlassClassificationEvaluator()
                .setLabelCol("label")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        double evaluate = evaluator.evaluate(predictions);

        System.out.println("Accuracy = "+evaluate);
    }

    public static String[] toLowerList(List<String> data){
        List<String> res=new ArrayList<String>();
        for(String d:data){
            if(d.equals("Outcome")){
                res.add("label");
            }
            else{
            res.add(d.toLowerCase()+"_cat");
            }
        }
        String[] array = res.toArray(new String[res.size()]);
        return array;
    }
}
