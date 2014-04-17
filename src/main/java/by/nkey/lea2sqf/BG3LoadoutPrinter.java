package by.nkey.lea2sqf;

import fr.soe.lea.domain.profile.Profile;

/**
 * TODO: comment
 *
 * @author Michail Nikolaev
 * @since <b>version 0.1</b> <i>created: 15.04.2014</i>
 */
public class BG3LoadoutPrinter extends AbstractLoadoutPrinter {
    public BG3LoadoutPrinter(Profile profile) {
        super(profile);
    }

    @Override
    protected String prolog() {
        return "_unit = _this select 0;\n";

    }

    @Override
    protected String unitId() {
        return "_unit";
    }

}