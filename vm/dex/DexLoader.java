package vm.dex;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DexLoader {
    private String filePath;
    private byte[] dexData;

    public DexLoader(String filePath) {
        this.filePath = filePath;
    }

    public void load() throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        dexData = fis.readAllBytes();
        fis.close();

        if (!isValidDex()) {
            throw new IOException("Invalid DEX file format.");
        }

        // 最低限、DEXヘッダーの解析やクラスIDリストの読み込みをここで実装予定
    }

    private boolean isValidDex() {
        // DEXファイルは先頭に "dex\n035\0" のマジックヘッダーがある
        if (dexData.length < 8) return false;
        String magic = new String(dexData, 0, 8);
        return magic.startsWith("dex\n035");
    }

    // 擬似的にクラス名リストを抽出して表示する例（本格的解析は別途実装）
    public void printClassNames() {
        // 本来は string_ids, type_ids などから読み込むが
        // ここではとりあえずファイルサイズだけ表示
        System.out.println("[*] DEX file size: " + dexData.length + " bytes");
        System.out.println("[*] (Class names extraction to be implemented)");
    }
}
