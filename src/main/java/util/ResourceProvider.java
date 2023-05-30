package util;

public class ResourceProvider {
    private static ResourceProvider instance;

    private ResourceProvider() {
        super();
    }


    public static String getResourcePath(String resPath) {
        return ResourceProvider.class.getResource(resPath).getPath();
    }
}
