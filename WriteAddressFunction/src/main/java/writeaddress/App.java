package writeaddress;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * Receives address from read lambda function and
 * writes it unto a dynamo db
 */

public class App implements RequestHandler<Address, String> {

    private final AmazonDynamoDB dynamoDBClient;
    private final String tableName;

    public App() {
        dynamoDBClient = AmazonDynamoDBClientBuilder.defaultClient();
        tableName = "address-table";
    }

    /**
     * Handles the address received from the other
     * lambda function
     * @param address received
     * @param context object
     * @return string
     */
    @Override
    public String handleRequest(Address address, Context context) {

        //instantiates a dynamo db object
        DynamoDB dynamoDB = new DynamoDB(dynamoDBClient);

        //creates the item to be added
        Item item = new Item()
                .withPrimaryKey("id", address.getId())
                .withString("number", address.getNumber())
                .withString("street", address.getStreet())
                .withString("state", address.getState())
                .withString("country", address.getCountry());

        //puts the object into the dynamo db
        dynamoDB.getTable(tableName).putItem(item);

        return "Address written to DynamoDB: " + address;
    }
}
