package com.okay.testcenter.tools.webUi;

import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.os.ExecutableFinder;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkState;

@Log4j
@Service("MyPhantomJSDriverService")
public class MyPhantomJSDriverService {
    @Value("${logging.path}")
    private  String SYSTEM_LOG_FILE;
    @PostConstruct
    public void init() {
    }


    @Value("${logging.path}")
    private  String PHANTOMJS_DEFAULT_LOGFILE = SYSTEM_LOG_FILE + "/phantomjsdriver.log";

    private static final String PHANTOMJS_DEFAULT_EXECUTABLE = "phantomjs";

    private static final String PHANTOMJS_DOC_LINK = "https://github.com/ariya/phantomjs/wiki";
    private static final String PHANTOMJS_DOWNLOAD_LINK = "http://phantomjs.org/download.html";
    private static final String GHOSTDRIVER_DOC_LINK = "https://github.com/detro/ghostdriver/blob/master/README.md";
    private static final String GHOSTDRIVER_DOWNLOAD_LINK = "https://github.com/detro/ghostdriver/downloads";

    public static final String PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY = "phantomjs.ghostdriver.path";

    public PhantomJSDriverService createDefaultService(Capabilities desiredCapabilities) {
        // Look for Proxy configuration within the Capabilities
        Proxy proxy = null;
        if (desiredCapabilities != null) {
            proxy = Proxy.extractFrom(desiredCapabilities);
        }

        // Find PhantomJS executable
        File phantomjsfile = findPhantomJS(desiredCapabilities, PHANTOMJS_DOC_LINK, PHANTOMJS_DOWNLOAD_LINK);

        // Find GhostDriver main JavaScript file
        File ghostDriverfile = findGhostDriver(desiredCapabilities, GHOSTDRIVER_DOC_LINK, GHOSTDRIVER_DOWNLOAD_LINK);


        // Build & return service
        return new PhantomJSDriverService.Builder().usingPhantomJSExecutable(phantomjsfile)
                .usingGhostDriver(ghostDriverfile)
                .usingAnyFreePort()
                .withProxy(proxy)
                .withLogFile(new File(SYSTEM_LOG_FILE + "/phantomjsdriver.log"))
                .usingCommandLineArguments(
                        findCLIArgumentsFromCaps(desiredCapabilities, PhantomJSDriverService.PHANTOMJS_CLI_ARGS))
                .usingGhostDriverCommandLineArguments(
                        findCLIArgumentsFromCaps(desiredCapabilities, PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS))
                .build();
    }


    protected static File findPhantomJS(Capabilities desiredCapabilities, String docsLink,
                                        String downloadLink) {
        String phantomjspath;
        if (desiredCapabilities != null &&
                desiredCapabilities.getCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY) != null) {
            phantomjspath = (String) desiredCapabilities.getCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY);
        } else {
            phantomjspath = new ExecutableFinder().find(PHANTOMJS_DEFAULT_EXECUTABLE);
            phantomjspath = System.getProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjspath);
        }

        checkState(phantomjspath != null,
                "The path to the driver executable must be set by the %s capability/system property/PATH variable;"
                        + " for more information, see %s. "
                        + "The latest version can be downloaded from %s",
                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                docsLink,
                downloadLink);

        File phantomjs = new File(phantomjspath);
        /*checkExecutable(phantomjs);*/
        return phantomjs;
    }

    protected static void checkExecutable(File exe) {
        Preconditions.checkState(exe.exists(), "The driver executable does not exist: %s", exe.getAbsolutePath());
        Preconditions.checkState(!exe.isDirectory(), "The driver executable is a directory: %s", exe.getAbsolutePath());
        Preconditions.checkState(exe.canExecute(), "The driver is not executable: %s", exe.getAbsolutePath());
    }


    private static String[] findCLIArgumentsFromCaps(Capabilities desiredCapabilities, String capabilityName) {
        if (desiredCapabilities != null) {
            Object cap = desiredCapabilities.getCapability(capabilityName);
            if (cap != null) {
                if (cap instanceof String[]) {
                    return (String[]) cap;
                } else if (cap instanceof Collection) {
                    try {
                        @SuppressWarnings("unchecked")
                        Collection<String> capCollection = (Collection<String>) cap;
                        return capCollection.toArray(new String[capCollection.size()]);
                    } catch (Exception e) {
                        // If casting fails, log an error and assume no CLI arguments are provided
                        log.warn(String.format(
                                "Unable to set Capability '%s' as it was neither a String[] or a Collection<String>",
                                capabilityName));
                    }
                }
            }
        }
        return new String[]{};  // nothing found: return an empty array of arguments
    }


    protected static File findGhostDriver(Capabilities desiredCapabilities, String docsLink, String downloadLink) {
        // Recover path to GhostDriver from the System Properties or the Capabilities
        String ghostdriverpath;
        if (desiredCapabilities != null &&
                desiredCapabilities.getCapability(PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY) != null) {
            ghostdriverpath = (String) desiredCapabilities.getCapability(PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY);
        } else {
            ghostdriverpath = System.getProperty(PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY);
        }

        if (ghostdriverpath != null) {
            // Check few things on the file before returning it
            File ghostdriver = new File(ghostdriverpath);
            checkState(ghostdriver.exists(),
                    "The GhostDriver does not exist: %s",
                    ghostdriver.getAbsolutePath());
            checkState(ghostdriver.isFile(),
                    "The GhostDriver is a directory: %s",
                    ghostdriver.getAbsolutePath());
            checkState(ghostdriver.canRead(),
                    "The GhostDriver is not a readable file: %s",
                    ghostdriver.getAbsolutePath());

            return ghostdriver;
        }

        // This means that no GhostDriver System Property nor Capability was set
        return null;
    }
}
