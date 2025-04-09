# LoginBonusPlugin

サーバーに導入することで、インベントリGUIを用いたログインボーナス機能を追加できます

<img width="45%" alt="screenshot1" src="https://github.com/user-attachments/assets/f2a442fa-83b8-4120-885f-a7068fa98199">
<img width="45%" alt="screenshot1" src="https://github.com/user-attachments/assets/5df02ed1-c661-49ef-806a-07a350a931d8">


## 特徴

### 管理者側

- すべての設定がGUIで完結
- 報酬のアイテムを自由にカスタム可能
- 日付変更時刻を設定可能
- 特定の報酬を受取可能なサブアカウント数を設定可能
- メンテナンス等でプレイヤーのログインが妨げられた場合に、ログイン履歴を修正可能

### プレイヤー側

- GUIクリックで簡単に報酬を受取可能
- 報酬受取条件等のルールを参照可能
- 報酬プールのプレビューが可能

## 使い方

### コマンド
- `/loginbonus[lb]` - 累計ログインボーナスGUIを表示
- `/loginbonus[lb] total` - 累計ログインボーナスGUIを表示
- `/loginbonus[lb] streak` - 連続ログインボーナスGUIを表示
- `/loginbonus[lb] admin` - 管理者用GUIを表示。ログボを作成・編集できます【op所持者のみ実行可】
- `/loginbonus[lb] help` - ヘルプを表示\
（[]：括弧内の書き方に代替可能）

## インストール（ひとまずman10用）

1. プラグインのJARファイルをダウンロードします。
2. ダウンロードしたJARファイルをサーバーの`plugins`フォルダにコピーします。
3. 報酬の受取履歴を格納するためのデータベースを1つ作成します。（データベース名の例：`loginbonus_info`）
4. サーバーを（再）起動し、接続先のデータベースの情報を変更する必要がある場合は、`LoginBonusPlugin/Config.yml`を書き換えます。`mysql_loginbonus_info`には、手順3で設定したデータベースの情報を入力してください。`mysql_connection_data`には、プレイヤーのログイン履歴を格納しているテーブルを持つデータベースの情報を入力してください。（既に存在しているとのことです）\
5. サーバーを再起動します。

## 依存プラグイン
- [Man10Score](https://github.com/forest611/Man10Score)
