package com.lakshya.xkcdcomicreader;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.FileFsFile;
import org.robolectric.res.FsFile;

import java.io.File;

public class LibraryTestRunner extends RobolectricTestRunner {
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory by default. Use the {@link Config} annotation to configure.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public LibraryTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        AndroidManifest appManifest = super.getAppManifest(config);
        FsFile androidManifestFile = appManifest.getAndroidManifestFile();

        if (androidManifestFile.exists()) {
            return appManifest;
        } else {
            androidManifestFile = FileFsFile.from(getModuleRootPath(config), appManifest.getAndroidManifestFile().getPath().replace("manifests" + File.separator + "full", "manifests" + File.separator + "aapt").replace("\\src\\main",""));
            FsFile resourceDirectoryFile = FileFsFile.from(getModuleRootPath(config), appManifest.getResDirectory().getPath().replace("SDKLibrary\\", "SDKLibrary\\app\\"));
            AndroidManifest androidManifest = new AndroidManifest(androidManifestFile, resourceDirectoryFile, appManifest.getAssetsDirectory());
            String name = androidManifest.getPackageName();
            return androidManifest;
        }
    }

    private String getModuleRootPath(Config config) {
        String moduleRoot = config.constants().getResource("").toString().replace("file:", "");
        return moduleRoot.substring(0, moduleRoot.indexOf("/build"));
    }
}
