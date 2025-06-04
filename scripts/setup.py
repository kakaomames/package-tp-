#!/usr/bin/env python3
import subprocess
import sys

print("[*] Python モジュールのインストール...")

modules = ["axmlparserpy"]

for module in modules:
    subprocess.run([sys.executable, "-m", "pip", "install", module])

print("[✔] Python 環境構築完了")
