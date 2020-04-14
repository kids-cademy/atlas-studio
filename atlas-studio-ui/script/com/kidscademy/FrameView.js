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
        if (this._view != null) {
            this._view.hide();
        }
        this._view = this.getByCss(`:scope > .%s`, viewName);
        $assert(this._view != null, "com.kidscademy.FrameView#select", "No child view with name |%s|.", viewName);
        return this._view.show();
    }

    toString() {
        return "com.kidscademy.FrameView";
    }
};
