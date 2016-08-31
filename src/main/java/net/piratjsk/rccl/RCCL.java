package net.piratjsk.rccl;

import net.piratjsk.rccl.listeners.CraftListener;
import net.piratjsk.rccl.listeners.SmeltListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Iterator;

public final class RCCL extends JavaPlugin {

    private static HashMap<Material, ItemStack> craftable = new HashMap<>();
    private static HashMap<Material, ItemStack> smeltable = new HashMap<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getCommand("rccl").setExecutor(new RCCLCommand(this));

        this.registerRecipesFromConfig();

        // listeners
        getServer().getPluginManager().registerEvents(new CraftListener(), this);
        getServer().getPluginManager().registerEvents(new SmeltListener(), this);
    }

    @Override
    public void onDisable() {
        this.unregisterAllRecipes();
    }

    public void registerRecipesFromConfig() {
        this.getConfig().getValues(true).entrySet().stream().filter(e -> !(e.getValue() instanceof MemorySection)).forEach(e -> {
            final String[] sk = e.getKey().split("\\.");
            final Material item = Material.valueOf(sk[1]);

            final String[] l = e.getValue().toString().split(" ");
            final ItemStack result = new ItemStack(Material.valueOf(l[1]));
            result.setAmount(Integer.valueOf(l[0]));

            if (sk[0].equalsIgnoreCase("crafting")) {
                ShapelessRecipe bukkitRecipe = new ShapelessRecipe(result);
                bukkitRecipe.addIngredient(1, item, -1);
                Bukkit.addRecipe(bukkitRecipe);
                craftable.put(item, result);
            }

            if (sk[0].equalsIgnoreCase("furnace")) {
                Bukkit.addRecipe(new FurnaceRecipe(result, item, 32767));
                smeltable.put(item, result);
            }
        });
    }

    public void unregisterAllRecipes() {
        final Iterator<Recipe> recipes = Bukkit.recipeIterator();
        while (recipes.hasNext()) {
            final Recipe recipe = recipes.next();
            if (recipe instanceof ShapelessRecipe) {
                final ShapelessRecipe shaplessRecipe = (ShapelessRecipe) recipe;
                if (shaplessRecipe.getIngredientList().size()!=1) continue;
                final Material item = shaplessRecipe.getIngredientList().get(0).getType();
                if (!craftable.containsKey(item)) continue;
                final ItemStack result = craftable.get(item);
                if (!shaplessRecipe.getResult().equals(result)) continue;
                recipes.remove();
            }

            if (recipe instanceof FurnaceRecipe) {
                final FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;
                final Material item = furnaceRecipe.getInput().getType();
                if (!smeltable.containsKey(item)) continue;
                final ItemStack result = smeltable.get(item);
                if (!furnaceRecipe.getResult().equals(result)) continue;
                recipes.remove();
            }
        }
        craftable.clear();
        smeltable.clear();
    }

    public static HashMap<Material, ItemStack> getCraftable() {
        return craftable;
    }

    public static HashMap<Material, ItemStack> getSmeltable() {
        return smeltable;
    }

}
