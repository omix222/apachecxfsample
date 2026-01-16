# Apache CXF SOAP Web Service Sample

JavaでApache CXFを使用したSOAP Webサービスのサンプルアプリケーションです。
プロバイダ（サーバー）とリクエスタ（クライアント）の2つのモジュールで構成され、**WSDL駆動開発**を実践しています。

## プロジェクトの特徴

- **マルチモジュール構成**: ProviderとRequesterを分離し、関心事の分離を実現
- **WSDL管理**: ProviderのWSDLをGit管理し、契約駆動開発を実現
- **自動コード生成**: WSDLからクライアントコードを自動生成

## プロジェクト構成

```
cxfsample/
├── pom.xml                              # 親POM（マルチモジュール設定）
├── README.md                            # このファイル
├── provider/                            # プロバイダモジュール（サーバー）
│   ├── pom.xml                          # Provider用POM
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/example/soap/
│           │       ├── HelloWorld.java       # サービスインターフェース
│           │       ├── HelloWorldImpl.java   # サービス実装
│           │       └── Server.java           # サーバー起動クラス
│           └── resources/
│               └── wsdl/
│                   └── HelloWorld.wsdl       # WSDL (Git管理)
└── requester/                           # リクエスタモジュール（クライアント）
    ├── pom.xml                          # Requester用POM
    └── src/
        └── main/
            └── java/
                └── com/example/soap/
                    └── Client.java           # クライアント実行クラス
```

## 必要要件

- Java 11以上
- Maven 3.6以上

## セットアップとビルド

### 1. プロジェクト全体のビルド

```bash
mvn clean install
```

このコマンドは以下を実行します：
- Providerモジュールのコンパイル
- Requesterモジュールで、ProviderのWSDLからクライアントコードを自動生成
- 両モジュールのビルドとパッケージング

## 使い方

### ステップ1: サーバー（プロバイダ）の起動

別のターミナルウィンドウでサーバーを起動します：

```bash
mvn -pl provider exec:java
```

サーバーが起動すると以下のメッセージが表示されます：

```
Starting Apache CXF SOAP Server...
Server started successfully!
Service available at: http://localhost:9090/HelloWorld
WSDL available at: http://localhost:9090/HelloWorld?wsdl

Press Ctrl+C to stop the server...
```

### ステップ2: WSDLの確認（オプション）

ブラウザまたはcurlでWSDLを確認できます：

```bash
curl http://localhost:9090/HelloWorld?wsdl
```

または、ブラウザで以下のURLにアクセス：
```
http://localhost:9090/HelloWorld?wsdl
```

### ステップ3: クライアント（リクエスタ）の実行

別のターミナルウィンドウでクライアントを実行します：

```bash
mvn -pl requester exec:java
```

## Webサービスのメソッド

このサンプルでは以下の3つのメソッドを提供しています：

### 1. sayHello(String name)
名前を受け取り、挨拶メッセージを返します。

**入力:**
- name: 挨拶する名前（String）

**出力:**
- 挨拶メッセージ（String）

### 2. add(int a, int b)
2つの整数を受け取り、合計を返します。

**入力:**
- a: 第一の整数
- b: 第二の整数

**出力:**
- a + b の合計（int）

### 3. getUserInfo(String userId)
ユーザーIDを受け取り、ユーザー情報を返します。

**入力:**
- userId: ユーザーID（String）

**出力:**
- ユーザー情報（String）

## 実行例

クライアントを実行すると以下のような出力が得られます：

```
Apache CXF SOAP Client (WSDL-Generated)
========================================

Test 1: Calling sayHello("Takahashi")
---------------------------
Response: Hello, Takahashi! Welcome to Apache CXF SOAP Web Service.

Test 2: Calling sayHello(null)
---------------------------
Response: Hello, Guest!

Test 3: Calling add(10, 20)
---------------------------
Response: 10 + 20 = 30

Test 4: Calling getUserInfo("12345")
---------------------------
Response:
User Information:
  User ID: 12345
  Name: User_12345
  Status: Active
  Created: 2024-01-01

Test 5: Multiple operations
---------------------------
1. Hello, User1! Welcome to Apache CXF SOAP Web Service.
2. Hello, User2! Welcome to Apache CXF SOAP Web Service.
3. Hello, User3! Welcome to Apache CXF SOAP Web Service.

===========================================
All tests completed successfully!
This client was generated from the WSDL file
===========================================
```

## 開発ワークフロー

### WSDLの更新

プロバイダ側のインターフェースを変更した場合：

1. `HelloWorld.java` または `HelloWorldImpl.java` を編集
2. サーバーを一時的に起動してWSDLを再取得：

```bash
# サーバーを起動
mvn -pl provider exec:java &
sleep 5

# WSDLをダウンロード
curl -o provider/src/main/resources/wsdl/HelloWorld.wsdl 'http://localhost:9090/HelloWorld?wsdl'

# サーバーを停止
pkill -f "com.example.soap.Server"
```

3. WSDLをGitにコミット
4. 変更をリクエスタに反映：

```bash
mvn clean install
```

### クライアントコードの再生成

WSDLを更新した後、クライアントコードを再生成するには：

```bash
mvn -pl requester clean generate-sources
```

## トラブルシューティング

### クライアント実行時にエラーが発生する場合

エラーメッセージ：
```
Error calling web service:
  Message: Connection refused
```

**解決方法:**
- サーバーが起動していることを確認してください
- ポート9090が他のアプリケーションで使用されていないか確認してください

### ポート変更方法

1. `provider/src/main/java/com/example/soap/Server.java` で `SERVICE_ADDRESS` を変更
2. サーバーを再起動してWSDLを再生成
3. WSDLを更新してリクエスタを再ビルド

### ビルドエラー

```bash
# クリーンビルド
mvn clean install

# 特定のモジュールのみビルド
mvn -pl provider clean compile
mvn -pl requester clean compile
```

## カスタマイズ

### 新しいメソッドの追加

#### Provider側（サーバー）

1. `provider/src/main/java/com/example/soap/HelloWorld.java` インターフェースに新しいメソッドを追加：

```java
@WebMethod
String getCurrentTime();
```

2. `provider/src/main/java/com/example/soap/HelloWorldImpl.java` に実装を追加：

```java
@Override
public String getCurrentTime() {
    return new java.util.Date().toString();
}
```

3. WSDLを再生成（上記の「WSDLの更新」セクションを参照）

#### Requester側（クライアント）

1. プロジェクトを再ビルド：

```bash
mvn clean install
```

2. `requester/src/main/java/com/example/soap/Client.java` で新しいメソッドを呼び出し：

```java
String time = client.getCurrentTime();
System.out.println("Server time: " + time);
```

## プロジェクトの利点

### WSDL駆動開発のメリット

1. **契約ファースト**: WSDLが正式な契約として機能
2. **バージョン管理**: WSDLの変更履歴を追跡可能
3. **自動生成**: クライアントコードを手動で書く必要がない
4. **型安全性**: WSDLから生成されたコードは型安全
5. **ドキュメント**: WSDLがそのままAPIドキュメントとして機能

### マルチモジュール構成のメリット

1. **分離**: ProviderとRequesterの関心事を分離
2. **再利用**: 複数のクライアントアプリケーションで共通のProviderを使用可能
3. **独立ビルド**: 各モジュールを個別にビルド・テスト可能
4. **明確な依存関係**: モジュール間の依存関係が明確

## Apache CXFについて

Apache CXFは、Webサービス（SOAPおよびRESTful）を開発するためのオープンソースフレームワークです。

主な特徴：
- JAX-WS（SOAP）とJAX-RS（REST）のサポート
- WSDLからのコード生成（wsdl2java）
- JavaからのWSDL生成（java2wsdl）
- さまざまなプロトコルとトランスポートのサポート
- Spring Frameworkとの統合

公式サイト: https://cxf.apache.org/

## ライセンス

このサンプルコードはMITライセンスの下で公開されています。
