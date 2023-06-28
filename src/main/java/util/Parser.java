package util;

import java.util.Map;

/**
 * This class contains the cli arguments parser
 */
public final class Parser {
    /**
     * @hidden
     */
    private Parser() {
    }

    /**
     * This method parse the arguments
     * @param args       arguments
     * @param parameters map of parameters to fill
     * @return true if there is an error, false otherwise
     */
    public static boolean argumentsParser(String[] args, Map<? super String, ? super String> parameters) {
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
