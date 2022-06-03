package basic;

import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jclouds.domain.Credentials;
import org.jclouds.googlecloud.GoogleCredentialsFromJson;
import saaf.Inspector;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Supplier;


public class MetadataRetriever {
    public static final JsonObject jsonfile = readIni("credential.json");
    public static final String provider = jsonfile.get("provider").getAsString();
    public static final String identity = jsonfile.get("identity").getAsString();// Access Key ID
    public static final String credential =provider.contains("google")?getCredentialFromJsonKeyFile(jsonfile.get("credential").getAsString()):jsonfile.get("credential").getAsString();//Secret Access Key.
    public static final String containerName = jsonfile.get("containerName").getAsString();// bucket namespace
    public static final String objectName=jsonfile.get("objectName").getAsString();
    public static final String osname = System.getProperty("os.name");
    public static final boolean isMac = osname.contains("Mac");
    public static Gson gson = new Gson();

    // inspector
    public static void getMetrics(boolean isMac, Inspector inspector, int count, int actual_count, boolean connect, Long initializeConnectionTime) {
        inspector.addAttribute("initializeConnectionTime", initializeConnectionTime);// initialize connection Time for the storage connect
        inspector.addAttribute("connect",connect);// whether there is a connection
        inspector.addAttribute("actual_count",actual_count);
        inspector.addAttribute("count",count);
        if(connect) inspector.addAttribute("duration", new Date().getTime()- initializeConnectionTime);
        if (!isMac){
            inspector.inspectAllDeltas();
        }
    }
    public static void getResponse(HttpResponse httpResponse, Inspector inspector) throws IOException {
        BufferedWriter writer = httpResponse.getWriter();
//        getMetrics(isMac,inspector,count,actual_count,connect, initializeConnectionTime);
        String res = gson.toJson(inspector.finish());
        res = res.replace("\\n","_");
        res = res.replace("\n"," ");
        writer.write(res);
    }
    private static String getCredentialFromJsonKeyFile(String filename) {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(filename);
            assert is != null;
            String fileContents = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Supplier<Credentials> credentialSupplier = new GoogleCredentialsFromJson(fileContents);
            return credentialSupplier.get().credential;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static JsonObject readIni(String filename) {
        Gson gson = new Gson();
        String file="{}";
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(filename);
            assert is != null;
            file = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception){
            exception.printStackTrace();
        }
        return gson.fromJson(file, JsonObject.class);
    }
}
