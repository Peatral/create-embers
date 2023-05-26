package xyz.peatral.createembers.ponder;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import xyz.peatral.createembers.CEFluids;
import xyz.peatral.createembers.content.fluid_vessel.FluidVesselBlockEntity;

public class FluidVesselScenes {
    public static void fill_and_empty(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("fluid_vessel/fill_and_empty", "Fill and Empty");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.idle(5);

        Selection pump = util.select.position(3, 1, 1);
        BlockPos pumpPos = util.grid.at(3, 1, 1);

        Selection largeCog = util.select.position(5, 0, 1);
        Selection kinetics = util.select.fromTo(5, 1, 0, 3, 1, 0);
        Selection tank = util.select.fromTo(4, 1, 1, 4, 2, 1);
        Selection vessel = util.select.position(0, 1, 3);

        scene.world.showSection(tank, Direction.DOWN);
        FluidStack content = new FluidStack(CEFluids.MOLTEN_GOLD.get(), 16000);
        scene.world.modifyBlockEntity(util.grid.at(4, 1, 1), FluidTankBlockEntity.class, be -> be.getTankInventory()
                .fill(content, IFluidHandler.FluidAction.EXECUTE));
        scene.idle(5);
        scene.world.showSection(vessel, Direction.DOWN);
        scene.idle(10);

        for (int i = 0; i <= 5; i++) {
            scene.world.showSection(util.select.position(i == 0 ? 3 : i == 4 ? 1 : 2, 1, i == 0 ? 1 : i == 4 ? 3 : i), Direction.DOWN);
            scene.idle(3);
        }

        scene.overlay.showText(25)
                .text("Fluid Vessels function very similar to a Tank")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(0, 1, 3));

        scene.idle(30);

        scene.overlay.showText(70)
                .text("They can store Fluids pushed trough Pipes")
                .placeNearTarget()
                .pointAt(util.vector.topOf(0, 1, 3));
        scene.world.showSection(largeCog, Direction.UP);
        scene.world.showSection(kinetics, Direction.SOUTH);
        scene.idle(10);
        scene.world.setKineticSpeed(pump, 256);
        scene.world.setKineticSpeed(kinetics, -256);
        scene.world.setKineticSpeed(largeCog, 128);
        scene.world.propagatePipeChange(pumpPos);
        scene.idle(60);

        scene.overlay.showText(60)
                .text("They can actually store the amount of two Tanks")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(4, 2, 1));
        scene.idle(125);

        scene.world.hideSection(kinetics, Direction.NORTH);
        scene.world.hideSection(largeCog, Direction.DOWN);
        scene.idle(5);
        scene.world.hideSection(tank, Direction.UP);
        scene.idle(5);
        scene.world.hideSection(pump, Direction.UP);
        scene.idle(5);

        for (int i = 0; i <= 5; i++) {
            scene.world.hideSection(util.select.position(i == 0 ? 3 : i == 4 ? 1 : 2, 1, i == 0 ? 1 : i == 4 ? 3 : i), Direction.UP);
            scene.idle(3);
        }
        scene.idle(5);

        scene.overlay.showText(70)
                .text("In contrary to Tanks, Fluid Vessels can be filled and emptied with a bucket")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(0, 1, 3));
        scene.idle(40);

        scene.overlay.showControls(new InputWindowElement(util.vector.centerOf(0, 1, 3), Pointing.RIGHT).rightClick()
                .withItem(new ItemStack(Items.BUCKET)), 20);
        scene.idle(15);
        scene.world.modifyBlockEntity(util.grid.at(0, 1, 3), FluidVesselBlockEntity.class, be -> {
            IFluidHandler handler = be.getTankBehaviour().getCapability().orElse(null);
            if (handler == null)
                return;
            handler.drain(16000, IFluidHandler.FluidAction.EXECUTE);
        });
        scene.idle(60);
    }
}
