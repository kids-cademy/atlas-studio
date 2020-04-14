$package("com.kidscademy.page");

com.kidscademy.page.ReleasesPage = class extends com.kidscademy.Page {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._sidebar.on("create-release", this._onCreateRelease, this);

        this._contextMenu = this.getByCssClass("context-menu");
        this._contextMenu.on("edit-release", this._onEditRelease, this);
        this._contextMenu.on("remove-release", this._onRemoveRelease, this);
        this._contextMenu.on("manage-objects", this._onManageObjects, this);
        this._contextMenu.on("android-app", this._onAndroidApp, this);

        this._listView = this.getByCssClass("list-view");
        this._listView.setContextMenu(this._contextMenu);

        this._listView.resetTimestamp();
        ReleaseService.getReleases(releases => this._listView.setObject(releases));
    }

    _onCreateRelease() {
        WinMain.assign("@link/release-form");
    }

    _onEditRelease(releaseView) {
        const release = releaseView.getUserData();
        WinMain.assign("@link/release-form", { release: release.id });
    }

    _onRemoveRelease(releaseView) {
        js.ua.System.confirm("@string/confirm-release-remove", ok => {
            if (ok) {
                const release = releaseView.getUserData();
                ReleaseService.removeRelease(release.id, () => releaseView.remove());
            }
        });
    }

    _onManageObjects(releaseView) {
        const release = releaseView.getUserData();
        WinMain.assign("@link/release", { release: release.id });
    }

    _onAndroidApp(releaseView) {
        const release = releaseView.getUserData();
        WinMain.assign("@link/android-app", { release: release.name });
    }

    toString() {
        return "com.kidscademy.page.ReleasesPage";
    }
};

WinMain.createPage(com.kidscademy.page.ReleasesPage);
