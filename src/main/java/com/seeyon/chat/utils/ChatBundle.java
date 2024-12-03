package com.seeyon.chat.utils;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author Shaozz
 */
public class ChatBundle {

    @NonNls
    private static final String BUNDLE = "messages.ChatBundle";
    private static final DynamicBundle INSTANCE = new DynamicBundle(ChatBundle.class, BUNDLE);

    public static @NotNull
    @Nls String message(
            @NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
            Object... params
    ) {
        return INSTANCE.getMessage(key, params);
    }

//    public static Supplier<@Nls String> lazyMessage(
//            @NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
//            Object @NotNull ... params
//    ) {
//        return INSTANCE.getLazyMessage(key, params);
//    }


}
