package xyz.peatral.createembers;

import com.simibubi.create.foundation.utility.Components;
import net.minecraft.network.chat.MutableComponent;

public class Lang {
    public static MutableComponent translateDirect(String key, Object... args) {
        return Components.translatable(CreateEmbers.ID + "." + key, com.simibubi.create.foundation.utility.Lang.resolveBuilders(args));
    }
}
