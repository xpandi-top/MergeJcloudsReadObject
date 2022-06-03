package basic;

import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.domain.Blob;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ObjectStorage {

    // read from an object and print the number of lines
    public static void readBlob(BlobStore blobStore, String containerName, String objectName) throws Exception  {
        Blob downloadBlob = blobStore.getBlob(containerName, objectName);
        InputStream inputStream = downloadBlob.getPayload().openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)) ;
        System.out.println(reader.lines().count());
    }

//    // read from an object and return the content stream
//    public static InputStream readBlobContent(BlobStore blobStore, String containerName, String objectName) throws Exception {
//        Blob downloadBlob = blobStore.getBlob(containerName, objectName);
//        return downloadBlob.getPayload().openStream();
//    }
//
//    // create an object with key-value
//    public static void writeBlob(BlobStore blobStore, String containerName, String objectName, String content) throws Exception {
//        ByteSource payload = ByteSource.wrap(content.getBytes(UTF_8));
//        // Add Blob
//        Blob blob = blobStore.blobBuilder(objectName)
//                .payload(payload)
//                .contentLength(payload.size())
//                .build();
//        blobStore.putBlob(containerName, blob);
//    }
//
//    // create a container
//    public static void createContainer(BlobStore blobStore, String containerName) {
//        boolean exist = blobStore.containerExists(containerName);
//        if (!exist){
//            blobStore.createContainerInLocation(null,containerName);
//            System.out.println("Deleted");
//        } else {
//            System.out.println("No bucket name "+containerName);
//        }
//    }
//
//    //delete a container
//    public static void deleteContainer(BlobStore blobStore, String containerName) {
//        boolean exist = blobStore.containerExists(containerName);
//        if (exist){
//            blobStore.deleteContainer(containerName);
//            System.out.println("Deleted");
//        } else {
//            System.out.println("No bucket name "+containerName);
//        }
//    }
//    // delete a object by objectname
//    public static void deleteBlob(BlobStore blobStore, String containerName, String objectName) {
//        boolean exist = blobStore.blobExists(containerName,objectName);
//        if (exist){
//            blobStore.removeBlob(containerName,objectName);
//            System.out.println("Deleted");
//        } else {
//            System.out.println("No bucket name "+containerName);
//        }
//    }
}
