package maze.frequency.client;

import java.util.ArrayList;
import java.util.List;

import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBox;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import maze.frequency.FrequencyMod;
import maze.frequency.block.LogicCombinatorBehaviour;
import maze.frequency.block.LogicCombinatorBlockEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EventBusSubscriber(modid = FrequencyMod.MODID, value = Dist.CLIENT)
public class LogicCombinatorClientHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger("Frequency/Handler");

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        HitResult target = mc.hitResult;
        if (target == null || !(target instanceof BlockHitResult result)) return;

        BlockPos pos = result.getBlockPos();

        if (!(mc.level.getBlockEntity(pos) instanceof LogicCombinatorBlockEntity blockEntity)) return;

        LogicCombinatorBehaviour behaviour = blockEntity.getLogicBehaviour();
        if (behaviour == null) {
            LOGGER.warn("[LogicHandler] behaviour is null at {}", pos);
            return;
        }


        for (int i = 0; i < 6; i++) {
            ValueBoxTransform transform = behaviour.getSlotTransform(i);
            Vec3 hitLoc = target.getLocation();
            boolean hit = behaviour.testHit(i, hitLoc);


            boolean empty = behaviour.isSlotEmpty(i);
            Component label = behaviour.getSlotLabel(i);

            ValueBox box = new ValueBox(label, new AABB(Vec3.ZERO, Vec3.ZERO).inflate(.25f), pos, blockEntity.getBlockState());
            box.passive(!hit);
            if (!empty) box.wideOutline();

            Pair<Object, BlockPos> key = Pair.of(i, pos);
            Outliner.getInstance()
                .showOutline(key, box.transform(transform))
                .highlightFace(result.getDirection());

            if (hit) {
                List<MutableComponent> tip = new ArrayList<>();
                tip.add(label.copy());
                tip.add(Component.translatable(empty ? "frequency.tooltip.click_to_set" : "frequency.tooltip.click_to_replace"));
                CreateClient.VALUE_SETTINGS_HANDLER.showHoverTip(tip);
            }
        }
    }
}
