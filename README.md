# Apache CXF SOAP Web Service Sample

JavaでApache CXFを使用したSOAP Webサービスのサンプルアプリケーションです。
リクエスタ（クライアント）とプロバイダ（サーバー）の両方が含まれています。

## プロジェクト構成

```
cxfsample/
├── pom.xml                              # Maven設定ファイル
├── README.md                            # このファイル
└── src/
    └── main/
        └── java/
            └── com/
                └── example/
                    └── soap/
                        ├── HelloWorld.java       # Webサービスインターフェース
                        ├── HelloWorldImpl.java   # Webサービス実装
                        ├── Server.java           # プロバイダ（サーバー）
                        └── Client.java           # リクエスタ（クライアント）
```

## 必要要件

- Java 11以上
- Maven 3.6以上

## セットアップ

### 1. プロジェクトのビルド

```bash
mvn clean compile
```

## 使い方

### ステップ1: サーバー（プロバイダ）の起動

別のターミナルウィンドウでサーバーを起動します：

```bash
mvn exec:java -Dexec.mainClass="com.example.soap.Server"
```

または：

```bash
mvn exec:java@run-server
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
mvn exec:java -Dexec.mainClass="com.example.soap.Client"
```

または：

```bash
mvn exec:java@run-client
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
Apache CXF SOAP Client
=====================

Test 1: Calling sayHello()
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

All tests completed successfully!
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

`Server.java` と `Client.java` の `SERVICE_ADDRESS` を変更してください：

```java
private static final String SERVICE_ADDRESS = "http://localhost:8080/HelloWorld";
```

## カスタマイズ

### 新しいメソッドの追加

1. `HelloWorld.java` インターフェースに新しいメソッドを追加
2. `HelloWorldImpl.java` に実装を追加
3. サーバーを再起動
4. クライアントから新しいメソッドを呼び出し

### 例：

```java
// HelloWorld.java
@WebMethod
String getCurrentTime();

// HelloWorldImpl.java
@Override
public String getCurrentTime() {
    return new java.util.Date().toString();
}

// Client.java
String time = client.getCurrentTime();
System.out.println("Server time: " + time);
```

## Apache CXFについて

Apache CXFは、Webサービス（SOAPおよびRESTful）を開発するためのオープンソースフレームワークです。

主な特徴：
- JAX-WS（SOAP）とJAX-RS（REST）のサポート
- WSDLからのコード生成
- さまざまなプロトコルとトランスポートのサポート
- Spring Frameworkとの統合

公式サイト: https://cxf.apache.org/

## ライセンス

このサンプルコードはMITライセンスの下で公開されています。
