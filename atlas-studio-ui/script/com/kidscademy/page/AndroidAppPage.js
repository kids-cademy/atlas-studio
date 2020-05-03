$package("com.kidscademy.page");

com.kidscademy.page.AndroidAppPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._app = null;

        this._sidebar.setTitle("Android App");
        this._sidebar.on("open-release", this._onOpenRelease, this);
        this._sidebar.on("android-settings", this._onAndroidSettings, this);
        this._sidebar.on("android-listing", this._onAndroidListing, this);
        this._sidebar.on("clean-project", this._onCleanProject, this);
        this._sidebar.on("build-apk", this._onBuildAPK, this);
        this._sidebar.on("build-bundle", this._onBuildBundle, this);

        this._releaseView = this.getByCssClass("release");
        this._objectsList = this.getByCssClass("objects-list");

        const release = WinMain.url.parameters.release;
        if (release) {
            ReleaseService.getAndroidAppForRelease(release, app => {
                if (app == null) {
                    // if android application is not created yet delegate android settings form
                    WinMain.assign("@link/android-settings", { release: release });
                    return;
                }
                this._onAppLoaded(app);
            });
        }
        else {
            const appId = Number(WinMain.url.parameters.app);
            ReleaseService.getAndroidApp(appId, this._onAppLoaded, this);
        }
    }

    _onAppLoaded(app) {
        this._app = app;
        this._releaseView.setObject(app);
        this._objectsList.resetTimestamp();
        ReleaseService.getReleaseItems(app.release.id, items => this._objectsList.setObject(items));
    }

    _onOpenRelease() {
        WinMain.assign("@link/release", { release: this._app.name });
    }

    _onAndroidSettings() {
        WinMain.assign("@link/android-settings", { app: this._app.id });
    }

    _onAndroidListing() {
        WinMain.assign("@link/android-listing", { app: this._app.id });
    }

    _onCleanProject() {
        ReleaseService.cleanAndroidProject(this._app.id, () => js.ua.System.alert("@string/alert-processing-done"));
    }

    _onBuildAPK() {
        ReleaseService.buildAndroidApp(this._app.id, () => js.ua.System.alert("@string/alert-processing-done"));
    }

    _onBuildBundle() {

    }

    toString() {
        return "com.kidscademy.page.AndroidAppPage";
    }
};

WinMain.createPage(com.kidscademy.page.AndroidAppPage);
