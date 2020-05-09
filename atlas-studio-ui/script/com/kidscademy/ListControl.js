$package("com.kidscademy");

com.kidscademy.ListControl = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        /**
         * Supported layouts.
         * @type {Array<String>}
         */
        this._layouts = ["icons", "tiles", "details", "cards"];

        /**
         * Currently selected layout.
         * @type {String}
         */
        this._currentLayout = null;

        this._layouts.forEach(layout => this.removeCssClass(layout));
        this.setObject([]);
    }

    setLayout(layout) {
        if (layout == null) {
            layout = this._layouts[0];
        }
        if (!this._layouts.includes(layout)) {
            $debug("com.kidscademy.ListControl#setLayout", `Invalid layout |${layout}|.`);
            layout = this._layouts[0];
        }
        if (this._currentLayout != null) {
            this.removeCssClass(this._currentLayout);
        }
        this._currentLayout = layout;
        this.addCssClass(layout);
    }

    getLayout() {
        return this._currentLayout;
    }

    toString() {
        return "com.kidscademy.ListControl";
    }
};
