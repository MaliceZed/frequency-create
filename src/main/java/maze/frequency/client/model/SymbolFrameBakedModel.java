package maze.frequency.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;

import org.jetbrains.annotations.Nullable;

import maze.frequency.FrequencyMod;
import maze.frequency.init.FrequencyModItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolFrameBakedModel extends BakedModelWrapper<BakedModel> {
    private final Object spriteLock = new Object();
    private Map<String, TextureAtlasSprite> symbolSprites;
    private TextureAtlasSprite defaultSprite;
    private boolean spritesLoaded = false;

    private String lastSymbolName = "";
    private final Map<Direction, List<BakedQuad>> cachedQuads = new HashMap<>();

    public SymbolFrameBakedModel(BakedModel original) {
        super(original);
    }

    private void ensureSpritesLoaded() {
        if (spritesLoaded) return;
        synchronized (spriteLock) {
            if (spritesLoaded) return;
            this.symbolSprites = new HashMap<>();
            TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);

            for (String name : FrequencyModItems.SYMBOL_NAMES) {
                String baseName = name.startsWith("brass_") ? name.substring(6) : name;
                ResourceLocation tex = ResourceLocation.fromNamespaceAndPath(FrequencyMod.MODID, "block/symbols/" + baseName);
                symbolSprites.put(baseName, atlas.getSprite(tex));
            }

            defaultSprite = symbolSprites.get("symbol_empty");
            spritesLoaded = true;
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable net.minecraft.client.renderer.RenderType renderType) {
        ensureSpritesLoaded();
        String symbol = data.get(SymbolFrameModelData.SYMBOL_PROPERTY);
        if (symbol == null || symbol.equals("symbol_empty")) {
            return super.getQuads(state, side, rand, data, renderType);
        }

        // Reset cache if the symbol has changed
        if (!lastSymbolName.equals(symbol)) {
            cachedQuads.clear();
            lastSymbolName = symbol;
        }

        // Return cached result if available
        if (cachedQuads.containsKey(side)) {
            return cachedQuads.get(side);
        }

        TextureAtlasSprite targetSprite = symbolSprites.get(symbol);
        if (targetSprite == null) {
            return super.getQuads(state, side, rand, data, renderType);
        }

        List<BakedQuad> original = super.getQuads(state, side, rand, data, renderType);
        if (original.isEmpty()) {
            cachedQuads.put(side, original);
            return original;
        }

        List<BakedQuad> result = new java.util.ArrayList<>(original.size());
        for (BakedQuad quad : original) {
            TextureAtlasSprite quadSprite = quad.getSprite();
            if (quadSprite != null && quadSprite.equals(defaultSprite)) {
                result.add(replaceSprite(quad, quadSprite, targetSprite));
            } else {
                result.add(quad);
            }
        }

        cachedQuads.put(side, result);
        return result;
    }

    private BakedQuad replaceSprite(BakedQuad quad, TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite) {
        if (oldSprite == newSprite) return quad;
        int[] vertices = quad.getVertices().clone();
        int vertexSize = 8;

        for (int i = 0; i < 4; i++) {
            int base = i * vertexSize;
            float oldU = Float.intBitsToFloat(vertices[base + 4]);
            float oldV = Float.intBitsToFloat(vertices[base + 5]);

            float modelU = (oldU - oldSprite.getU0()) / (oldSprite.getU1() - oldSprite.getU0());
            float modelV = (oldV - oldSprite.getV0()) / (oldSprite.getV1() - oldSprite.getV0());

            float newU = newSprite.getU(modelU);
            float newV = newSprite.getV(modelV);

            vertices[base + 4] = Float.floatToRawIntBits(newU);
            vertices[base + 5] = Float.floatToRawIntBits(newV);
        }

        return new BakedQuad(vertices, quad.getTintIndex(), quad.getDirection(), newSprite, quad.isShade());
    }
}
