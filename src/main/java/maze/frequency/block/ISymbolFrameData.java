package maze.frequency.block;

/**
 * Common interface for symbol frame data, allowing client and server
 * to interact with symbol frame state without referencing client-only classes.
 */
public interface ISymbolFrameData {
    String getSymbolName();
    void setSymbolName(String name);
}
