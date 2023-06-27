package util;

import java.util.Map;

public class Parser {
    private Parser() {
    }
    public static boolean argumentsParser(String[] args, Map<String, String> parameters) {
        if (args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("-")) {
                    if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                        parameters.put(args[i], args[i + 1]);
                        i++;
                    } else {
                        parameters.put(args[i], args[i]);
                    }
                } else {
                    System.err.println("Parameter \"" + args[i] + "\" has no tag");
                    return true;
                }
            }
        }
        return false;
    }
}
