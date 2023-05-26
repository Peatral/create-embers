package xyz.peatral.createembers.datagen;

import xyz.peatral.createembers.CreateEmbers;

public class CELangGen {

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
    }

    public static void gogglesTooltip(String path, String enUs) {
        CreateEmbers.registrate().addRawLang(CreateEmbers.ID + ".gui.goggles." + path, enUs);
    }

    public static void gui(String path, String enUs) {
        CreateEmbers.registrate().addRawLang(CreateEmbers.ID + ".gui." + path, enUs);
    }
}
