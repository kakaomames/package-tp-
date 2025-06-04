#!/bin/bash

echo "[*] Java 環境の確認..."
if type -p java; then
    echo "  Java found: $(java -version 2>&1 | head -n 1)"
else
    echo "  Java not found. Installing..."
    sudo apt update && sudo apt install -y openjdk-17-jdk
fi

echo "[*] Python3 の確認..."
if ! command -v python3 &> /dev/null; then
    echo "  Python3 not found. Installing..."
    sudo apt install -y python3 python3-pip
else
    echo "  Python3 found: $(python3 --version)"
fi

echo "[*] 必要CLIツールのインストール..."
sudo apt install -y wget unzip git

# apktool の取得
echo "[*] apktool を取得中..."
mkdir -p tools && cd tools
wget -nc https://raw.githubusercontent.com/iBotPeaches/Apktool/master/scripts/linux/apktool
wget -nc https://bitbucket.org/iBotPeaches/apktool/downloads/apktool_2.9.3.jar -O apktool.jar
chmod +x apktool
cd ..

# dex2jar の取得
echo "[*] dex2jar を取得中..."
wget -nc https://github.com/pxb1988/dex2jar/releases/download/2.1/dex2jar-2.1.zip
unzip -n dex2jar-2.1.zip -d tools/dex2jar

echo "[✔] 環境構築完了"
