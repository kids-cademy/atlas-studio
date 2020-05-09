$package("com.kidscademy");

com.kidscademy.ListView = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        /**
         * Optional reference to external context menu, related to this list items. It is diplayed on right click or long tap on a list item. 
         * This reference is null if not set in which case right click or long tap event is delegated to default action.
         * @type {com.kidscademy.ContextMenu}
         */
        this._contextMenu = null;

        this._timestamp = 0;
        this._loadingInfoView = this.getByCssClass("loading-info");

        this._listControl = this.getByCssClass("list-control");
        this._listControl.setLayout("icons");
        this._listControl.on("click", this._onListClick, this);

        this._actions = this.getByClass(com.kidscademy.Actions);
        if (this._actions != null) {
            this._actions.bind(this);
        }

        this._slaveLists = [];

        this.on("contextmenu", this._onContextMenu, this);
    }

    bindSlaveList(slaveList) {
        this._slaveLists.push(slaveList);
    }

    setContextMenu(contextMenu) {
        this._contextMenu = contextMenu;
    }

    resetTimestamp() {
        this._timestamp = Date.now();
    }

    setObject(objects) {
        if (this._timestamp !== 0) {
            if (this._loadingInfoView != null) {
                this._loadingInfoView.setObject({
                    objectsCount: objects.length,
                    ellapsedTime: Date.now() - this._timestamp
                }).show();
            }
            this._timestamp = 0;
        }
        else {
            if (this._loadingInfoView != null) {
                this._loadingInfoView.hide();
            }
        }

        this._listControl.setObject(objects).show();
        return this;
    }

    addObject(object) {
        this._listControl.addObject(object).show();
        return this;
    }

    isEmpty() {
        return this._listControl.getChildrenCount() === 0;
    }

    // --------------------------------------------------------------------------------------------
    // FILTER ACTION HANDLERS

    _onLoadItems() {
    }

    _onResetFilter() {
    }

    _onIconsView() {
        this._setLayout("icons");
    }

    _onTilesView() {
        this._setLayout("tiles");
    }

    _onDetailsView() {
        this._setLayout("details");
    }

    _onCardsView() {
        this._setLayout("cards");
    }

    _setLayout(layout) {
        this._listControl.setLayout(layout);
        this._slaveLists.forEach(slaveList => slaveList._listControl.setLayout(layout));
    }

    // --------------------------------------------------------------------------------------------

    _onListClick(ev) {
        const item = ev.target.getParentByTag("li");
        if (item != null) {
            if (this._contextMenu != null) {
                const action = this._contextMenu.getDefaultAction(ev);
                if (action != null) {
                    action.listener.call(action.scope, item);
                }
            }
        }
    }

    _onContextMenu(ev) {
        if (this._contextMenu != null) {
            ev.halt();
            this._contextMenu.open(ev.target.getParentByTag("li"));
        }
    }

    toString() {
        return "com.kidscademy.ListView";
    }
};
