package maze.frequency.client;

/**
 * ThreadLocal flag for switching the symbol rendering context.
 * Set by TableClothRendererMixin and checked by DepotRendererMixin.
 */
public final class SymbolRenderContext {
    public static final ThreadLocal<Boolean> TABLE_CLOTH = ThreadLocal.withInitial(() -> false);

    private SymbolRenderContext() {}
}
