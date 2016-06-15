package al.qa.so;

import java.util.function.Consumer;

/**
 * @author Alexey Lyanguzov.
 */
public interface ScreenPart {
    default <T, R extends BaseScreen> R action(Consumer<T> proc){
        return action(proc, null);
    }

    default String name(){
        return this.getClass().getSimpleName();
    }

    @SuppressWarnings("all")
    default <T, R extends BaseScreen> R action(Consumer<T> proc, T argument){
        return Manager.doAction(proc, argument);
    }

    default <T, R extends BaseScreen> R transition(Consumer<T> proc){
        return transition(proc, null);
    }

    @SuppressWarnings("all")
    default <T, R extends BaseScreen> R transition(Consumer<T> proc, T argument){
        return (R)Manager.doTransition(proc, argument);
    }

    default <T, R extends BaseScreen> R check(Consumer<T> proc){
        return check(proc, null);
    }

    @SuppressWarnings("all")
    default <T, R extends BaseScreen> R check(Consumer<T> proc, T argument){
        return Manager.doCheck(proc, argument);
    }
}
