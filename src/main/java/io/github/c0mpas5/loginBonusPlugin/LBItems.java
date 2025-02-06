package io.github.c0mpas5.loginBonusPlugin;
import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.Bukkit;
//import org.bukkit.ChatColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.Base64;

public class LBItems {
    // adminHomeGui
    public static ItemStack createPickaxeIS() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "ログボ作成");
            meta.setLore(List.of(
                    ChatColor.GRAY + "新規ログインボーナスを作成します",
                    ChatColor.GRAY + "テンプレートや既存のログインボーナスを基に作成することもできます"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack editAnvilIS() {
        ItemStack item = new ItemStack(Material.ANVIL, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "ログボ編集");
            meta.setLore(List.of(
                    ChatColor.GRAY + "既存のログインボーナスを編集・削除します"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }


    // adminCreateGui
    public static ItemStack rewardSettingChestIS(){
        ItemStack item = new ItemStack(Material.CHEST, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "報酬設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "当該ログインボーナスの報酬内容を設定します"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack periodSettingClockIS(){
        ItemStack item = new ItemStack(Material.CLOCK, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "開催期間設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "当該ログインボーナスの開催期間を設定します"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack bannerSettingPaintingIS(){
        ItemStack item = new ItemStack(Material.PAINTING, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "バナー設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "当該ログインボーナスのバナーを設定します"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack nameSettingNametagIS(){
        ItemStack item = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "内部名設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "当該ログインボーナスの名前を設定します"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack otherSettingBookIS(){
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "その他の設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "当該ログインボーナスのその他の設定を行います"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack loadBookShelfIS(){
        ItemStack item = new ItemStack(Material.BOOKSHELF, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "設定を読み込む");
            meta.setLore(List.of(
                    ChatColor.GRAY + "既存のログインボーナスやテンプレートから",
                    ChatColor.GRAY + "報酬設定や開催期間設定などのすべての設定を読み込みます",
                    ChatColor.of("#DF2E8F") + "現在設定中の内容は上書きされます"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }


    // adminRewardSettingGui
    public static ItemStack normalRewardPlayerHeadIS(){
        ItemStack item = getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MxYjJmNTkyY2ZjOGQzNzJkY2Y1ZmQ0NGVlZDY5ZGRkYzY0NjAxZDc4NDZkNzI2MTlmNzA1MTFkODA0M2E4OSJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "通常枠報酬設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "通常枠の報酬プールを設定します",
                    ChatColor.of("#797979") + "・各スロットのうち、いずれか1スロットが報酬として与えられます",
                    ChatColor.of("#797979") + "・報酬を与える際、各スロットの排出確率が等分されて抽選されます"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack specialRewardPlayerHeadIS(){
        ItemStack item = getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ2NzUxNThjMDc2N2VlNTA4YzUyZDUyNDI2Y2VmM2EyYzJiMjliN2U0ODdjOTI5NTNhMzgyM2Y1NjFkMDZhZiJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "特別枠報酬設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "特別枠の報酬プールを設定します",
                    ChatColor.of("#797979") + "・各スロットのうち、いずれか1スロットが報酬として与えられます",
                    ChatColor.of("#797979") + "・報酬を与える際、各スロットの排出確率が等分されて抽選されます"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack bonusRewardPlayerHeadIS(){
        ItemStack item = getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTdiYzI1MWE2Y2IwZDZkOWYwNWM1NzExOTExYTZlYzI0YjIwOWRiZTY0MjY3OTAxYTRiMDM3NjFkZWJjZjczOCJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "ボーナス枠報酬設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "ボーナス枠の報酬プールを設定します",
                    ChatColor.of("#797979") + "・各スロットのうち、いずれか1スロットが報酬として与えられます",
                    ChatColor.of("#797979") + "・報酬を与える際、各スロットの排出確率が等分されて抽選されます"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack continuousRewardPlayerHeadIS(){
        ItemStack item = getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTdiODMyNGQxYjI5ZWZkNjY3YmZiZDY5MTM0ZTdkODkxNzExMDc5MWI4MWJhZjgwZmRiNmUyYzIwN2FmZTE4MSJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "連続ログボ報酬設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "連続ログインボーナスの報酬プールを設定します",
                    ChatColor.of("#797979") + "・各スロットのうち、いずれか1スロットが報酬として与えられます",
                    ChatColor.of("#797979") + "・報酬を与える際、各スロットの排出確率が等分されて抽選されます"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack bonusRewardConditionCommandBlockIS(){
        ItemStack item = new ItemStack(Material.COMMAND_BLOCK, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "ボーナス枠条件設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "ボーナス枠の報酬を受け取るために必要なログイン日数の割合を設定します",
                    ChatColor.of("#797979") + "・【ログインした日数（日）/ログインボーナスの開催期間（日）* 100】で割合（百分率）を算出します",
                    ChatColor.of("#797979") + "・算出された値が当項目で設定した割合以上のプレイヤーに対し、",
                    ChatColor.of("#797979") + "　開催期間最終日にボーナス枠の報酬を与えます"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack subAccountSettingPaperIS(){
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "サブアカウント設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "個人が何アカウントまで特別枠/ボーナス枠/連続ログインボーナスの報酬を受け取れるか設定します"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack rewardSettingTutorialBookIS(){
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "各報酬枠について");
            meta.setLore(List.of(
                    ChatColor.WHITE + "【通常枠】────────────────",
                    ChatColor.of("#C2C2C2") + "特別枠/ボーナス枠が与えられない場合に与えられる報酬枠です。",
                    ChatColor.of("#C2C2C2") + "同一人物が複数のアカウントでログインした場合でも、この枠の報酬は上限無く受け取ることができます。",
                    ChatColor.of("#C2C2C2") + " ",
                    ChatColor.WHITE + "【特別枠】────────────────",
                    ChatColor.of("#C2C2C2") + "開催期間中に10の倍数日ログインした場合に与えられる報酬枠です。",
                    ChatColor.of("#C2C2C2") + "同一人物が複数のアカウントでログインした場合、",
                    ChatColor.of("#C2C2C2") + "この枠の報酬は" + ChatColor.GOLD  + "サブアカウント設定" + ChatColor.RESET + "" + ChatColor.of("#C2C2C2") + "で指定した回数を上限に受け取ることができます。",
                    ChatColor.of("#C2C2C2") + " ",
                    ChatColor.WHITE + "【ボーナス枠】──────────────",
                     ChatColor.GOLD + "ボーナス枠条件設定" + ChatColor.RESET + "" + ChatColor.of("#C2C2C2") + "で指定したログイン率を満たしたプレイヤーに対して",
                    ChatColor.of("#C2C2C2") + "開催期間最終日に与えられる報酬枠です。",
                    ChatColor.of("#C2C2C2") + "同一人物が複数のアカウントでログインした場合、",
                    ChatColor.of("#C2C2C2") + "この枠の報酬は" +  ChatColor.GOLD + "サブアカウント設定" + ChatColor.RESET + "" + ChatColor.of("#C2C2C2") + "で指定した回数を上限に受け取ることができます。",
                    ChatColor.of("#C2C2C2") + " ",
                    ChatColor.WHITE + "【連続ログインボーナス】──────────────",
                    ChatColor.of("#C2C2C2") + "開催期間中に10日間連続してログインした場合に与えられる報酬枠です。",
                    ChatColor.of("#C2C2C2") + "ログインが1日でも途切れるか、一度この報酬枠を受け取ると、連続ログインのカウントはリセットされます。",
                    ChatColor.of("#C2C2C2") + "期間中、10日間連続ログインを達成する度に報酬を受け取ることができます。",
                    ChatColor.of("#C2C2C2") + "同一人物が複数のアカウントでログインした場合、",
                    ChatColor.of("#C2C2C2") + "この枠の報酬は" + ChatColor.GOLD + "サブアカウント設定" + ChatColor.RESET + "" + ChatColor.of("#C2C2C2") + "で指定した回数を上限に受け取ることができます。"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    // adminRewardSettingGui
    public static ItemStack normalRewardSettingTutorialBookIS() {
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "各報酬枠について");
            meta.setLore(List.of(
                    ChatColor.WHITE + "【通常枠について】────────────────",
                    ChatColor.of("#C2C2C2") + "・特別枠/ボーナス枠が与えられない場合に与えられる報酬枠です。",
                    ChatColor.of("#C2C2C2") + "・同一人物が複数のアカウントでログインした場合でも、この枠の報酬は上限無く受け取ることができます。",
                    ChatColor.of("#C2C2C2") + " ",
                    ChatColor.WHITE + "【通常枠報酬プールについて】──────────────",
                    ChatColor.of("#C2C2C2") + "・各スロットのうち、いずれか1スロットが報酬として与えられます",
                    ChatColor.of("#C2C2C2") + "・報酬を与える際、各スロットの排出確率が等分されて抽選されます"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    //汎用
    public static ItemStack backgroundBlackGlassIS() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(" ");
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack backgroundLimeGlassIS() {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "保存する");
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack getCustomHead(String base64) {
        // Create a new player profile with a random UUID
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());

        // Decode the base64 string to get the URL
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        String decodedString = new String(decodedBytes);
        String url = decodedString.split("\"url\":\"")[1].split("\"")[0];

        // Set the skin URL to the player profile
        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL(url));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        profile.setTextures(textures);

        // Create the ItemStack and set the player profile to the SkullMeta
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setPlayerProfile(profile);
            head.setItemMeta(meta);
        }

        return head;
    }
}
