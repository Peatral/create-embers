package xyz.peatral.createembers.crafting;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import xyz.peatral.createembers.CreateEmbers;

public class CERecipes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, CreateEmbers.ID);
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CreateEmbers.ID);



    public static final RegistryObject<RecipeType<StampingRecipe>> STAMPING = registerType("stamping");

    public static final RegistryObject<RecipeSerializer<StampingRecipe>> STAMPING_SERIAZLIZER =
            SERIALIZERS.register("stamping", StampingRecipe.Serializer::new);




    public static void register(IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
        SERIALIZERS.register(modEventBus);
    }

    static <T extends Recipe<?>> RegistryObject<RecipeType<T>> registerType(final String name) {
        return RECIPE_TYPES.register(name, () -> new RecipeType<T>() {
            public String toString() {
                return name;
            }
        });
    }
}
