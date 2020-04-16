$package("com.kidscademy.collection");

com.kidscademy.collection.InsightListView = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._page = WinMain.page;

        this.on("click", this._onClick, this);
        this.on("contextmenu", this._onContextMenu, this);
    }

    _onClick(ev) {
        if (this._page != null) {
            $assert(typeof this._page._onItemClick === "function", "com.kidscademy.collection.InsightListView#_onClick", "Missing item click handler from |%s|.", this._page.toString());
            this._page._onItemClick(ev);
        }
    }

    _onContextMenu(ev) {
        if (this._page != null) {
            $assert(typeof this._page._onContextMenu === "function", "com.kidscademy.collection.InsightListView#_onContextMenu", "Missing context menu handler from |%s|.", this._page.toString());
            this._page._onContextMenu(ev);
        }
    }

    toString() {
        return "com.kidscademy.collection.InsightListView";
    }
};
