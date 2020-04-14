$package("com.kidscademy.page");

com.kidscademy.page.CollectionsPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("@string/collections");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("links-meta", this._onLinksMeta, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);
        this._sidebar.on("create-collection", this._onCreateCollection, this);

        this._contextMenu = this.getByCssClass("context-menu");
        this._contextMenu.on("edit-collection", this._onEditCollection, this);
        this._contextMenu.on("remove-collection", this._onRemoveCollection, this);
        this._contextMenu.on("manage-objects", this._onManageObjects, this);

        this._listView = this.getByCssClass("list-view");
        this._listView.setContextMenu(this._contextMenu);

        this._listView.resetTimestamp();
        AtlasService.getCollections(collections => this._listView.setObject(collections));
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

WinMain.createPage(com.kidscademy.page.CollectionsPage);
