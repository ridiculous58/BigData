package com.bigdatacompany.mongodbent;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

public class Application {
    public static void main(String[] args) {
        MongoClient client=new MongoClient("localhost",27017);
        MongoDatabase infoDB = client.getDatabase("Info");
        MongoCollection<Document> personalCollection = infoDB.getCollection("Personal");

        BasicDBObject data=new BasicDBObject()
                .append("name","Thomas Edison")
                .append("date",1847)
                .append("country","USA");

        BasicDBObject data2=new BasicDBObject()
                .append("name","Elon Musk")
                .append("date",1971)
                .append("country","Africa")
                .append("job","entrepreneur")
                ;

        //personalCollection.insertOne(Document.parse(data.toJson()));
        /*Document parse = Document.parse(data.toJson());
        Document parse2 = Document.parse(data2.toJson());

        personalCollection.insertMany(Arrays.asList(parse,parse2));*/

        /*FindIterable<Document> documents = personalCollection.find(new BasicDBObject("date",1942));

        for (Document doc:documents){
            System.out.println(doc.toJson());
        }*/

        /*Bson filter = Filters.exists("job");
        Bson update = Updates.set("child", "Nevada Must");
        personalCollection.updateOne(filter,update);*/

       /* Bson deleteFilter = Filters.eq("country", "England");
        personalCollection.deleteMany(deleteFilter);*/

       // personalCollection.drop();

        infoDB.drop();
    }
}
