package by.nkey.lea2sqf;

import fr.soe.lea.dao.profile.ProfileDAO;
import fr.soe.lea.domain.profile.Profile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static final String PROFILES_LOCATION = "profiles/units";
    public static final String PROFILES_CONVERTED_ZEALOT = "profiles/converted/zealot";
    public static final String PROFILES_CONVERTED_BG3 = "profiles/converted/bg3";
    public static final String PROFILES_CONVERTED_EDITOR = "profiles/converted/editor";

    public static void main(String[] args) throws IOException {
        ProfileDAO profileDAO = new ProfileDAO();


        Files.createDirectories(Paths.get(PROFILES_CONVERTED_ZEALOT));
        Files.createDirectories(Paths.get(PROFILES_CONVERTED_BG3));
        Files.createDirectories(Paths.get(PROFILES_CONVERTED_EDITOR));

        Path directory = Paths.get(PROFILES_LOCATION);

        processDirectory(profileDAO, directory);

        System.out.print("Done");
    }

    private static void processDirectory(ProfileDAO profileDAO, Path directory) throws IOException {
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(directory)) {
            for (Path path : paths) {
                if (path.toAbsolutePath().toString().endsWith("lea.profile")) {
                    Profile profile = profileDAO.loadProfile(path.toFile());
                    System.out.println("Converting " + profile.getProfileName());
                    writeProfile(Paths.get(PROFILES_CONVERTED_ZEALOT,
                            Paths.get(PROFILES_LOCATION).relativize(directory).toString(),
                            profile.getProfileName() + ".sqf"), new ZealotLoadoutPrinter(profile).print().getBytes());
                    writeProfile(Paths.get(PROFILES_CONVERTED_BG3,
                            Paths.get(PROFILES_LOCATION).relativize(directory).toString(),
                            profile.getProfileName() + ".sqf"), new BG3LoadoutPrinter(profile).print().getBytes());
                    writeProfile(Paths.get(PROFILES_CONVERTED_EDITOR,
                            Paths.get(PROFILES_LOCATION).relativize(directory).toString(),
                            profile.getProfileName() + ".sqf"), new EditorLoadoutPrinter(profile).print().getBytes());
                } else if ((!path.toString().equals(".")) && path.toFile().isDirectory()) {
                    processDirectory(profileDAO, path);
                }
            }
        }
    }

    private static void writeProfile(Path path, byte[] bytes) throws IOException {
        path.getParent().toFile().mkdirs();
        Files.write(path, bytes);
    }
}
