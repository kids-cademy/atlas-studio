$package("com.kidscademy");

com.kidscademy.ContextMenu = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._titleView = this.getByCssClass("title");

		/**
		 * Optional object view on context menu header. It can be null if context menu belongs to a group view
		 * not oriented on icons, e.g. table view.
		 * @type {js.dom.Element}
		 */
		this._objectView = this.getByCssClass("object");

		/**
 		 * Dictionary for action handler. Key is the action name and value is mouse click event listener.
 		 * @type {Object}
 		 */
		this._actionHandlers = {};

		this._contextView = null;

		this.getByCssClass("close").on("click", this.hide, this);
	}

	on(actionName, listener, scope) {
		const action = this.getByCss("li[data-name='%s']", actionName);
		$assert(action != null, "com.kidscademy.ContextMenu#on", "No menu item for action |%s|.", actionName);

		if (action.hasCssClass("default-action")) {
			this._defaultAction = { name: actionName, listener: listener, scope: scope };
		}

		const _this = this;
		const actionHandler = function () {
			_this.hide();
			listener.call(scope || window, _this._contextView);
		};
		action.on("click", actionHandler);
	}

	getDefaultAction() {
		return this._defaultAction;
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

			if (child.hasCssClass("default-action")) {
				this._defaultAction = { name: name, listener: actionHandler };
			}

			child.on("click", actionHandler, container);
			this._actionHandlers[name] = actionHandler;
		});
		return this;
	}

	open(contextView) {
		this._contextView = contextView;
		if (this._contextView == null) {
			return;
		}
		const object = contextView.getUserData();
		this._titleView.setText(object.title).show();
		this._objectView.setObject(object).show();
		this.show();
	}

	toString() {
		return "com.kidscademy.ContextMenu";
	}
};
