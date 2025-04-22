package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LoginBonusTabCompleter implements TabCompleter {

    private final List<String> commonSubCommands = Arrays.asList("help", "total", "streak");
    private final List<String> adminSubCommands = Arrays.asList("admin", "status");
    private final List<String> statusOptions = Arrays.asList("on", "off");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("loginbonus")) {
            if (args.length == 1) {
                // 最初の引数のサブコマンド一覧
                completions.addAll(commonSubCommands);

                // admin権限を持っている場合のみadminサブコマンドを表示
                if (sender.hasPermission("loginbonusplugin.op")) {
                    completions.addAll(adminSubCommands);
                }

                // 入力された部分文字列でフィルタリング
                return filterCompletions(completions, args[0]);
            } else if (args.length == 2) {
                // 2つ目の引数の補完
                if (args[0].equalsIgnoreCase("status") && sender.hasPermission("loginbonusplugin.op")) {
                    completions.addAll(statusOptions);
                    return filterCompletions(completions, args[1]);
                }
            }
        }

        return completions;
    }

    // 入力中の文字列で始まる候補だけをフィルタリングするヘルパーメソッド
    private List<String> filterCompletions(List<String> completions, String input) {
        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }
}