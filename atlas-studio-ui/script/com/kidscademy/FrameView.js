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
        this._selectedView = this.getFirstChild();
    }

    setObject(childViews) {
        $assert(this._selectedView, "com.kidscademy.FrameView#setObject", "Invalid state. No child view selected.");
        this._selectedView.setObject(childViews);
    }

    /**
     * Select current child view identified by CSS class.
     * 
     * @param {String} viewName the name of child view to select.
     * @returns {js.dom.Element} newly selected child view.
     * @assert view name argument is not undefined, null or empty.
     */
    select(viewName) {
        $assert(viewName, "com.kidscademy.FrameView#select", "Undefined, null or empty view name.");
        this.show();
        this._selectedView.hide();
        this._selectedView = this.getByCss(`:scope > .${viewName}`);
        $assert(this._selectedView != null, "com.kidscademy.FrameView#select", `No child view with class |${viewName}|.`);
        return this._selectedView.show();
    }

    isSelected(viewName) {
        $assert(this._selectedView, "com.kidscademy.FrameView#setObject", "Invalid state. No child view selected.");
        return this._selectedView.hasCssClass(viewName);
    }

    scrollIntoView() {
        $assert(this._selectedView, "com.kidscademy.FrameView#setObject", "Invalid state. No child view selected.");
        this._selectedView.scrollIntoView();
    }

    toString() {
        return "com.kidscademy.FrameView";
    }
};
