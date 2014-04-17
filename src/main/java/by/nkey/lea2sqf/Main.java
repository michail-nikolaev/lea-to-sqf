package by.nkey.lea2sqf;

import fr.soe.lea.dao.profile.ProfileDAO;
import fr.soe.lea.domain.profile.Profile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static final String PROFILES_LOCATION = "profiles/units";
    public static final String PROFILES_CONVERTED_ZEALOT = "profiles/units/zealot";
    public static final String PROFILES_CONVERTED_BG3 = "profiles/units/bg3";
    public static final String PROFILES_CONVERTED_EDITOR = "profiles/units/editor";

    public static void main(String[] args) throws IOException {
        ProfileDAO profileDAO = new ProfileDAO();


        Files.createDirectories(Paths.get(PROFILES_CONVERTED_ZEALOT));
        Files.createDirectories(Paths.get(PROFILES_CONVERTED_BG3));
        Files.createDirectories(Paths.get(PROFILES_CONVERTED_EDITOR));

        for (Path path : Files.newDirectoryStream(Paths.get(PROFILES_LOCATION))) {
            Profile profile = profileDAO.loadProfile(path.toFile());
            System.out.println("Converting " + profile.getProfileName());
            Files.write(Paths.get(PROFILES_CONVERTED_ZEALOT, path.toFile().getName() + ".sqf"),
                    new ZealotLoadoutPrinter(profile).print().getBytes());
            Files.write(Paths.get(PROFILES_CONVERTED_BG3, path.toFile().getName() + ".sqf"),
                    new BG3LoadoutPrinter(profile).print().getBytes());
            Files.write(Paths.get(PROFILES_CONVERTED_EDITOR, path.toFile().getName() + ".sqf"),
                    new EditorLoadoutPrinter(profile).print().getBytes());
        }

        System.out.print("Done");
        System.in.read();
    }
}
