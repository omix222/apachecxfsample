# Apache CXF SOAP Web Service Sample

JavaでApache CXFを使用したSOAP Webサービスのサンプルアプリケーションです。
プロバイダ（サーバー）とリクエスタ（クライアント）の2つのモジュールで構成され、**WSDL駆動開発**を実践しています。

## プロジェクトの特徴

- **マルチモジュール構成**: ProviderとRequesterを分離し、関心事の分離を実現
- **WSDL管理**: ProviderとRequester両方でWSDLをGit管理し、契約駆動開発を実現
- **自動コード生成**: RequesterのローカルWSDLからクライアントコードを自動生成
- **独立したビルド**: Requesterは独立したプロジェクトとしてビルド可能
- **WildFly Bootable JAR**: RequesterはWildFly 31 Bootable JARとしてパッケージング可能で、Jakarta EE 10対応のWebアプリケーションとして動作

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
│                   └── HelloWorld.wsdl       # WSDL (Git管理・マスター)
└── requester/                           # リクエスタモジュール（WildFly Bootable JAR）
    ├── pom.xml                          # Requester用POM（WAR + Bootable JAR）
    └── src/
        └── main/
            ├── java/
            │   └── com/example/soap/
            │       ├── RestApplication.java      # JAX-RS アプリケーション設定
            │       └── HelloWorldResource.java   # REST APIエンドポイント
            ├── resources/
            │   └── wsdl/
            │       └── HelloWorld.wsdl       # WSDL (Git管理・コピー)
            └── webapp/
                ├── index.html                # Web UI
                └── WEB-INF/
                    ├── beans.xml             # CDI設定
                    ├── jboss-web.xml         # JBoss設定
                    └── web.xml               # Web設定
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
- Requesterモジュールで、ローカルのWSDLファイルからクライアントコードを自動生成
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

### ステップ3: クライアント（リクエスタ）の実行 - WildFly Bootable JAR版

RequesterはWildFly 31 Bootable JARとして動作するWebアプリケーションとして実装されています。

#### Bootable JARのビルド

```bash
mvn clean package -pl requester
```

このコマンドにより、以下が生成されます：
- `requester/target/cxf-soap-requester.war` - 通常のWARファイル
- `requester/target/cxf-soap-requester-bootable.jar` - WildFly Bootable JAR (約130MB)

#### Bootable JARの起動

```bash
java -jar requester/target/cxf-soap-requester-bootable.jar
```

サーバーが起動すると、以下のURLでアクセスできます：

- **Web UI**: http://localhost:8080/
- **ヘルスチェック**: http://localhost:8080/rest/api/health
- **挨拶メッセージ**: http://localhost:8080/rest/api/hello?name=Takahashi
- **加算**: http://localhost:8080/rest/api/add?a=10&b=20
- **ユーザー情報**: http://localhost:8080/rest/api/user/12345
- **全テスト実行**: http://localhost:8080/rest/api/test/all

#### REST APIレスポンス例

```bash
# ヘルスチェック
curl http://localhost:8080/rest/api/health
# => {"status":"UP","service":"SOAP Client (WildFly Bootable JAR)"}

# 挨拶メッセージ
curl "http://localhost:8080/rest/api/hello?name=Takahashi"
# => {"success":true,"input":"Takahashi","message":"Hello, Takahashi! Welcome to Apache CXF SOAP Web Service."}

# 全テスト実行
curl http://localhost:8080/rest/api/test/all
# => すべてのSOAPメソッドのテスト結果をJSON形式で返します
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

# WSDLをダウンロード（Provider側）
curl -o provider/src/main/resources/wsdl/HelloWorld.wsdl 'http://localhost:9090/HelloWorld?wsdl'

# サーバーを停止
pkill -f "com.example.soap.Server"
```

3. **WSDLをRequester側にコピー**：

```bash
# Provider → Requesterへコピー
cp provider/src/main/resources/wsdl/HelloWorld.wsdl requester/src/main/resources/wsdl/
```

4. 両方のWSDLをGitにコミット
5. 変更をリクエスタに反映：

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
2. **独立したプロジェクト**: Requesterは自身のWSDLを持ち、単独でビルド可能
3. **再利用**: 複数のクライアントアプリケーションで共通のProviderを使用可能
4. **独立ビルド**: 各モジュールを個別にビルド・テスト可能
5. **明確な依存関係**: ビルド時にProvider側への依存なし

## Apache CXFについて

Apache CXFは、Webサービス（SOAPおよびRESTful）を開発するためのオープンソースフレームワークです。

主な特徴：
- JAX-WS（SOAP）とJAX-RS（REST）のサポート
- WSDLからのコード生成（wsdl2java）
- JavaからのWSDL生成（java2wsdl）
- さまざまなプロトコルとトランスポートのサポート
- Spring Frameworkとの統合

公式サイト: https://cxf.apache.org/

## WildFly Bootable JARについて

RequesterモジュールはWildFly 31 Bootable JARとして実装されています。

### 技術スタック

- **アプリケーションサーバー**: WildFly 31.0.0.Final
- **Jakarta EE**: 10.0
- **Apache CXF**: 4.0.5 (Jakarta EE互換)
- **JAX-RS**: REST APIエンドポイント
- **CDI**: 依存性注入
- **パッケージング**: Bootable JAR（単一実行可能JAR）

### アーキテクチャ

1. **REST APIレイヤー** (`HelloWorldResource.java`)
   - JAX-RSエンドポイントを提供
   - SOAPクライアントをラップしてJSON形式で公開
   - エラーハンドリングとレスポンス整形

2. **SOAP クライアントレイヤー**
   - WSDLから自動生成されたクライアントコード
   - Jakarta EE `jakarta.xml.ws.*` パッケージを使用（CXF 4.x）

3. **Web UIレイヤー**
   - HTML/CSSによるシンプルなインターフェース
   - 各エンドポイントへのリンク

### Galleon レイヤー構成

WildFly Bootable JARは以下のGalleonレイヤーを使用：

- `jaxrs-server`: JAX-RS (REST API) サポート
- `webservices`: JAX-WS (SOAP) クライアントサポート
- `cdi`: CDI (Contexts and Dependency Injection)

これにより、必要最小限のWildFlyランタイムのみをパッケージングし、約130MBのBootable JARを実現しています。

### デプロイメント

Bootable JARは以下の環境にデプロイ可能です：

- ローカル開発環境（`java -jar`で実行）
- コンテナ環境（Docker, Kubernetes）
- クラウド環境（AWS, Azure, GCP）

Dockerイメージ例：
```dockerfile
FROM eclipse-temurin:17-jre
COPY requester/target/cxf-soap-requester-bootable.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]
```

## ライセンス

このサンプルコードはMITライセンスの下で公開されています。


## tasks.json 
```
{
    "version": "2.0.0",
    "tasks": [
        {
            "label": "Build All",
            "type": "shell",
            "command": "mvn",
            "args": ["clean", "package", "-DskipTests"],
            "group": {
                "kind": "build",
                "isDefault": true
            },
            "problemMatcher": "$mvn",
            "presentation": {
                "reveal": "always",
                "panel": "new"
            }
        },
        {
            "label": "Build Provider",
            "type": "shell",
            "command": "mvn",
            "args": ["clean", "package", "-DskipTests"],
            "options": {
                "cwd": "${workspaceFolder}/provider"
            },
            "problemMatcher": "$mvn",
            "presentation": {
                "reveal": "always",
                "panel": "new"
            }
        },
        {
            "label": "Build Requester",
            "type": "shell",
            "command": "mvn",
            "args": ["clean", "package", "-DskipTests"],
            "options": {
                "cwd": "${workspaceFolder}/requester"
            },
            "problemMatcher": "$mvn",
            "presentation": {
                "reveal": "always",
                "panel": "new"
            }
        },
        {
            "label": "Run Provider (SOAP Server)",
            "type": "shell",
            "command": "mvn",
            "args": ["exec:java"],
            "options": {
                "cwd": "${workspaceFolder}/provider"
            },
            "isBackground": true,
            "problemMatcher": {
                "pattern": {
                    "regexp": "^$"
                },
                "background": {
                    "activeOnStart": true,
                    "beginsPattern": "^.*$",
                    "endsPattern": "^.*Server started at.*$"
                }
            },
            "presentation": {
                "reveal": "always",
                "panel": "dedicated",
                "clear": true
            }
        },
        {
            "label": "Run Requester (WildFly)",
            "type": "shell",
            "command": "java",
            "args": ["-jar", "target/cxf-soap-requester-bootable.jar"],
            "options": {
                "cwd": "${workspaceFolder}/requester"
            },
            "isBackground": true,
            "problemMatcher": {
                "pattern": {
                    "regexp": "^$"
                },
                "background": {
                    "activeOnStart": true,
                    "beginsPattern": "^.*$",
                    "endsPattern": "^.*WFLYSRV0025.*$"
                }
            },
            "presentation": {
                "reveal": "always",
                "panel": "dedicated",
                "clear": true
            }
        },
        {
            "label": "Build & Run Provider",
            "dependsOn": ["Build Provider", "Run Provider (SOAP Server)"],
            "dependsOrder": "sequence",
            "problemMatcher": []
        },
        {
            "label": "Build & Run Requester",
            "dependsOn": ["Build Requester", "Run Requester (WildFly)"],
            "dependsOrder": "sequence",
            "problemMatcher": []
        },
        {
            "label": "Build & Run All Servers",
            "dependsOn": ["Build All"],
            "dependsOrder": "sequence",
            "problemMatcher": []
        },
        {
            "label": "Test All",
            "type": "shell",
            "command": "mvn",
            "args": ["test"],
            "group": {
                "kind": "test",
                "isDefault": true
            },
            "problemMatcher": "$mvn",
            "presentation": {
                "reveal": "always",
                "panel": "new"
            }
        },
        {
            "label": "Clean All",
            "type": "shell",
            "command": "mvn",
            "args": ["clean"],
            "problemMatcher": "$mvn",
            "presentation": {
                "reveal": "always",
                "panel": "new"
            }
        },
        {
            "label": "Open Requester (Browser)",
            "type": "shell",
            "command": "open",
            "args": ["http://localhost:8080/"],
            "problemMatcher": [],
            "presentation": {
                "reveal": "silent",
                "panel": "shared"
            }
        },
        {
            "label": "Open Provider WSDL (Browser)",
            "type": "shell",
            "command": "open",
            "args": ["http://localhost:9090/HelloWorld?wsdl"],
            "problemMatcher": [],
            "presentation": {
                "reveal": "silent",
                "panel": "shared"
            }
        }
    ]
}
