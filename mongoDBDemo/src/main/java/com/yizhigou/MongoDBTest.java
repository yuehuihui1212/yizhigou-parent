package com.yizhigou;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * @Author: yuehuihui
 * @Description:
 * @Date: created in 13:27 2018/6/2
 * @Modified By:
 **/
public class MongoDBTest {

    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase mongoDatabase = client.getDatabase("jiyun");
        MongoCollection<Document> student = mongoDatabase.getCollection("student");
        FindIterable<Document> documents = student.find();
        for (Document d : documents) {
            System.out.println("name:"+d.getString("name"));
            System.out.println("age:"+d.getDouble("age"));
            System.out.println("address:"+d.getString("address"));
            System.out.println("===========================");

        }
    }
}
