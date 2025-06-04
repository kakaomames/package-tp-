package vm.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * 簡易版 バイナリAXMLパーサー
 * AndroidManifest.xml のバイナリXML (AXML)を読み取るための基礎クラス
 */
public class AXMLParser {
    private byte[] data;
    private int offset = 0;
    private List<String> stringPool = new ArrayList<>();

    public AXMLParser(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        data = fis.readAllBytes();
        fis.close();
    }

    private int readInt() {
        int val = ByteBuffer.wrap(data, offset, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        offset += 4;
        return val;
    }

    private short readShort() {
        short val = ByteBuffer.wrap(data, offset, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
        offset += 2;
        return val;
    }

    private byte readByte() {
        byte val = data[offset];
        offset += 1;
        return val;
    }

    private String readString(int strOffset) {
        // stringPool内の文字列取得（UTF-16LE）
        // ここでは簡易にstringPoolから取得する想定
        if (strOffset < 0 || strOffset >= stringPool.size()) return null;
        return stringPool.get(strOffset);
    }

    public void parse() throws IOException {
        // 1. ヘッダー読み込み（0x00080003）
        int axmlMagic = readInt();
        if (axmlMagic != 0x00080003) {
            throw new IOException("Not a valid AXML file");
        }

        // 2. ファイルサイズ
        int fileSize = readInt();
        System.out.println("[*] AXML file size: " + fileSize);

        // 3. 文字列プールチャンクの解析（省略せずにしっかり読む必要あり）
        parseStringPool();

        // 4. 他チャンクを順に解析（ここは省略）
        System.out.println("[*] String pool size: " + stringPool.size());

        // TODO: XMLタグ解析を追加すること
    }

    private void parseStringPool() throws IOException {
        // 文字列プールの先頭は現在のoffset位置にある想定
        // 詳細仕様は AndroidのAXMLフォーマットを参照する必要あり

        // chunk type (4 bytes)
        int chunkType = readInt();
        if (chunkType != 0x001C0001) { // STRING_POOL_TYPE (0x001C0001)
            throw new IOException("String pool chunk not found");
        }

        int chunkSize = readInt();
        int stringCount = readInt();
        int styleCount = readInt();
        int flags = readInt();
        int stringsStart = readInt();
        int stylesStart = readInt();

        // stringOffsets 配列の読み込み
        int[] stringOffsets = new int[stringCount];
        for (int i = 0; i < stringCount; i++) {
            stringOffsets[i] = readInt();
        }

        // 保存用の文字列データ開始オフセット
        int stringsBase = offset;

        // 文字列読み込み
        for (int i = 0; i < stringCount; i++) {
            int strOffset = stringsBase + stringOffsets[i];
            String str = readUtf16LeString(strOffset);
            stringPool.add(str);
        }

        // 文字列プール終わりのoffsetを合わせる
        offset = stringsBase + (chunkSize - (4*8 + stringCount*4)); // ざっくり合わせる
    }

    private String readUtf16LeString(int strOffset) {
        // バイト配列のstrOffsetからUTF-16LE文字列を読み取る
        // 先頭2バイトは文字長(文字数), その後UTF-16LE文字列
        int pos = strOffset;
        int len = (data[pos] & 0xFF) | ((data[pos + 1] & 0xFF) << 8);
        pos += 2;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = (char)((data[pos] & 0xFF) | ((data[pos + 1] & 0xFF) << 8));
            sb.append(c);
            pos += 2;
        }
        return sb.toString();
    }
}
