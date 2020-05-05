$package("com.kidscademy.page");

com.kidscademy.page.CollectionsPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("@string/collections");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("links-meta", this._onLinksMeta, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);
        this._sidebar.on("create-collection", this._onCreateCollection, this);

        this._collectionsMenu = this.getByCssClass("collections-menu");
        this._collectionsMenu.on("edit-collection", this._onEditCollection, this);
        this._collectionsMenu.on("remove-collection", this._onRemoveCollection, this);
        this._collectionsMenu.on("manage-objects", this._onManageObjects, this);

        this._collectionsView = this.getByCssClass("collections-view");
        this._collectionsView.setContextMenu(this._collectionsMenu);

        this._objectsMenu = this.getByCssClass("objects-menu");
		// objects context menu handler are implemented by com.kidscademy.ObjectsContextMenu mixin
        this._objectsMenu.on("edit-object", this._onEditObject, this);
        this._objectsMenu.on("preview-object", this._onPreviewObject, this);
        this._objectsMenu.on("remove-object", this._onRemoveObject, this);
        this._objectsMenu.on("move-object", this._onMoveObject, this);
        this._objectsMenu.on("add-to-release", this._onAddToRelease, this);

        this._objectsView = this.getByCssClass("objects-view");
        this._objectsView.setContextMenu(this._objectsMenu);
        this._collectionsView.bindSlaveList(this._objectsView);

        this._itemSelect = this.getByClass(com.kidscademy.ItemSelect);

        this._collectionsView.resetTimestamp();
        AtlasService.getCollections(collections => this._collectionsView.setObject(collections));
        AtlasService.getRecentUsedAtlasObjects(objects => this._objectsView.setObject(objects).show());
    }

    _onCollections() {
        WinMain.assign("@link/collections");
    }

    _onLinksMeta() {
        WinMain.assign("@link/links-meta");
    }

    _onFeaturesMeta() {
        WinMain.assign("@link/features-meta");
    }

    _onCreateCollection() {
        WinMain.assign("@link/collection-form");
    }

    _onEditCollection(collectionView) {
        const collection = collectionView.getUserData();
        WinMain.assign("@link/collection-form", { collection: collection.id });
    }

    _onRemoveCollection(collectionView) {
        js.ua.System.confirm("@string/confirm-collection-remove", ok => {
            if (ok) {
                const collection = collectionView.getUserData();
                AtlasService.removeAtlasCollection(collection.id, () => collectionView.remove());
            }
        });
    }

    _onManageObjects(collectionView) {
        const collection = collectionView.getUserData();
        WinMain.assign("@link/collection", { collection: collection.id });
    }

    toString() {
        return "com.kidscademy.page.CollectionsPage";
    }
};

Object.assign(com.kidscademy.page.CollectionsPage.prototype, com.kidscademy.ObjectsContextMenu);
WinMain.createPage(com.kidscademy.page.CollectionsPage);
