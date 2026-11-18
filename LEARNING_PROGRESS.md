
# Learning Progress

## 学習目的

Java / Spring Bootを中心に、既存コードの読解、仕様理解、修正、テストの能力を身につける。

対象リポジトリ：
- Backend: https://github.com/sakuma-s/health-tracker
- Frontend: https://github.com/sakuma-s/health-tracker-front

## 理解度の評価基準

- A：コードを参照しながら、ヒントなしで処理を正確に説明でき、応用質問にも正解できる
- B：ヒントがあれば説明できる、または一部に理解不足がある
- C：解説後も処理を正確に説明できない
- 未評価：理解度を確認していない

## 学習進捗


| 機能 | 対象ファイル | 理解度 | 最終学習日 | 復習予定 |
|---|---|---|---|---|
| AWS Lightsail本番デプロイ | 本番JAR・health-tracker.service | 未評価 | 2026-11-18 | 未定 |

### 2026-11-18 本番デプロイ結果

- 対象：検索結果の睡眠時間表示不具合
- 関連PR：Frontend #9、Backend #32
- 実施：JARのバックアップ・転送・SHA-256照合・切り替え
- systemdサービスの再起動：成功
- 本番検索機能の動作確認：成功
- 結果：本番デプロイ完了

## 復習結果

学習開始後に記録する。

## 未解決の疑問・課題

GitHub Issuesで管理する。
- AWS Lightsailで `journalctl -u health-tracker.service`
  を実行しても `-- No entries --` と表示される。
  journald全体は正常動作しているが、原因は未解決。

## 次回の学習候補

初回の理解度診断後に決定する。
