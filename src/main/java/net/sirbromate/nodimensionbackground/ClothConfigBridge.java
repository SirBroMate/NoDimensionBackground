package net.sirbromate.nodimensionbackground;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClothConfigBridge implements ConfigScreenFactory<Screen> {
    @Override
    public Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setTitle(Text.translatable("nodimensionbackground.config.title"))
                .setSavingRunnable(Config::save)
                .setParentScreen(parent);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("nodimensionbackground.config.title"));

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("nodimensionbackground.config.disablenetherbackground"), Config.disableNetherBackground)
                .setDefaultValue(true)
                .setSaveConsumer(b -> Config.disableNetherBackground = b)
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("nodimensionbackground.config.disableendbackground"), Config.disableEndBackground)
                .setDefaultValue(true)
                .setSaveConsumer(b -> Config.disableEndBackground = b)
                .build());

        return builder.build();
    }
}
