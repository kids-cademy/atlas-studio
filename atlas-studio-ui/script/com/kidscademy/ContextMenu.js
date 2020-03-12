$package("com.kidscademy");

com.kidscademy.ContextMenu = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._objectView = this.getByCssClass("object");

		/**
 		 * Dictionary for action handler. Key is the action name and value is mouse click event listener.
 		 * @type {Object}
 		 */
		this._actionHandlers = {};

		this._contextView = null;

		const closeAction = this.getByCssClass("close");
		closeAction.on("click", this._onClose, this);
	}

	/**
	 * Bind contextual menu to object selected on user interface.
	 * 
	 * @param {Object} container parent container. 
	 */
	bind(container) {
		function handlerName(name) {
			return "_on" + name.replace(/(?:^|\-)(\w)/g, (match, capture) => capture.toUpperCase());
		}
		function isAction(child) {
			return !child.hasCssClass("control") && !child.hasCssClass("separator");
		}

		this._container = container;

		this.getByCssClass("menu").getChildren().forEach(child => {
			if (!isAction(child)) {
				return;
			}
			const name = child.getName();
			if (name == null) {
				return;
			}

			const containerHandler = container[handlerName(name)];
			if (typeof containerHandler !== "function") {
				throw `Missing handler for action ${name}.`;
			}

			// create a closure to keep action handler state that include current action name
			// container handler can obtain current action via this._actions.getCurrentAction()
			const menu = this;
			const actionHandler = function () {
				menu.hide();
				containerHandler.call(container, menu._contextView);
			}.bind(container);

			child.on("click", actionHandler, container);
			this._actionHandlers[name] = actionHandler;
		});
		return this;
	}

	open(contextView) {
		this._contextView = contextView;
		if (this._contextView != null) {
			this.removeCssClass("no-object");
			this._objectView.setObject(contextView.getUserData()).show();
		}
		else {
			this.addCssClass("no-object");
			this._objectView.hide();
		}
		this.show();
	}

	_onClose() {
		this.hide();
	}

	toString() {
		return "com.kidscademy.ContextMenu";
	}
};
