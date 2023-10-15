import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertManyResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class DBHandler {

    public static class Temperature {
        private double temp;
        private long timeInMillis;

        public Temperature(double temp, int timeInMillis) {
            this.temp = temp;
            this.timeInMillis = timeInMillis;
        }

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public long getTimeInMillis() {
            return timeInMillis;
        }

        public void setTimeInMillis(long timeInMillis) {
            this.timeInMillis = timeInMillis;
        }
    }

    public static List<Temperature> recipes = Arrays.asList(
            new Temperature(25, 23456743),
            new Temperature(30, 23456354),
            new Temperature(20, 23456765),
            new Temperature(35, 23456276)
    );

    public static void main(String[] args) {

        // Replace the placeholder connection string below with your
        // Altas cluster specifics. Be sure it includes
        // a valid username and password! Note that in a production environment,
        // you do not want to store your password in plain-text here.
        ConnectionString mongoUri = new ConnectionString("<Replace this with your user string. Dont forget to repalce the password in the user string as well.>");

        // Provide the name of the database and collection you want to use.
        // If they don't already exist, the driver and Atlas will create them
        // automatically when you first write data.
        String dbName = "FleetlyData";
        String collectionName = "TempData";

        // a CodecRegistry tells the Driver how to move data between Java POJOs (Plain Old Java Objects) and MongoDB documents
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        // The MongoClient defines the connection to our MongoDB datastore instance (Atlas) using MongoClientSettings
        // You can create a MongoClientSettings with a Builder to configure codecRegistries, connection strings, and more
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(mongoUri).build();

        MongoClient mongoClient = null;
        try {
            mongoClient = MongoClients.create(settings);
        } catch (MongoException me) {
            System.err.println("Unable to connect to the MongoDB instance due to an error: " + me);
            System.exit(1);
        }

        // MongoDatabase defines a connection to a specific MongoDB database
        MongoDatabase database = mongoClient.getDatabase(dbName);
        // MongoCollection defines a connection to a specific collection of documents in a specific database
        MongoCollection<Temperature> collection = database.getCollection(collectionName, Temperature.class);

        /*      *** INSERT DOCUMENTS ***
         *
         * You can insert individual documents using collection.insert().
         * In this example, we're going to create 4 documents and then
         * insert them all in one call with insertMany().
         */

        try {
            // recipes is a static variable defined above
            InsertManyResult result = collection.insertMany(recipes);
            System.out.println("Inserted " + result.getInsertedIds().size() + " documents.\n");
        } catch (MongoException me) {
            System.err.println("Unable to insert any recipes into MongoDB due to an error: " + me);
            System.exit(1);
        }


    }
}
