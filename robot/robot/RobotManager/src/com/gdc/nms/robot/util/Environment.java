package com.gdc.nms.robot.util;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class Environment {



	 public static Path getTempPath() {
	        return Paths.get(System.getProperty(Constants.TEMP_DIR_PROPERTY));
	    }

	    public static Path getCurrentPath() {
	        Path path = getSelfJar();
	        if (Files.isRegularFile(path)) {
	            return path.getParent();
	        }
	        return path;
	    }

	    public static Path getJavaHome() {
	        return Paths.get(System.getProperty(Constants.JAVA_PATH_PROPERTY));
	    }

	    public static Path getJava() {
	        String name = "java";
	        if (isWindows()) {
	            name = "java.exe";
	        }
	        return getJavaHome().resolve("bin/" + name);
	    }

	    public static Path getSelfJar() {
	        try {
	            return Paths.get(Environment.class.getProtectionDomain().getCodeSource().getLocation().toURI());
	        } catch (URISyntaxException e) {
	            return null;
	        }
	    }

	public static String getOsName() {
		return System.getProperty(Constants.OS_NAME_PROPERTY, Constants.UNKNOWN_PROPERTY_VALUE);
	}

	public static Path getUserHome() {
		return Paths.get(System.getProperty(Constants.USER_HOME_PROPERTY));
	}

	public static Path getInstallationCachePath() {
		return getUserHome().resolve(Constants.INTALLER_REGISTRY_FOLDER);
	}
	


	public static boolean isWindows() {
		return getOsName().toLowerCase().contains("win");
	}

	public static boolean isUnix() {
		String osName = getOsName().toLowerCase();
		return osName.contains("nux") || osName.contains("nix");
	}

	private Environment() {
	}
}
