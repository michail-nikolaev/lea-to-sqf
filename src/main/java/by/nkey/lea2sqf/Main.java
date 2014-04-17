package by.nkey.lea2sqf;

import fr.soe.lea.dao.profile.ProfileDAO;
import fr.soe.lea.domain.profile.Profile;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        ProfileDAO profileDAO = new ProfileDAO();
        Profile profile = profileDAO.loadProfile(new File("AFF_SPEC.lea.profile"));
        System.out.println(new TestLoadoutPrinter(profile).print());
    }
}
