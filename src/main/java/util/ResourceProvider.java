package util;

public class ResourceProvider {
    private static ResourceProvider instance;

    private synchronized static ResourceProvider getInstance(){
        if(instance==null)
            instance = new ResourceProvider();

        return instance;
    }
    public static String getResourcePath(String resPath){
        return getInstance().getClass().getResource(resPath).getPath();
    }
}
