package net.piratjsk.rccl.listeners;

import net.piratjsk.rccl.RCCL;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

public final class SmeltListener implements Listener {

    @EventHandler
    public void onReSmelt(final FurnaceSmeltEvent event) {
        final ItemStack item = event.getSource();
        final ItemStack result = RCCL.getSmeltable().get(item.getType()).clone();
        if (result!=null) {
            if (item.getType().getMaxDurability() != 0) {
                final double dur = item.getType().getMaxDurability() - item.getDurability();
                final double amount = result.getAmount() * (dur / item.getType().getMaxDurability());
                if (amount >= 1.0) result.setAmount((int) amount);
                else if (amount >= 0.5) result.setAmount(1);
                else result.setType(Material.AIR);
            }
            event.setResult(result);
        }
    }

}
