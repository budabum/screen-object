package al.qa.so;

import java.util.function.Consumer;

/**
 * @author Alexey Lyanguzov.
 */
public interface ScreenPart {
    default String name(){
        return this.getClass().getSimpleName();
    }

    default <T, R extends BaseScreen> R perform(Consumer<T> proc){
        return perform(proc, null);
    }

    @SuppressWarnings("all")
    default <T, R extends BaseScreen> R perform(Consumer<T> proc, T argument){
        return Manager.perform(proc, argument);
    }
}
