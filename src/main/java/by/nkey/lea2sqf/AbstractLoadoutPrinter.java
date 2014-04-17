package by.nkey.lea2sqf;

import fr.soe.lea.domain.loadout.Loadout;
import fr.soe.lea.domain.object.DomainObject;
import fr.soe.lea.domain.object.Item;
import fr.soe.lea.domain.object.Magazine;
import fr.soe.lea.domain.object.Weapon;
import fr.soe.lea.domain.profile.Profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: comment
 *
 * @author Michail Nikolaev
 * @since <b>version 0.1</b> <i>created: 15.04.2014</i>
 */
public abstract class AbstractLoadoutPrinter {
    private Profile profile;

    public AbstractLoadoutPrinter(Profile profile) {
        this.profile = profile;
    }

    public String print() {
        StringBuilder builder = new StringBuilder();
        builder.append(prolog());
        Loadout loadout = profile.getLoadout();
        builder.append("removeAllWeapons ").append(unitId()).append(";\n");
        builder.append("removeAllAssignedItems ").append(unitId()).append(";\n");
        builder.append("removeAllItems ").append(unitId()).append(";\n");
        builder.append("removeHeadgear ").append(unitId()).append(";\n");
        builder.append("removeVest ").append(unitId()).append(";\n");

        removeBackpack(builder);
        if (!loadout.isKeepDefaultUniform()) {
            builder.append("removeUniform ").append(unitId()).append(";\n");
        }

        addWeapon(builder, loadout.getGoggles());
        addWeapon(builder, loadout.getGlasses());
        addWeapon(builder, loadout.getNvg());
        addWeapon(builder, loadout.getHelmet());

        linkItem(builder, loadout.getMap());
        linkItem(builder, loadout.getRadio());
        linkItem(builder, loadout.getRadio());
        linkItem(builder, loadout.getCompass());
        linkItem(builder, loadout.getGps());
        linkItem(builder, loadout.getWatch());

        if (!loadout.isKeepDefaultUniform()) {
            addUniform(builder, loadout.getUniform());
        }
        addVest(builder, loadout.getVest());

        addBackpack(builder, loadout.getBackpack());

        if (loadout.getLauncher() != null) {
            if (loadout.getBackpack() == null) {
                builder.append(unitId()).append(" addBackpack \"B_TacticalPack_blk\";\n");
                addMagazine(builder, loadout.getLauncherMagazine());
                addWeapon(builder, loadout.getLauncher());
                removeBackpack(builder);
            } else {
                addMagazine(builder, loadout.getLauncherMagazine());
                addWeapon(builder, loadout.getLauncher());
            }
            addSecondaryWeaponItem(builder, loadout.getLauncherLight());
            addSecondaryWeaponItem(builder, loadout.getLauncheroptic());
            addSecondaryWeaponItem(builder, loadout.getLauncherSilencer());
        }

        if (loadout.getRifle() != null) {
            addMagazine(builder, loadout.getRifleMagazine());
            addWeapon(builder, loadout.getRifle());
            addPrimaryWeaponItem(builder, loadout.getRifleLight());
            addPrimaryWeaponItem(builder, loadout.getRifleoptic());
            addPrimaryWeaponItem(builder, loadout.getRifleSilencer());
        }

        if (loadout.getPistolWeapon() != null) {
            addMagazine(builder, loadout.getPistolMagazine());
            addWeapon(builder, loadout.getPistolWeapon());
            addHandgunItem(builder, loadout.getPistolLight());
            addHandgunItem(builder, loadout.getPistolSilencer());
            addHandgunItem(builder, loadout.getPistoloptic());
        }

        if (loadout.getBackpack() != null) {
            if (loadout.getBackpack().getDomainObjects() != null) {
                Map<String, Integer> magazines = new HashMap<>();
                for (DomainObject item : loadout.getBackpack().getDomainObjects()) {
                    if (item instanceof Magazine) {
                        if (!magazines.containsKey(item.getClassname())) {
                            magazines.put(item.getClassname(), 0);
                        }
                        magazines.put(item.getClassname(), magazines.get(item.getClassname()) + 1);
                    } else if (item instanceof Item) {
                        builder.append("(backpackContainer ").append(unitId()).append(") addItemCargoGlobal [\"")
                                .append(item.getClassname()).append("\", 1];\n");
                    } else if (item instanceof Weapon) {
                        builder.append("(backpackContainer ").append(unitId()).append(") addWeaponCargoGlobal [\"")
                                .append(item.getClassname()).append("\", 1];\n");
                    }
                }
                for (Map.Entry<String, Integer> magAndNumber : magazines.entrySet()) {
                    builder.append("(backpackContainer ").append(unitId()).append(") addMagazineCargoGlobal [\"")
                            .append(magAndNumber.getKey()).append("\", ").append(magAndNumber.getValue())
                            .append("];\n");
                }
            }
        }


        if (loadout.getUniform() != null) {
            if (loadout.getUniform().getDomainObjects() != null) {
                processInventory(builder, loadout.getUniform().getDomainObjects());
            }
        }


        if (loadout.getVest() != null) {
            if (loadout.getVest().getDomainObjects() != null) {
                processInventory(builder, loadout.getVest().getDomainObjects());
            }
        }

        return builder.toString();
    }

    private void processInventory(StringBuilder builder, List<DomainObject> items) {
        Map<String, Integer> magazines = new HashMap<>();
        for (DomainObject item : items) {
            if (item instanceof Magazine) {
                if (!magazines.containsKey(item.getClassname())) {
                    magazines.put(item.getClassname(), 0);
                }
                magazines.put(item.getClassname(), magazines.get(item.getClassname()) + 1);
            } else if (item instanceof Item) {
                builder.append(unitId()).append("  addItem[\"").append(item.getClassname()).append("\", 1];\n");
            }
        }
        for (Map.Entry<String, Integer> magAndNumber : magazines.entrySet()) {
            builder.append(unitId()).append(" addMagazines[\"").append(magAndNumber.getKey()).append("\", ")
                    .append(magAndNumber.getValue()).append("];\n");
        }
    }

    private void addHandgunItem(StringBuilder builder, Item item) {
        addCommand(builder, item, "addHandgunItem");
    }

    private void addSecondaryWeaponItem(StringBuilder builder, Item item) {
        addCommand(builder, item, "addSecondaryWeaponItem");
    }

    private void addPrimaryWeaponItem(StringBuilder builder, Item item) {
        addCommand(builder, item, "addPrimaryWeaponItem");
    }

    private void addVest(StringBuilder builder, Item vest) {
        addCommand(builder, vest, "addVest");
    }

    private void addUniform(StringBuilder builder, Item uniform) {
        addCommand(builder, uniform, "addUniform");
    }

    private void addCommand(StringBuilder builder, DomainObject item, final String addCommand) {
        if (item != null) {
            builder.append(unitId()).append(" ").append(addCommand).append(" ").append("\"").append(item.getClassname())
                    .append("\"").append(";\n");
        }
    }


    private void addMagazine(StringBuilder builder, Magazine magazine) {
        addCommand(builder, magazine, "addMagazine");
    }

    private StringBuilder removeBackpack(StringBuilder builder) {
        return builder.append("removeBackpack ").append(unitId()).append(";\n");
    }

    private void addWeapon(StringBuilder builder, DomainObject item) {
        addCommand(builder, item, "addWeapon");
    }

    private void linkItem(StringBuilder builder, Item item) {
        addCommand(builder, item, "linkItem");
    }

    private void addBackpack(StringBuilder builder, Item item) {
        if (item != null) {
            addCommand(builder, item, "addBackpack");
            builder.append("clearMagazineCargoGlobal ").append("(backpackContainer ").append(unitId()).append(");\n");
            builder.append("clearItemCargoGlobal ").append("(backpackContainer ").append(unitId()).append(");\n");
            builder.append("clearWeaponCargoGlobal ").append("(backpackContainer ").append(unitId()).append(");\n");
        }
    }

    protected abstract String prolog();

    protected abstract String unitId();


}
