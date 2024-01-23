package fastdrop;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class FastDrop implements ModInitializer {
    public static final ModMetadata MOD_META;
    public static final String MOD_ID = "fastdrop";
    public static boolean hold_mouse0;


    static {
        MOD_META = FabricLoader.getInstance()
                .getModContainer(MOD_ID)
                .orElseThrow()
                .getMetadata();
    }

    @Override
    public void onInitialize() {
    }
}

