package lambda;

import com.amazonaws.services.lambda.runtime.*;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import saaf.Inspector;

import java.util.*;


import static basic.MetadataRetriever.*;
import static basic.ObjectStorage.*;

public class ReadObject implements RequestHandler<Request, HashMap<String, Object>>, HttpFunction {
//    static BlobStoreContext blobContext;
    static BlobStore blobStore;
    static Long initializeConnectionTime;
    public void procedure(Request request,Inspector inspector) {
        //*******************collect initial data
        if (!isMac){
            inspector.inspectAll();
        }
        // Get values from request
        boolean connect = false;
        int count = 2;
        String myObjectName = objectName;
        String myContainerName = containerName;
        if (request!=null&&request.getObjectName()!=null) myObjectName = request.getObjectName();
        if (request!=null&&request.getContainerName()!=null) myContainerName = request.getContainerName();
        if (request!=null && request.getCount()>0) count = request.getCount();
        int actual_count = count;
        // Initialize Jclouds
        if (blobStore==null){
            BlobStoreContext blobContext = ContextBuilder.newBuilder(provider)
                    .credentials(identity, credential)
                    .buildApi(BlobStoreContext.class);
            blobStore = blobContext.getBlobStore();
            initializeConnectionTime = new Date().getTime();
            System.out.println("Start initializing");
        }
        //****************START FUNCTION IMPLEMENTATION*************************
        try {
            readBlob(blobStore,myContainerName,myObjectName);
            connect=true;
        }catch (Exception e){
            inspector.addAttribute("duration", new Date().getTime()-initializeConnectionTime);
            e.printStackTrace();
        }
        //***************Get Result**************8
        getMetrics(isMac, inspector, count, actual_count, connect, initializeConnectionTime);

    }
    // AWS Handler
    @Override
    public HashMap<String, Object> handleRequest(Request request, Context context) {
        Inspector inspector = new Inspector();
        procedure(request,inspector);
        return inspector.finish();
    }

    // Google Handler
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
        Inspector inspector = new Inspector();
        procedure(new Request(httpRequest),inspector);
        getResponse(httpResponse,inspector);
    }

    public static void main(String[] args) {
        ReadObject readObject = new ReadObject();
        System.out.println(readObject.handleRequest(new Request("1000 Sales Records.csv"),null));
    }
}
