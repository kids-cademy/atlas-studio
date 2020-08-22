$package("com.kidscademy.page");

com.kidscademy.page.AndroidAppPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._app = null;
        this._timestamp = 0;

        this._sidebar.setTitle("Android App");
        this._sidebar.on("open-release", this._onOpenRelease, this);
        this._sidebar.on("release-settings", this._onReleaseSettings, this);
        this._sidebar.on("android-listing", this._onAndroidListing, this);
        this._sidebar.on("android-settings", this._onAndroidSettings, this);
        this._sidebar.on("clean-build", this._onCleanBuild, this);
        this._sidebar.on("build-apk", this._onBuildAPK, this);
        this._sidebar.on("build-signed-apk", this._onBuildSignedAPK, this);
        this._sidebar.on("build-bundle", this._onBuildBundle, this);
        this._sidebar.on("toggle-console", this._onToggleConsole, this);

        const contextMenu = this.getByCssClass("context-menu");
        contextMenu.on("edit-object", this._onEditObject, this);
        contextMenu.on("preview-object", this._onPreviewObject, this);
        contextMenu.on("open-collection", this._onOpenCollection, this);

        this._releaseView = this.getByCssClass("release");
        this._objectsList = this.getByCssClass("objects-list");
        this._objectsList.setContextMenu(contextMenu);

        this._languageSelect = this._sidebar.getByCssClass("languages");

        this._console = this.getByClass(com.kidscademy.Console);
        this._load();
    }

    _load() {
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
        this._languageSelect.setOptions(app.release.languages);
        this._languageSelect.setValue(this.getPageAttr("preview-language"));

        this._objectsList.resetTimestamp();
        ReleaseService.getReleaseItems(app.release.id, items => this._objectsList.setObject(items));
    }

    _onUnload() {
        this.setPageAttr("preview-language", this._languageSelect.getValue());
    }

    _onOpenRelease() {
        WinMain.assign("@link/release", { release: this._app.name });
    }

    _onReleaseSettings() {
        WinMain.assign("@link/release-form", { release: this._app.release.id });
    }

    _onAndroidListing() {
        WinMain.assign("@link/android-listing", { app: this._app.id });
    }
    _onAndroidSettings() {
        WinMain.assign("@link/android-settings", { app: this._app.id });
    }

    _onCleanBuild() {
        this._beforeProcessing();
        ReleaseService.cleanAndroidProject(this._app.id, this._afterProcessing, this);
    }

    _onBuildAPK() {
        this._beforeProcessing();
        ReleaseService.buildAndroidApp(this._app.id, this._afterProcessing, this);
    }

    _onBuildSignedAPK() {
        this._beforeProcessing();
        ReleaseService.buildSignedAndroidApp(this._app.id, this._afterProcessing, this);
    }

    _onBuildBundle() {
        this._beforeProcessing();
        ReleaseService.buildAndroidBundle(this._app.id, this._afterProcessing, this);
    }

    _beforeProcessing() {
        this._timestamp = Date.now();
        this._console.show();
    }

    _afterProcessing() {
        var done = "-- PROCESSING DONE";
        if (this._timestamp !== 0) {
            done += `: ${Date.now() - this._timestamp} msec.`;
            this._timestamp = 0;
        }

        this._console.println(done);
        this._load();
    }

    _onToggleConsole() {
        this._console.toggleCssClass("hidden");
    }

    _onEditObject(objectView) {
        const object = objectView.getUserData();
        WinMain.assign("@link/object-form", { object: object.id });
    }

    _onPreviewObject(objectView) {
        const object = objectView.getUserData();
        WinMain.assign("@link/reader", { object: object.id, language: this._languageSelect.getValue() });
    }

    _onOpenCollection(objectView) {
        const object = objectView.getUserData();
        WinMain.assign("@link/collection", { collection: object.collection.id });
    }

    toString() {
        return "com.kidscademy.page.AndroidAppPage";
    }
};

WinMain.createPage(com.kidscademy.page.AndroidAppPage);
