package bettersnowvillages.command;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.compat.IceAndFireForksUtil;
import bettersnowvillages.config.ForgeConfigHandler;
import bettersnowvillages.world.gen.BetterSnowVillagesChunkGenerator;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BetterSnowVillagesCommand extends CommandBase {

    public static final String LOCATE = "locate";

    @Override
    @Nonnull
    public String getName() {
        return BetterSnowVillages.MODID;
    }

    @Override
    @Nonnull
    public String getUsage(ICommandSender commandSender) {
        StringBuilder usage = new StringBuilder();

        usage.append("/bettersnowvillages <locate> [chunk steps]");

        return usage.toString();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if(args.length < 1) {
            sender.sendMessage(new TextComponentString(getUsage(sender)));
            throw new WrongUsageException("commands.bettersnowvillages.invalidusage");
        }

        switch (args[0]) {
            case LOCATE: {
                String name = BetterSnowVillagesChunkGenerator.BETTER_SNOW_VILLAGE.getStructureName();
                if(ForgeConfigHandler.betterSVGen.betterSVGenerator != BetterSnowVillages.SnowVillageGenerator.MapGenStructure) {
                    throw new CommandException("commands.bettersnowvillages.notmapgenstructure", name);
                }
                else {
                    int distance = args.length > 1
                            ? parseInt(args[1])
                            : IceAndFireForksUtil.getSnowVillageMinimumDistance(ForgeConfigHandler.betterSVGen.useIFConfig) >> 4;
                    distance++;
                    BlockPos blockPos = BetterSnowVillagesChunkGenerator.BETTER_SNOW_VILLAGE.getNearestStructurePos(server.getEntityWorld(), sender.getPosition(), false, distance);
                    if(blockPos == null) {
                        throw new CommandException("commands.locate.failure", name);
                    }

                    String command = "/tp @s " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ();
                    ITextComponent component = new TextComponentTranslation("commands.locate.success",
                            BetterSnowVillagesChunkGenerator.BETTER_SNOW_VILLAGE.getStructureName(),
                            blockPos.getX(),
                            blockPos.getZ()
                    );
                    component.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(command)));
                    component.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
                    sender.sendMessage(component);
                }
                break;
            }
            default:
                throw new WrongUsageException("commands.bettersnowvillages.invalidusage");
        }
    }

    // ==================================================
    //                     Permission
    // ==================================================
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender commandSender) {
        if(commandSender instanceof EntityPlayer) {
            if(!commandSender.canUseCommand(this.getRequiredPermissionLevel(), this.getName()))
                return false;
        }
        return true;
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(CommandBase.getListOfStringsMatchingLastWord(args, LOCATE));
        }
        else if (args.length == 2){
            switch (args[0]) {
                case LOCATE:
                    completions.addAll(CommandBase.getListOfStringsMatchingLastWord(args, String.valueOf(IceAndFireForksUtil.getSnowVillageMinimumDistance(ForgeConfigHandler.betterSVGen.useIFConfig) >> 4)));
                    break;
            }
        }
        return completions;
    }
}
