$package("com.kidscademy.page");

/**
 * Atlas release manager.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.ReleasePage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.showObject();
        this._sidebar.on("edit-release", this._onEditRelease, this);
        this._sidebar.on("clear-release", this._onClearRelease, this);
        this._sidebar.on("remove-release", this._onRemoveRelease, this);
        this._sidebar.on("android-app", this._onAndroidApp, this);

        this._contextMenu = this.getByCssClass("context-menu");
        this._contextMenu.on("edit-object", this._onEditObject, this);
        this._contextMenu.on("preview-object", this._onPreviewObject, this);
        this._contextMenu.on("translate-object", this._onTranslateObject, this);
        this._contextMenu.on("open-collection", this._onOpenCollection, this);
        this._contextMenu.on("remove-object", this._onRemoveObject, this);

        this._release = null;
        this._objectsList = this.getByName("objects-list");
        this._objectsList.setContextMenu(this._contextMenu);

        this._languageSelect = this._sidebar.getByCssClass("languages");

        const release = WinMain.url.parameters.release;
        const releaseId = Number(release);
        if (isNaN(releaseId)) {
            ReleaseService.getReleaseByName(release, this._onReleaseLoaded, this);
        }
        else {
            ReleaseService.getRelease(releaseId, this._onReleaseLoaded, this);
        }
    }

    _onReleaseLoaded(release) {
        this._release = release;
        this._sidebar.setObject(release);

        this._languageSelect.setOptions(release.languages);
        this._languageSelect.setValue(this.getContextAttr("preview-language"));

        this._objectsList.resetTimestamp();
        ReleaseService.getReleaseItems(release.id, items => this._objectsList.setObject(items));
    }

    _onUnload() {
        this.setContextAttr("preview-language", this._languageSelect.getValue());
    }

    _onEditRelease() {
        WinMain.assign("@link/release-form", { release: this._release.id });
    }

    _onClearRelease() {
        js.ua.System.confirm("@string/confirm-release-clear", ok => {
            if (ok) {
                ReleaseService.clearRelease(this._release.id, () => {
                    this._objectsList.resetTimestamp();
                    ReleaseService.getReleaseItems(this._release.id, releases => {
                        this._objectsList.setObject(releases)
                    });
                });
            }
        });
    }

    _onRemoveRelease() {
        js.ua.System.confirm("@string/confirm-release-remove", ok => {
            if (ok) {
                ReleaseService.removeRelease(this._release.id, () => WinMain.assign("@link/releases"));
            }
        });
    }

    _onAndroidApp() {
        WinMain.assign("@link/android-app", { release: this._release.name });
    }

    _onEditObject(objectView) {
        const object = objectView.getUserData();
        WinMain.assign("@link/object-form", { object: object.id });
    }

    _onPreviewObject(objectView) {
        const object = objectView.getUserData();
        WinMain.assign("@link/reader", { object: object.id, language: this._languageSelect.getValue() });
    }

    _onTranslateObject(objectView) {
        const object = objectView.getUserData();
        WinMain.assign("@link/translate-object", { object: object.id, language: this._languageSelect.getValue() });
    }

    _onOpenCollection(objectView) {
        const object = objectView.getUserData();
        WinMain.assign("@link/collection", { collection: object.collection.id });
    }

    _onRemoveObject(objectView) {
        js.ua.System.confirm("@string/confirm-remove-from-release", ok => {
            if (ok) {
                const object = objectView.getUserData();
                ReleaseService.removeReleaseChild(this._release.id, object.id, () => objectView.remove());
            }
        });
    }

    toString() {
        return "com.kidscademy.page.ReleasePage";
    }
};

WinMain.createPage(com.kidscademy.page.ReleasePage);
