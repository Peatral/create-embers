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

        gogglesTooltip("ember_container", "Ember Container Info:");

        gui("none", "None");

        gogglesTooltip("boiler.steam_info", "Modified Boiler Stats:");
        gogglesTooltip("boiler.pressure", "%1$s - boiler pressure");
        gogglesTooltip("boiler.temperature", "%1$s - boiler temperature");
        gogglesTooltip("boiler.boiling_point", "%1$s - boiling point");

        CreateEmbers.registrate().addRawLang(CreateEmbers.ID + ".generic.unit.pressure", "bar");
        CreateEmbers.registrate().addRawLang(CreateEmbers.ID + ".generic.unit.temperature", "°C");

        CreateEmbers.registrate().addRawLang(CreateEmbers.ID + ".generic.unit.embers", "emb");
        CreateEmbers.registrate().addRawLang("itemGroup.createembers.main", "Create Embers");
    }

    public static void gogglesTooltip(String path, String enUs) {
        CreateEmbers.registrate().addRawLang(CreateEmbers.ID + ".gui.goggles." + path, enUs);
    }

    public static void gui(String path, String enUs) {
        CreateEmbers.registrate().addRawLang(CreateEmbers.ID + ".gui." + path, enUs);
    }
}
