package view;

public class ResourceProvider {

    private static ResourceProvider instance;

    private static ResourceProvider getInstance(){
        if(instance==null)
            instance = new ResourceProvider();

        return instance;
    }
    public static String getResourcePath(){
        return getInstance().getClass().getResource("/").getPath();
    }
}
