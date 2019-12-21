$package("com.kidscademy.form");

/**
 * Base control for atlas objects form.
 */
com.kidscademy.form.FormControl = class extends js.dom.Control {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

		/**
		 * Parent form page.
		 * @type {com.kidscademy.form.FormPage}
		 */
        this._formPage = null;
    }

    /**
     * Form control life cycle hook. Invoked after parent form created but before atlas object loaded.
     */
    onCreate(formPage) {
        this._formPage = formPage;

        this.findByCss("[data-persist]").forEach(element => {
            const value = this._formPage.getPageAttr(element.getAttr("data-persist"));
            if (value) {
                element.setValue(value);
            }
        });
    }

    onDestroy() {
        this.findByCss("[data-persist]").forEach(element => {
            this._formPage.setPageAttr(element.getAttr("data-persist"), element.getValue());
        });
    }

    /**
     * Form control life cycle hook. Invoked after atlas object was loaded and this control value updated.
     */
    onStart() {

    }

    _fireEvent(eventName) {
        const event = new Event(eventName, { bubbles: true });
        this._node.dispatchEvent(event);
    }

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
    toString() {
        return "com.kidscademy.form.FormControl";
    }
};
