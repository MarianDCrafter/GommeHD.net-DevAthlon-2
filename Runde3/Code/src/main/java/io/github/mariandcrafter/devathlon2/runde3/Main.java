package io.github.mariandcrafter.devathlon2.runde3;

import io.github.mariandcrafter.bukkitpluginapi.Plugin;
import io.github.mariandcrafter.bukkitpluginapi.messages.Message;
import org.bukkit.ChatColor;

/**
 * The main class of the DevAthlon plugin.
 *
 * @author MarianDCrafter
 */
public class Main extends Plugin {

    private static Main instance;

    /**
     * Called when the plugin is enabled. Sets up everything including listeners, commands etc.
     */
    @Override
    public void onEnable() {
        instance = this;

        getMessageSender().send(new Message.Builder("Hello World!")
                .setConsole(true).build());
        System.out.println("2. Devathlon, 3. Runde - GreenGlowPixel-Team - Plugin enabled!");
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        System.out.println("2. Devathlon, 3. Runde - GreenGlowPixel-Team - Plugin disabled!");
    }

    /**
     * @return the instance of the main class
     */
    public static Main getInstance() {
        return instance;
    }

    @Override
    protected String prefix() {
        return "DevAthlon";
    }

    @Override
    protected ChatColor defaultColor() {
        return ChatColor.GOLD;
    }

}
