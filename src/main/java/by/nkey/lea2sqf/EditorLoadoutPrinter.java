package by.nkey.lea2sqf;

import fr.soe.lea.domain.profile.Profile;

/**
 * TODO: comment
 *
 * @author Michail Nikolaev
 * @since <b>version 0.1</b> <i>created: 15.04.2014</i>
 */
public class EditorLoadoutPrinter extends AbstractLoadoutPrinter{
    public EditorLoadoutPrinter(Profile profile) {
        super(profile);
    }

    @Override
    protected String prolog() {
        return "";
    }

    @Override
    protected String unitId() {
        return "player";
    }
}
