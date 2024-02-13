package xyz.peatral.createembers;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.LangNumberFormat;
import net.minecraft.network.chat.MutableComponent;

public class CELang {
    public static LangBuilder builder() {
        return new LangBuilder(CreateEmbers.ID);
    }

    public static LangBuilder translate(String langKey, Object... args) {
        return builder().translate(langKey, args);
    }

    public static LangBuilder text(String text) {
        return builder().text(text);
    }

    public static MutableComponent translateDirect(String key, Object... args) {
        return Components.translatable(CreateEmbers.ID + "." + key, Lang.resolveBuilders(args));
    }
}
