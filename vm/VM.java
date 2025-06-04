package vm;

import vm.dex.DexLoader;

public class VM {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java vm.VM <path_to_classes.dex>");
            return;
        }

        String dexPath = args[0];
        System.out.println("[*] Loading DEX: " + dexPath);

        try {
            DexLoader dexLoader = new DexLoader(dexPath);
            dexLoader.load();
            System.out.println("[✔] DEX loaded successfully.");
            // ここで擬似VMとしての処理を呼び出し可能（例：クラスの一覧表示など）
            dexLoader.printClassNames();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
