package net.piratjsk.rccl.listeners;

import net.piratjsk.rccl.RCCL;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public final class CraftListener implements Listener {

    @EventHandler
    public void onCraft(final PrepareItemCraftEvent event) {
        ItemStack item = null;
        int notAir = 0;
        for (final ItemStack slot : event.getInventory().getMatrix()) {
            if (slot.getType() != Material.AIR) {
                item = slot;
                notAir++;
            }
        }
        if (notAir!=1 || item==null) return;

        final ItemStack result = RCCL.getCraftable().get(item.getType()).clone();
        if (result!=null) {
            if (item.getType().getMaxDurability() != 0) {
                final double dur = item.getType().getMaxDurability() - item.getDurability();
                final double amount = result.getAmount() * (dur / item.getType().getMaxDurability());
                if (amount >= 1.0) result.setAmount((int) amount);
                else if (amount >= 0.5) result.setAmount(1);
                else result.setType(Material.AIR);
            }
            event.getInventory().setResult(result);
        }
    }

}
