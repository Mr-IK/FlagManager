[![](https://jitpack.io/v/Mr-IK/FlagManager.svg)](https://jitpack.io/#Mr-IK/FlagManager)
# FlagManager 使い方

###導入
####Maven
```pom
    <repository>
       <id>jitpack.io</id>
       <url>https://jitpack.io</url>
    </repository>

    <dependency>
        <groupId>com.github.Mr-IK</groupId>
        <artifactId>FlagManager</artifactId>
        <version>1.0.1</version>
    </dependency>
```
####Gradle
```gradle
maven { url 'https://jitpack.io' }

implementation 'com.github.Mr-IK:FlagManager:1.0.1'
```

###使用方法

####Java

```java
//フラグを上げる(onにする)
FlagAPI.playerFlagUp(player, "testflag");

//フラグを下げる(offにする)
FlagAPI.playerFlagDown(player, "testflag");

//フラグの状況(onの場合true、offでfalse)
if(FlagAPI.isPlayerHasFlag(player,"testflag")){
  //hogehoge
}

//カウンターを取得(存在しない場合-1、念のためhasCountを使用すること)
int count = FlagAPI.getPlayerCount(player,"testcount");

//カウンターを作成(既に存在している場合実行されない)
FlagAPI.createPlayerCount(player,"testcount",3);

//カウンターをアップデート(存在しない場合実行されない)
FlagAPI.updatePlayerCount(player,"testcount",6);

//カウンターを作成/アプデ(存在しないと作成、してたらアプデする)
FlagAPI.forceUpdatePlayerCount(player,"testcount",10);

//カウンタの状況(存在する場合true、しないとfalse)
if(FlagAPI.isPlayerHasCount(player,"testcount")){
  //hogehoge
}
```
####Kotlin
```kotlin
FlagAPI.playerFlagUp(player, "testflag")

//フラグを下げる(offにする)
FlagAPI.playerFlagDown(player, "testflag")

//フラグの状況(onの場合true、offでfalse)
if (FlagAPI.isPlayerHasFlag(player, "testflag")) {
   //hogehoge
}

//カウンターを取得(存在しない場合-1、念のためhasCountを使用すること)
val count: Int = FlagAPI.getPlayerCount(player, "testcount")

//カウンターを作成(既に存在している場合実行されない)
FlagAPI.createPlayerCount(player, "testcount", 3)

//カウンターをアップデート(存在しない場合実行されない)
FlagAPI.updatePlayerCount(player, "testcount", 6)

//カウンターを作成/アプデ(存在しないと作成、してたらアプデする)
FlagAPI.forceUpdatePlayerCount(player, "testcount", 10)

//カウンタの状況(存在する場合true、しないとfalse)
if (FlagAPI.isPlayerHasCount(player, "testcount")) {
//hogehoge
}
```