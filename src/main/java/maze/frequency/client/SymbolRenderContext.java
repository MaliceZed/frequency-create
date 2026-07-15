package maze.frequency.client;

/**
 * ThreadLocal-флаг для переключения контекста рендеринга символов.
 * Устанавливается TableClothRendererMixin и проверяется DepotRendererMixin.
 */
public final class SymbolRenderContext {
    public static final ThreadLocal<Boolean> TABLE_CLOTH = ThreadLocal.withInitial(() -> false);

    private SymbolRenderContext() {}
}
