package lambda;

import com.google.cloud.functions.HttpRequest;
import com.google.gson.JsonObject;

import static basic.MetadataRetriever.gson;

public class Request {
    String objectName;
    String containerName;
    int count;
    public Request(){}
    public Request(String objectName){
        this.objectName = objectName;
    }
    public Request(int count){
        this.count = count;
    }
    public Request(String objectName, int count){
        this.objectName = objectName;
        this.count = count;
    }
    public Request(HttpRequest httpRequest) throws Exception {
        JsonObject body = gson.fromJson(httpRequest.getReader(),JsonObject.class);
        if (body.has("objectName")) this.objectName = body.get("objectName").getAsString(); // if objectName is defined
        if (body.has("containerName")) this.containerName = body.get("containerName").getAsString(); // if objectName is defined
        if (body.has("count")) this.count = body.get("count").getAsInt();
    }
    public void setObjectName(String objectName){
        this.objectName = objectName;
    }
    public void setCount(int count){
        this.count=count;
    }
    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public int getCount() {
        return this.count;
    }
    public String getObjectName() {
        return this.objectName;
    }
    public String getContainerName(){return this.containerName;}
}
