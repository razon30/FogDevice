import static spark.Spark.*;

public class DataReceiverService {

    public static void main(String[] args) {
        // Define the port for your server
        int port = 4567;
        port(port);

        // Define an endpoint to receive data via POST request
        post("/receiveData", "application/json", (request, response) -> {
            // Handle the received data
            String data = request.body();
            System.out.println("Received data: " + data);

            // You can process the data further or save it to a database

            // processing and sending response back
            if (data.contains("Temperature")){
                return "Requires Accelerometer Data";
            }else {
                return "Data received successfully!";
            }
        });
    }
}

