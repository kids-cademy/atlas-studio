$package("com.kidscademy");

com.kidscademy.Sidebar = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this.CONTEXT_ATTR = "side-bar-collapsed";

        this._titleView = this.getByCssClass("title");
        this._objectView = this.getByCssClass("object-view");

        const viewTabs = this.getByCssClass("view-tabs");
        if (viewTabs != null) {
            viewTabs.on("click", this._onViewTabsClick, this);
        }

        this.addCssClass("collapsed", WinMain.page.getContextAttr(this.CONTEXT_ATTR));
        this.getByCssClass("collapse").on("click", this._onCollapse, this);

        this._events = this.getCustomEvents();
        this._events.register("view-selected");
    }

    showObject() {
        this._objectView.show();
    }

    setObject(object) {
        this._titleView.setText(object.title);
        this._objectView.setObject(object);
        return this;
    }

    setTitle(title) {
        this._titleView.setText(title);
    }

    /**
     * Register quick action or custom event listener. This method adds click event listener to quick action identified by name parameter.
     * 
     * @param {String} name quick action or event name,
     * @param {Function} listener event listener,
     * @param {Object} scope optional function runtime scope, used if listener is not lambda.
     */
    on(name, listener, scope) {
        // if name parameter is a custom event process it first
        if (this._events.hasType(name)) {
            this._events.addListener(name, listener, scope);
            return;
        }
        const action = this.getByCss("li[data-name='%s']", name);
        $assert(action != null, "com.kidscademy.Sidebar#on", "Action |%s| not declared on sidebar menu.", name);
        action.on("click", listener, scope);
    }

    _onViewTabsClick(ev) {
        const item = ev.target.getParentByTag("li");
        if (item != null) {
            this._events.fire("view-selected", item.getName());
        }
    }

    _onCollapse() {
        this.toggleCssClass("collapsed");
        WinMain.page.setContextAttr(this.CONTEXT_ATTR, this.hasCssClass("collapsed"));
    }

    toString() {
        return "com.kidscademy.Sidebar";
    }
};
