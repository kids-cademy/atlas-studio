$package("com.kidscademy");

/**
 * A collapsible section has a <code>body</code> that can be expanded or collapsed. In order to toggle the
 * <code>body</code> collapsed state, a collapsible section has also a <code>head</code> that is always visible.
 * <p>
 * Collapsible section allows for dynamic content. For this reason animation adapts itself to the <code>body</code>
 * height before start.
 * <p>
 * Implementation note: animation is implemented with CSS3 transition on max-height attribute. Unfortunately transition
 * needs explicit dimension, that is, auto is not supported. For this reason, at animation end max-height is set to
 * auto, see {@link #_onAnimationComplete} and restored to new body height before starting a another animation
 * transition.
 * 
 * @author Iulian Rotaru
 * @since 1.0
 */
com.kidscademy.CollapsibleSection = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this.addCssClass("collapsible");

		this._head = this.getByCssClass("head");
		this._body = this.getByCssClass("body");
		this._head.on("click", this._onHeadClick, this);
		this._body.on("transitionend", this._onAnimationComplete, this);

		this._bodyHeight = this._body.style.getHeight();
		this._body.style.set("max-height", "0");
		this._collapsed = true;
		this.addCssClass("collapsed");
	}

	collapse() {
		if (!this._collapsed) {
			this._onHeadClick(null);
		}
	}

	/**
	 * Toggle collapsed state and start transition animation.
	 * 
	 * @param {js.event.Event} ev mouse click event.
	 */
	_onHeadClick(ev) {
		if (!this._collapsed) {
			this._body.style.set("transition", "none");
			this._bodyHeight = this._body.style.getHeight();
			this._body.style.set("max-height", this._bodyHeight + "px");
			this._body.style.set("transition", "");
		}

		// need to delay a little animation transition start in order to ensure browser had restored it
		// without timeout expand animation is not performed

		js.util.Timeout(10, () => {
			this._collapsed = !this._collapsed;
			this.addCssClass("collapsed", this._collapsed);
			this._body.style.set("max-height", this._collapsed ? "0" : this._bodyHeight + "px");
		}, this);
	}

	/**
	 * On animation complete, reset maximum height in order to allow for dynamic content. See class description for
	 * details.
	 * 
	 * @param {js.event.Event} ev transition end event.
	 */
	_onAnimationComplete(ev) {
		if (!this._collapsed) {
			this._body.style.set("max-height", "none");
		}
	}

	toString() {
		return "com.kidscademy.CollapsibleSection";
	}
};

$preload(com.kidscademy.CollapsibleSection);
