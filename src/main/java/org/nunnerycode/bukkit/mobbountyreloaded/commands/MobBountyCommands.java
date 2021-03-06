package org.nunnerycode.bukkit.mobbountyreloaded.commands;

import org.apache.commons.lang.math.DoubleRange;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.nunnerycode.bukkit.mobbountyreloaded.MobBountyReloadedPlugin;
import org.nunnerycode.bukkit.mobbountyreloaded.utils.MessageUtils;
import org.nunnerycode.bukkit.mobbountyreloaded.utils.MobUtils;
import se.ranzdo.bukkit.methodcommand.Arg;
import se.ranzdo.bukkit.methodcommand.Command;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MobBountyCommands {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private MobBountyReloadedPlugin plugin;

    public MobBountyCommands(MobBountyReloadedPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(identifier = "mobbountyreloaded load", permissions = "mobbountyreloaded.command.load")
    public void loadSubcommand(CommandSender sender) {
        plugin.reload();
        MessageUtils.sendColoredMessage(sender, plugin.getIvorySettings().getString("language.messages.load",
                                                                                    "language.messages.load"));
    }

    @Command(identifier = "mobbountyreloaded save", permissions = "mobbountyreloaded.command.save")
    public void saveSubcommand(CommandSender sender) {
        plugin.save();
        MessageUtils.sendColoredMessage(sender, plugin.getIvorySettings().getString("language.messages.save",
                                                                                    "language.messages.save"));
    }

    @Command(identifier = "mobbountyreloaded worldreward", permissions = "mobbountyreloaded.command.worldreward")
    public void worldRewardSubcommand(CommandSender sender, @Arg(name = "world", def = "") String worldName,
                                      @Arg(name = "mob", def = "ZOMBIE") String mobName,
                                      @Arg(name = "reward", def = "0.0") String reward) {
        if (worldName.equals("") && sender instanceof Player) {
            worldName = ((Player) sender).getWorld().getName();
        }
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            MessageUtils.sendColoredMessage(sender, plugin.getIvorySettings().getString("language.message" +
                                                                                        ".world-does-not-exist",
                    "language.message.world-does-not-exist"));
            return;
        }
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(mobName);
        } catch (Exception e) {
            MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings().getString("language.messages." +
                                                                                                "reward-help",
                    "language.messages.reward-help"
            ), new String[][]{{"%mobs%", MobUtils.getMobsOnServer().toString()}});
            MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings().getString("language.messages." +
                                                                                                "world-reward-help",
                    "language.messages.world-reward-help"
            ), new String[][]{{"%worlds%", getWorldNames().toString()}});
            MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings().getString("language.messages." +
                                                                                                "world-reward-failure",
                    "language.messages.worldreward-failure"
            ), new String[][]{{"%mob%", mobName}, {"%value%", reward}});
            return;
        }
        if (entityType == null) {
            MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings().getString("language.messages." +
                                                                                                "reward-help",
                    "language.messages.reward-help"
            ), new String[][]{{"%mobs%", MobUtils.getMobsOnServer().toString()}});
            MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings().getString("language.messages." +
                                                                                                "world-reward-help",
                    "language.messages.world-reward-help"
            ), new String[][]{{"%worlds%", getWorldNames().toString()}});
            MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings().getString("language.messages." +
                                                                                                "world-reward-failure",
                    "language.messages.worldreward-failure"
            ), new String[][]{{"%mob%", mobName}, {"%value%", reward}});
            return;
        }
        plugin.getMobHandler().setReward(entityType, world, reward);
        MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings().getString("language.messages" +
                                                                                            ".world-reward-success",
                "language.messages.world-reward-success"
        ), new String[][]{{"%mob%", mobName}, {"%value%", reward}});
    }

    @Command(identifier = "mobbountyreloaded reward", permissions = "mobbountyreloaded.command.reward")
    public void rewardSubcommand(CommandSender sender, @Arg(name = "mob", def = "ZOMBIE") String mobName,
                                 @Arg(name = "reward", def = "0.0") String reward) {
        EntityType entityType;
        try {
            entityType = EntityType.valueOf(mobName);
        } catch (Exception e) {
            MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings().getString("language.messages." +
                                                                                                "reward-help",
                    "language.messages.reward-help"
            ), new String[][]{{"%mobs%", MobUtils.getMobsOnServer().toString()}});
            MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings().getString("language.messages." +
                                                                                                "reward-failure",
                    "language.messages.reward-failure"
            ), new String[][]{{"%mob%", mobName}, {"%value%", reward}});
            return;
        }
        if (entityType == null) {
            MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings().getString("language.messages." +
                                                                                                "reward-help",
                    "language.messages.reward-help"
            ), new String[][]{{"%mobs%", MobUtils.getMobsOnServer().toString()}});
            MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings().getString("language.messages." +
                                                                                                "reward-failure",
                    "language.messages.reward-failure"
            ), new String[][]{{"%mob%", mobName}, {"%value%", reward}});
            return;
        }
        plugin.getMobHandler().setReward(entityType, null, reward);
        MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings().getString("language.messages" +
                                                                                            ".reward-success",
                "language.messages.reward-success"
        ), new String[][]{{"%mob%", mobName}, {"%value%", reward}});
    }

    @Command(identifier = "mobbountyreloaded list", permissions = "mobbountyreloaded.command.list")
    public void listSubcommand(CommandSender sender,
                               @Arg(name = "world", def = "") String worldName) {
        if (worldName.equals("") && sender instanceof Player) {
            worldName = ((Player) sender).getWorld().getName();
        }
        World world = Bukkit.getWorld(worldName);
        for (EntityType entityType : EntityType.values()) {
            DoubleRange doubleRange = plugin.getMobHandler().getReward(entityType, world);
            if (doubleRange.getMaximumDouble() < 0.0) {
                if (doubleRange.getMaximumDouble() == doubleRange.getMinimumDouble()) {
                    MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings()
                            .getString("language.messages.list-fine", ""), new String[][]{
                            {"%mob%", plugin.getIvorySettings()
                                    .getString("language.mob-names." + entityType.name(), entityType.name())},
                            {"%value%", DECIMAL_FORMAT.format(doubleRange.getMaximumDouble())}});
                } else {
                    MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings()
                            .getString("language.messages.list-fine-range", ""), new String[][]{
                            {"%mob%", plugin.getIvorySettings()
                                    .getString("language.mob-names." + entityType.name(), entityType.name())},
                            {"%value1%", DECIMAL_FORMAT.format(doubleRange.getMinimumDouble())},
                            {"%value2%", DECIMAL_FORMAT.format(doubleRange.getMaximumDouble())}});
                }
            } else if (doubleRange.getMaximumDouble() > 0.0) {
                if (doubleRange.getMaximumDouble() == doubleRange.getMinimumDouble()) {
                    MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings()
                            .getString("language.messages.list-reward", ""), new String[][]{
                            {"%mob%", plugin.getIvorySettings()
                                    .getString("language.mob-names." + entityType.name(), entityType.name())},
                            {"%value%", DECIMAL_FORMAT.format(doubleRange.getMaximumDouble())}});
                } else {
                    MessageUtils.sendColoredArgumentMessage(sender, plugin.getIvorySettings()
                            .getString("language.messages.list-reward-range", ""), new String[][]{
                            {"%mob%", plugin.getIvorySettings()
                                    .getString("language.mob-names." + entityType.name(), entityType.name())},
                            {"%value1%", DECIMAL_FORMAT.format(doubleRange.getMinimumDouble())},
                            {"%value2%", DECIMAL_FORMAT.format(doubleRange.getMaximumDouble())}});
                }
            }
        }
    }

    private List<String> getWorldNames() {
        List<String> worldNames = new ArrayList<>();
        for (World w : Bukkit.getWorlds()) {
            worldNames.add(w.getName());
        }
        return worldNames;
    }

}
