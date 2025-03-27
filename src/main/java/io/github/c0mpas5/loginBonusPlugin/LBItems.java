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
import java.time.LocalDate;
import java.util.ArrayList;
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

    public static ItemStack globalSettingEnderEyeIS() {
        ItemStack item = new ItemStack(Material.ENDER_EYE, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "グローバル設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "ログイン情報修正などが行えます"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }


    // adminFirstNameSettingGui
    public static ItemStack firstNamePaperIS(){
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("作成したいログボの名前を入力");
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack firstNameSettingTutorialBookIS(){
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "新規ログインボーナス内部名設定について");
            meta.setLore(List.of(
                    ChatColor.of("#C2C2C2") + "" + ChatColor.BOLD + "作成するログインボーナスの内部名を名前欄に入力してください。",
                    ChatColor.of("#C2C2C2") + "ここで設定した内部名はあとから変更することもできます。"
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

    public static ItemStack timeSettingClockIS(){
        ItemStack item = new ItemStack(Material.CLOCK, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "時間系設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "当該ログインボーナスの開催期間・日付変更時刻を設定します"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack invalidTimeSettingClockIS(){
        ItemStack item = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "【無効】時間系設定");
            meta.setLore(List.of(
                    ChatColor.RED + "" + ChatColor.BOLD + "現在当該ログインボーナス開催中につき、この設定は編集できません",
                    ChatColor.GRAY + "当該ログインボーナスの開催期間・日付変更時刻を設定します"
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
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "【未実装】バナー設定");
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
                    ChatColor.GRAY + "当該ログインボーナスのその他の設定を行います",
                    ChatColor.GRAY + "有効化設定などが行えます"
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
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "【未実装】設定を読み込む");
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
    public static ItemStack normalRewardPoolPlayerHeadIS(){
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

    public static ItemStack specialRewardPoolPlayerHeadIS(){
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

    public static ItemStack bonusRewardPoolPlayerHeadIS(){
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

    public static ItemStack continuousRewardPoolPlayerHeadIS(){
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

    // adminNormalRewardSettingGui
    public static ItemStack normalRewardSettingTutorialBookIS() {
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "通常枠について");
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

    // adminSpecialRewardSettingGui
    public static ItemStack specialRewardSettingTutorialBookIS() {
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "特別枠について");
            meta.setLore(List.of(
                    ChatColor.WHITE + "【特別枠について】────────────────",
                    ChatColor.of("#C2C2C2") + "開催期間中に10の倍数日ログインした場合に与えられる報酬枠です。",
                    ChatColor.of("#C2C2C2") + "同一人物が複数のアカウントでログインした場合、",
                    ChatColor.of("#C2C2C2") + "この枠の報酬は" + ChatColor.GOLD  + "サブアカウント設定" + ChatColor.RESET + "" + ChatColor.of("#C2C2C2") + "で指定した回数を上限に受け取ることができます。",
                    ChatColor.of("#C2C2C2") + " ",
                    ChatColor.WHITE + "【特別枠報酬プールについて】──────────────",
                    ChatColor.of("#C2C2C2") + "・各スロットのうち、いずれか1スロットが報酬として与えられます",
                    ChatColor.of("#C2C2C2") + "・報酬を与える際、各スロットの排出確率が等分されて抽選されます"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    // adminBonusRewardSettingGui
    public static ItemStack bonusRewardSettingTutorialBookIS() {
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "ボーナス枠について");
            meta.setLore(List.of(
                    ChatColor.WHITE + "【ボーナス枠について】────────────────",
                    ChatColor.GOLD + "ボーナス枠条件設定" + ChatColor.RESET + "" + ChatColor.of("#C2C2C2") + "で指定したログイン率を満たしたプレイヤーに対して",
                    ChatColor.of("#C2C2C2") + "開催期間最終日に与えられる報酬枠です。",
                    ChatColor.of("#C2C2C2") + "同一人物が複数のアカウントでログインした場合、",
                    ChatColor.of("#C2C2C2") + "この枠の報酬は" +  ChatColor.GOLD + "サブアカウント設定" + ChatColor.RESET + "" + ChatColor.of("#C2C2C2") + "で指定した回数を上限に受け取ることができます。",
                    ChatColor.of("#C2C2C2") + " ",
                    ChatColor.WHITE + "【ボーナス枠報酬プールについて】──────────────",
                    ChatColor.of("#C2C2C2") + "・各スロットのうち、いずれか1スロットが報酬として与えられます",
                    ChatColor.of("#C2C2C2") + "・報酬を与える際、各スロットの排出確率が等分されて抽選されます"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    // adminContinuousRewardSettingGui
    public static ItemStack continuousRewardSettingTutorialBookIS() {
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "連続ログインボーナスについて");
            meta.setLore(List.of(
                    ChatColor.WHITE + "【連続ログインボーナスについて】────────────────",
                    ChatColor.of("#C2C2C2") + "開催期間中に10日間連続してログインした場合に与えられる報酬枠です。",
                    ChatColor.of("#C2C2C2") + "ログインが1日でも途切れるか、一度この報酬枠を受け取ると、連続ログインのカウントはリセットされます。",
                    ChatColor.of("#C2C2C2") + "期間中、10日間連続ログインを達成する度に報酬を受け取ることができます。",
                    ChatColor.of("#C2C2C2") + "同一人物が複数のアカウントでログインした場合、",
                    ChatColor.of("#C2C2C2") + "この枠の報酬は" + ChatColor.GOLD + "サブアカウント設定" + ChatColor.RESET + "" + ChatColor.of("#C2C2C2") + "で指定した回数を上限に受け取ることができます。",
                    ChatColor.of("#C2C2C2") + " ",
                    ChatColor.WHITE + "【連続ログインボーナス報酬プールについて】──────────────",
                    ChatColor.of("#C2C2C2") + "・各スロットのうち、いずれか1スロットが報酬として与えられます",
                    ChatColor.of("#C2C2C2") + "・報酬を与える際、各スロットの排出確率が等分されて抽選されます"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack tempPaperIS() {
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "仮置きの紙");
            meta.setLore(List.of(
                    ChatColor.of("#C2C2C2") + "このプールの設定を保存するには、",
                    ChatColor.of("#C2C2C2") + "ログインが1日でも途切れるか、一度この報酬枠を受け取ると、連続ログインのカウントはリセットされます。"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    // adminBonusRewardConditionSettingGui
    public static ItemStack bonusRewardConditionSettingTutorialBookIS(){
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "ボーナス枠について");
            meta.setLore(List.of(
                    ChatColor.WHITE + "【ボーナス枠条件設定について】────────────────",
                    ChatColor.of("#C2C2C2") + "ボーナス枠の報酬を受け取るために必要なログイン日数の割合を",
                    ChatColor.of("#C2C2C2") + "このGUIの名前欄に入力し、右の「保存する」をクリックすることで設定できます。",
                    ChatColor.of("#C2C2C2") + "" + ChatColor.BOLD + "・【ログインした日数（日）/ログインボーナスの開催期間（日）* 100】" + "" + ChatColor.RESET + "" + ChatColor.of("#C2C2C2") +  "で割合（百分率）を算出します。",
                    ChatColor.of("#C2C2C2") + "・算出された値が当項目で設定した割合以上のプレイヤーに対し、",
                    ChatColor.of("#C2C2C2") + "　開催期間最終日にボーナス枠の報酬を与えます。",
                    ChatColor.of("#C2C2C2") + " ",
                    ChatColor.of("#DF2E8F") + "" + ChatColor.BOLD + "【注意】受付可能な入力範囲：0~100（整数）"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack periodSettingClockIS() {
        ItemStack item = new ItemStack(Material.CLOCK, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "開催期間設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "当該ログインボーナスの開始日と終了日を設定します"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack dailyResetTimeSettingCompassIS() {
        ItemStack item = new ItemStack(Material.COMPASS, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "日付変更時刻設定");
            meta.setLore(List.of(
                    ChatColor.GRAY + "当該ログインボーナスの、「日付が変更したと判定する時刻」を設定します"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack periodSettingTutorialBookIS(){
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "開催期間設定について");
            meta.setLore(List.of(
                    ChatColor.of("#C2C2C2") + "当該ログインボーナスの開始日と終了日を設定します。",
                    ChatColor.of("#C2C2C2") + "名前欄に以下の形式に従って入力してください。",
                    ChatColor.GOLD + "" + ChatColor.BOLD + "【形式】(年)/(月)/(日)-(年)/(月)/(日)",
                    ChatColor.of("#C2C2C2") + "【注意事項】",
                    ChatColor.of("#C2C2C2") + "・前の年月日は開始日、後の年月日は終了日を表します。",
                    ChatColor.of("#C2C2C2") + "・(年)は西暦4桁、(月)と(日)は2桁で入力してください。",
                    ChatColor.of("#C2C2C2") + "・入力に空白は含めないでください。",
                    ChatColor.of("#C2C2C2") + "・日付変更時刻が0時以外に設定されている場合、その分だけ開催期間がずれます。",
                    ChatColor.of("#C2C2C2") + "　例えば、日付変更時刻が5時で、入力が以下の例の場合、",
                    ChatColor.of("#C2C2C2") + "　開催期間は2025/01/01 5:00 ~ 2025/02/01 4:59となります。",
                    ChatColor.of("#C2C2C2") + "・入力例:2025/01/01-2025/01/31"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack dailyResetTimeTutorialBookIS(){
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "日付変更時刻設定について");
            meta.setLore(List.of(
                    ChatColor.of("#C2C2C2") + "当該ログインボーナスの、「日付が変更したと判定する時刻」を設定します。",
                    ChatColor.of("#C2C2C2") + "名前欄に以下の形式に従って入力してください。",
                    ChatColor.GOLD + "" + ChatColor.BOLD + "【形式】(0~23の整数)",
                    ChatColor.of("#C2C2C2") + "【注意事項】",
                    ChatColor.of("#C2C2C2") + "・この設定は1時間単位で行えます。",
                    ChatColor.of("#C2C2C2") + "・例えば、この設定が5(時)の場合、",
                    ChatColor.of("#C2C2C2") + "　特定の日の5:00から翌日の4:59までが1日と見なされます。",
                    ChatColor.of("#C2C2C2") + "・入力例:5"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack defaultNamePaperIS(){
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("0~100の整数を入力(%)");
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack enabledEmeraldBlockIS(){
        ItemStack item = new ItemStack(Material.EMERALD_BLOCK, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "現在有効化中");
            meta.setLore(List.of(
                    ChatColor.GRAY + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.GRAY + "当該ログインボーナスを" + ChatColor.RED + "無効化" + ChatColor.GRAY + "します",
                    ChatColor.of("#797979") + "当該ログインボーナスが期間中開催されるようにするかどうかを設定します",
                    ChatColor.of("#797979") + "デフォルトでは無効化されており、全ての項目を設定した状態でないと有効化させることはできません"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack disabledRedStoneBlockIS(){
        ItemStack item = new ItemStack(Material.REDSTONE_BLOCK, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "現在無効化中");
            meta.setLore(List.of(
                    ChatColor.GRAY + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.GRAY + "当該ログインボーナスを" + ChatColor.GREEN + "有効化" + ChatColor.GRAY + "します",
                    ChatColor.of("#797979") + "当該ログインボーナスが期間中開催されるようにするかどうかを設定します",
                    ChatColor.of("#797979") + "デフォルトでは無効化されており、全ての項目を設定した状態でないと有効化させることはできません"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    /////////// グローバル設定 ////////////

    public static ItemStack recoverDateBrushIS(){
        ItemStack item = new ItemStack(Material.BRUSH, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "ログイン情報修正");
            meta.setLore(List.of(
                    ChatColor.GRAY + "指定した日付を、データベースに記録されているかに関わらず、",
                    ChatColor.GRAY + "このプラグインでは「プレイヤー全員がログインした」と見なすように設定します",
                    ChatColor.GRAY + "メンテナンスの影響でプレイヤーがログインを逃がした場合などに有効です"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack recoverDateTutorialBookIS(){
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "ログイン情報修正について");
            meta.setLore(List.of(
                    ChatColor.of("#C2C2C2") + "指定した日付を、データベースに記録されているかに関わらず、",
                    ChatColor.of("#C2C2C2") + "このプラグインでは「プレイヤー全員がログインした」と見なすように設定します。",
                    ChatColor.GOLD + "" + ChatColor.BOLD + "【形式】(年)/(月)/(日)",
                    ChatColor.of("#C2C2C2") + "【注意事項】",
                    ChatColor.of("#C2C2C2") + "・(年)は西暦4桁、(月)と(日)は2桁で入力してください。",
                    ChatColor.of("#C2C2C2") + "・入力に空白は含めないでください。",
                    ChatColor.of("#C2C2C2") + "・入力例:2025/01/01",
                    ChatColor.of("#C2C2C2") + " ",
                    ChatColor.of("#C2C2C2") + "【現在登録済みの日付】"
            ));

            ArrayList<LocalDate> existingDates = RewardManager.getRecoverLoginMissedDate();
            List<String> lore = meta.getLore();
            StringBuilder currentRow = new StringBuilder();

            for(int i = 0; i < existingDates.size(); i++) {
                currentRow.append(existingDates.get(i).toString().replace("-", "/"));

                // 3つごと、または最後の要素で改行
                if ((i + 1) % 3 == 0 || i == existingDates.size() - 1) {
                    lore.add(ChatColor.of("#C2C2C2") + currentRow.toString());
                    currentRow = new StringBuilder();
                } else {
                    currentRow.append(", ");
                }
            }
            meta.setLore(lore);

            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack subAccountSettingPlayerHeadIS(){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
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

    public static ItemStack subAccountSettingTutorialBookIS(){
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "サブアカウント設定について");
            meta.setLore(List.of(
                    ChatColor.of("#C2C2C2") + "個人が何アカウントまで特別枠/ボーナス枠/連続ログインボーナスの報酬を受け取れるか設定します。",
                    ChatColor.of("#C2C2C2") + "例えば、1アカウントまでサブ垢を許容する場合、本垢分も含めて入力する数値は「2」です。",
                    ChatColor.of("#C2C2C2") + "上の例の場合、個人が特別枠（10日目、20日目、...）／ボーナス枠（最終日）／連続ログボ報酬を",
                    ChatColor.of("#C2C2C2") + "それぞれ「2」回まで受け取れます。",
                    ChatColor.GOLD + "" + ChatColor.BOLD + "【形式】0以上の整数"
            ));

            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    //////////// ログボ編集用 /////////////
    public static ItemStack loginBonusNameChestIS(String bonusName, String period){
        ItemStack item = new ItemStack(Material.CHEST, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + bonusName);
            meta.setLore(List.of(
                    ChatColor.GRAY + "開催期間: " + period,
                    ChatColor.of("#797979") + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "当該ログインボーナスを編集します。開催期間中は時間関連の設定は編集できません",
                    ChatColor.RED + "" + ChatColor.BOLD + "[右クリック] " + ChatColor.RESET + ChatColor.RED + "当該ログインボーナスを削除します"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack noLoginBonusNamePaperIS(){
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "設定済みのログインボーナスはありません");
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack confirmDeleteRedGlassIS() {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "はい");
            meta.setLore(List.of(
                    ChatColor.RED + "" + ChatColor.BOLD + "クリックして削除を確定します",
                    ChatColor.RED + "" + ChatColor.BOLD + "※この操作は取り消せません"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack cancelDeleteGreenGlassIS() {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "いいえ");
            meta.setLore(List.of(
                    ChatColor.GREEN + "クリックしてキャンセルします"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }
    //////////// User用 /////////////
    public static ItemStack nextPageAquaPlayerHeadIS() {
        ItemStack item = getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjgyYWQxYjljYjRkZDIxMjU5YzBkNzVhYTMxNWZmMzg5YzNjZWY3NTJiZTM5NDkzMzgxNjRiYWM4NGE5NmUifX19");
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "次のページ");
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack prevPageAquaPlayerHeadIS() {
        ItemStack item = getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdhZWU5YTc1YmYwZGY3ODk3MTgzMDE1Y2NhMGIyYTdkNzU1YzYzMzg4ZmYwMTc1MmQ1ZjQ0MTlmYzY0NSJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "前のページ");
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack displayPageCountGlassIS(int currentPage, int maxPage) {
        ItemStack item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + currentPage + ChatColor.RESET + ChatColor.WHITE + "/" + maxPage + " ページ");
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack alreadyClaimedRewardForUserGlassIS(int day){
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "[DAY " + day + "] 無効な報酬");
            meta.setLore(List.of(
                    ChatColor.GRAY + "この報酬は受取済みであるか、受取可能な日を逃がしてしまっているため、受け取ることができません"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack normalRewardForUserPlayerHeadIS(int day, boolean canClaim){
        ItemStack item = getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MxYjJmNTkyY2ZjOGQzNzJkY2Y1ZmQ0NGVlZDY5ZGRkYzY0NjAxZDc4NDZkNzI2MTlmNzA1MTFkODA0M2E4OSJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "[DAY " + day + "] " + ChatColor.GRAY + "" + ChatColor.BOLD + "デイリー報酬（コモン）");
            if (canClaim) {
                meta.setLore(List.of(
                        ChatColor.GREEN + "" + ChatColor.BOLD + "受取可能",
                        ChatColor.GRAY + "ふつうの報酬をランダムに受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "報酬受取のための条件を満たしている時、報酬を受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[右クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "抽選の対象となるアイテムの一覧を表示します"

                ));
                meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
                meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            } else {
                meta.setLore(List.of(
                        ChatColor.GRAY + "ふつうの報酬をランダムに受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "報酬受取のための条件を満たしている時、報酬を受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[右クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "抽選の対象となるアイテムの一覧を表示します"
                ));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack specialRewardForUserPlayerHeadIS(int day, boolean canClaim){
        ItemStack item = getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQ2NzUxNThjMDc2N2VlNTA4YzUyZDUyNDI2Y2VmM2EyYzJiMjliN2U0ODdjOTI5NTNhMzgyM2Y1NjFkMDZhZiJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "[DAY " + day + "] " + ChatColor.AQUA + "" + ChatColor.BOLD + "デイリー報酬（レア）");
            if(canClaim){
                meta.setLore(List.of(
                        ChatColor.GREEN + "" + ChatColor.BOLD + "受取可能",
                        ChatColor.GRAY + "レアな報酬をランダムに受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "報酬受取のための条件を満たしている時、報酬を受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[右クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "抽選の対象となるアイテムの一覧を表示します"
                ));
                meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
                meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            } else {
                meta.setLore(List.of(
                        ChatColor.GRAY + "レアな報酬をランダムに受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "報酬受取のための条件を満たしている時、報酬を受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[右クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "抽選の対象となるアイテムの一覧を表示します"
                ));
                item.setItemMeta(meta);
            }
        }
        return item;
    }

    public static ItemStack bonusRewardForUserPlayerHeadIS(int day, boolean canClaim, int daysBetween, int loginCount, String currentBonusName){
        ItemStack item = getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTdiYzI1MWE2Y2IwZDZkOWYwNWM1NzExOTExYTZlYzI0YjIwOWRiZTY0MjY3OTAxYTRiMDM3NjFkZWJjZjczOCJ9fX0=");
        ItemMeta meta = item.getItemMeta();

        float loginRate = (float) loginCount * 100 / (float) daysBetween;
        loginRate = (float) Math.floor(loginRate * 100) / 100;

        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "[DAY " + day + "] " + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "デイリー報酬（エピック）");
            if(canClaim && loginRate >= RewardManager.getBonusRewardCondition(currentBonusName)){
                meta.setLore(List.of(
                        ChatColor.GREEN + "" + ChatColor.BOLD + "受取可能（現在のログイン率：" + loginRate + "%）",
                        ChatColor.GRAY + "豪華な報酬をランダムに受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "報酬受取のための条件を満たしている時、報酬を受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[右クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "抽選の対象となるアイテムの一覧を表示します"
                ));
                meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
                meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            } else if(canClaim) {
                meta.setLore(List.of(
                        ChatColor.RED + "" + ChatColor.BOLD + "受取不可（現在のログイン率：" + loginRate + "%）",
                        ChatColor.RED + "" + ChatColor.BOLD + "この報酬の受取にはログイン率" + ChatColor.UNDERLINE + RewardManager.getBonusRewardCondition(currentBonusName) + "%" + ChatColor.RESET + "" + ChatColor.RED + "" + ChatColor.BOLD + "が必要です",
                        ChatColor.GRAY + "豪華な報酬をランダムに受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "報酬受取のための条件を満たしている時、報酬を受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[右クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "抽選の対象となるアイテムの一覧を表示します"
                ));
            } else if(loginRate >= RewardManager.getBonusRewardCondition(currentBonusName)) {
                meta.setLore(List.of(
                        ChatColor.GREEN + "" + ChatColor.BOLD + "受取条件達成中（現在のログイン率：" + loginRate + "%） " + ChatColor.RESET + ChatColor.WHITE + "" + ChatColor.BOLD + "/ 必要：" + RewardManager.getBonusRewardCondition(currentBonusName) + "%",
                        ChatColor.GRAY + "豪華な報酬をランダムに受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "報酬受取のための条件を満たしている時、報酬を受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[右クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "抽選の対象となるアイテムの一覧を表示します"
                ));
            } else {
                meta.setLore(List.of(
                        ChatColor.RED + "" + ChatColor.BOLD + "受取条件未達（現在のログイン率：" + loginRate + "%） " + ChatColor.RESET + ChatColor.WHITE + "" + ChatColor.BOLD + "/ 必要：" + RewardManager.getBonusRewardCondition(currentBonusName) + "%",
                        ChatColor.GRAY + "豪華な報酬をランダムに受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "報酬受取のための条件を満たしている時、報酬を受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[右クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "抽選の対象となるアイテムの一覧を表示します"
                ));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack loginStreakShowingPaperIS(int loginStreak){
        ItemStack item = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "現在の連続ログイン日数：" + loginStreak + "日");
            meta.setLore(List.of(
                    ChatColor.GRAY + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.GRAY + "連続ログインボーナス受取画面を開く"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack alreadyClaimedContinuousRewardForUserGlassIS(){
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "無効な報酬");
            meta.setLore(List.of(
                    ChatColor.RED + "この報酬は受取済みです",
                    ChatColor.GRAY + "明日以降にログインすると連続ログインボーナスの進捗が戻り、",
                    ChatColor.GRAY + "再度10日連続でログインすることで、この報酬を受け取ることができます"
            ));
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack continuousRewardForUserPlayerHeadIS(int streak, int needDayToClaim){
        ItemStack item = getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTdiODMyNGQxYjI5ZWZkNjY3YmZiZDY5MTM0ZTdkODkxNzExMDc5MWI4MWJhZjgwZmRiNmUyYzIwN2FmZTE4MSJ9fX0=");
        ItemMeta meta = item.getItemMeta();



        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "連続ログイン報酬");
            if(streak >= needDayToClaim){
                meta.setLore(List.of(
                        ChatColor.GREEN + "" + ChatColor.BOLD + "受取可能",
                        ChatColor.GRAY + "連続ログイン報酬をランダムに受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "報酬受取のための条件を満たしている時、報酬を受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[右クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "抽選の対象となるアイテムの一覧を表示します"
                ));
                meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
                meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            } else {
                meta.setLore(List.of(
                        ChatColor.RED + "" + ChatColor.BOLD + "受取不可（現在の連続ログイン日数：" + streak + "日）",
                        ChatColor.RED + "" + ChatColor.BOLD + "この報酬の受取にはログイン率" + needDayToClaim + "日" + ChatColor.RESET + "" + ChatColor.RED + "" + ChatColor.BOLD + "が必要です",
                        ChatColor.GRAY + "連続ログイン報酬をランダムに受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[左クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "報酬受取のための条件を満たしている時、報酬を受け取れます",
                        ChatColor.of("#797979") + "" + ChatColor.BOLD + "[右クリック] " + ChatColor.RESET + ChatColor.of("#797979") + "抽選の対象となるアイテムの一覧を表示します"
                ));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack poolForUserTutorialBookIS() {
        ItemStack item = new ItemStack(Material.BOOK, 1);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "報酬プールについて");
            meta.setLore(List.of(
                    ChatColor.of("#C2C2C2") + "・各スロットのうち、いずれか1スロットが報酬として与えられます",
                    ChatColor.of("#C2C2C2") + "・報酬を受け取る際、各スロットの" + ChatColor.GOLD + ChatColor.BOLD + "排出確率が等分" + ChatColor.RESET + ChatColor.of("#C2C2C2") + "されて抽選されます"
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

    public static ItemStack saveLimeGlassIS() {
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

    public static ItemStack returnLimeGlassIS() {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "前に戻る");
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true); // 発光効果（効果なし）
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemStack cancelRedGlassIS(){
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "保存せず前に戻る");
            meta.setLore(List.of(
                    ChatColor.RED + "[左クリック] 保存せず前に戻る"
            ));
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
