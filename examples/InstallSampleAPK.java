import installer.aosp_stub.PackageInstaller;
import java.io.File;

public class InstallSampleAPK {

    public static void main(String[] args) {
        File apk = new File("examples/sample.apk");

        PackageInstaller installer = new PackageInstaller();
        installer.installPackage(apk, new PackageInstaller.InstallObserver() {
            @Override
            public void onInstallSuccess(String packageName) {
                System.out.println("[SUCCESS] Installed: " + packageName);
            }

            @Override
            public void onInstallFailure(String packageName, String reason) {
                System.out.println("[FAILURE] " + packageName + ": " + reason);
            }
        });
    }
}
