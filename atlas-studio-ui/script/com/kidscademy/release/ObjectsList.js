$package("com.kidscademy.release");

com.kidscademy.release.ObjectsList = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._listView = this.getByCssClass("list-view");
        this._listView.on("click", this._onListClick, this);

        this._listType = new com.kidscademy.CssFlags(this._listView, "icons", "cards");
        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
    }

    onCreate(page) {
        this._page = page;
        //this._releaseForm = this._page.getByClass(com.kidscademy.release.ReleaseForm);

        this._listType.set(page.getPageAttr("release-objects-list-type"));
        this._actions.fire("load-items");
    }

    onDestroy(page) {
        page.setPageAttr("release-objects-list-type", this._listType.get());
    }

    open(release) {
        ReleaseService.getReleaseItems(release.id, objects => this._listView.setObject(objects).show());
    }

    // --------------------------------------------------------------------------------------------
    // FILTER ACTION HANDLERS

    _onLoadItems() {
    }

    _onResetFilter() {

    }

    _onIconsView() {
        this._listType.set("icons");
    }

    _onCardsView() {
        this._listType.set("cards");
    }

    // --------------------------------------------------------------------------------------------

    toString() {
        return "com.kidscademy.release.ObjectsList";
    }
};
