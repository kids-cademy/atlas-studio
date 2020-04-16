$package("com.kidscademy");

com.kidscademy.FrameView = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        /**
         * Currently selected child view. In a frame view only one child view can be selected at a time.
         * Use {@link #select(String)} to select current child view.
         * 
         * @type js.dom.Element
         */
        this._view = this.getFirstChild();
    }

    /**
     * Select current view identified by its name. By convention child view name is stored on layout using 'data-name' attribute.
     * 
     * @param {String} viewName the name of child view to select.
     */
    select(viewName) {
        $assert(viewName != null, "com.kidscademy.FrameView#select", "Null view name.");
        this._view.hide();
        this._view = this.getByCss(`:scope > .%s`, viewName);
        $assert(this._view != null, "com.kidscademy.FrameView#select", "No child view with class |%s|.", viewName);
        return this._view.show();
    }

    setObject(items) {
        $assert(this._view != null, "com.kidscademy.FrameView#setObject", "Invalid state. Null selected view.");
        this._view.setObject(items);
    }

    scrollIntoView(childId) {
        const childView = this._view.getByCss("li[id='%s']", childId);
        if (childView != null) {
            childView.scrollIntoView();
        }
    }

    toString() {
        return "com.kidscademy.FrameView";
    }
};
