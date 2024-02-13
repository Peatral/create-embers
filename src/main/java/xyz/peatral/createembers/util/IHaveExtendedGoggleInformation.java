package xyz.peatral.createembers.util;

import com.rekindled.embers.api.power.IEmberCapability;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.LazyOptional;
import xyz.peatral.createembers.CreateEmbers;

import java.util.List;
import java.util.Optional;

public interface IHaveExtendedGoggleInformation extends IHaveGoggleInformation {
    default boolean containedEmbersTooltip(List<Component> tooltip, boolean isPlayerSneaking,
                                          LazyOptional<IEmberCapability> handler) {
        Optional<IEmberCapability> resolve = handler.resolve();
        if (!resolve.isPresent())
            return false;

        IEmberCapability emberContainer = resolve.get();

        LangBuilder embers = new LangBuilder(CreateEmbers.ID).translate("generic.unit.embers");
        new LangBuilder(CreateEmbers.ID).translate("gui.goggles.ember_container")
                .forGoggles(tooltip);

        double embersAmount = emberContainer.getEmber();

        Lang.builder()
                .add(Lang.number(embersAmount)
                        .add(embers)
                        .style(ChatFormatting.GOLD))
                .text(ChatFormatting.GRAY, " / ")
                .add(Lang.number(emberContainer.getEmberCapacity())
                        .add(embers)
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);


        return true;
    }
}
