package xyz.peatral.createembers.datagen;

import com.simibubi.create.foundation.data.CreateRegistrate;
import xyz.peatral.createembers.CreateEmbers;

public class CELangGen {
    public static final CreateRegistrate REGISTRATE = CreateEmbers.registrate();

    public static void prepare() {
        gogglesTooltip("stamper", "Stamping Data:");
        gogglesTooltip("stamper.stamp", "Stamp Item: %s");
        gogglesTooltip("stamper.item", "Stamp Base Item: %s");
        gogglesTooltip("stamper.fluid", "Stamp Base Fluid: %s");
        gogglesTooltip("stamper.recipes", "Recipes:");
        gogglesTooltip("stamper.recipe", " - %s");
        gogglesTooltip("stamper.running", "Running");
        gogglesTooltip("stamper.runningticks", "Running Ticks: %d");

        gui("none", "None");

        jei("recipe.stamping", "Stamping");
    }

    public static void gogglesTooltip(String path, String enUs) {
        REGISTRATE.addRawLang(CreateEmbers.ID + ".gui.goggles." + path, enUs);
    }

    public static void gui(String path, String enUs) {
        REGISTRATE.addRawLang(CreateEmbers.ID + ".gui." + path, enUs);
    }
    public static void jei(String path, String enUs) {
        REGISTRATE.addRawLang(CreateEmbers.ID + ".jei." + path, enUs);
    }
}
