$package("com.kidscademy");

/**
 * Context menu handlers for atlas objects. This mixin is used by objects list from collection page and
 * by most recent used objects from workspace page.
 */
com.kidscademy.ObjectsContextMenu = {
    _onEditObject(objectView) {
        const object = objectView.getUserData();
        WinMain.assign("@link/object-form", { object: object.id });
    },

    _onPreviewObject(objectView) {
        const object = objectView.getUserData();
        WinMain.assign("@link/reader", { object: object.id });
    },

    _onRemoveObject(objectView) {
        js.ua.System.confirm("@string/confirm-object-remove", (ok) => {
            if (ok) {
                const object = objectView.getUserData();
                AtlasService.removeAtlasObject(object.id, () => objectView.remove());
            }
        });
    },

    _onMoveObject(objectView) {
        AtlasService.getCollections(collections => this._itemSelect.load(collections));
        this._itemSelect.open(collection => {
            const object = objectView.getUserData();
            AtlasService.moveAtlasObject(object, collection.id, () => objectView.remove());
        });
    },

    _onAddToRelease(objectView) {
        ReleaseService.getReleases(releases => this._itemSelect.load(releases));
        this._itemSelect.open(release => {
            const object = objectView.getUserData();
            ReleaseService.addReleaseChild(release.id, object.id);
        });
    }
};
