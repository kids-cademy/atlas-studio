$package("com.kidscademy");

/**
 * Actions handler.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.Actions = class extends js.dom.Element {
	/**
	 * Construct an instance of Actions class.
	 * 
	 * @param js.dom.Document ownerDoc element owner document,
	 * @param Node node native {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Parent container initialized by {@link #bind()} method.
		 * @type {js.dom.Element}
		 */
		this._container = null;

		/**
		 * Dictionary for action handler. Key is the action name and value is mouse click event listener.
		 * @type {Object}
		 */
		this._actionHandlers = {};

		/**
		 * Dictionary for action key handlers. Map key is the key code from key event and value is the related 
		 * mouse click event listener. For a list of supported key codes see {@link js.event.Key}.
		 * @type {Object}
		 */
		this._keyHanders = {};

		/**
		 * Name of the action executed previous to current one, possible null if no operation was invoked yet. This value is updated
		 * after every action handler invocation.
		 * This value is returned by {@link this.getPreviousAction()} and can be used by container when current action handler depends
		 * on previous action.
		 * @type {String} 
		 */
		this._previousAction = null;
	}

	/**
	 * Bind this actions controller to parent container and initialize actions controller internall state.
	 * @param {js.dom.Element} container parent container.
	 */
	bind(container) {
		function handlerName(name) {
			return "_on" + name.replace(/(?:^|\-)(\w)/g, (match, capture) => capture.toUpperCase());
		}

		this._container = container;
		var keyHandlers = false;

		this.getChildren().forEach(child => {
			if (!this._isAction(child)) {
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
			const actions = this;
			const actionHandler = function () {
				containerHandler.apply(container, arguments);
				actions._previousAction = name;
			}.bind(container);

			child.on("click", actionHandler, container);
			this._actionHandlers[name] = actionHandler;

			var dataKey = child.getAttr("data-key");
			if (dataKey != null) {
				dataKey.split(',').forEach(keyName => {
					keyName = keyName.trim();

					var ctrlKey = false;
					if (keyName.startsWith("CTRL+")) {
						ctrlKey = true;
						keyName = keyName.substring(5);
					}

					const keyCode = js.event.Key[keyName];
					if (typeof keyCode === "undefined") {
						throw `Invalid key name ${keyName} assigned to action ${name}`;
					}
					this._keyHanders[keyCode] = {
						ctrlKey: ctrlKey,
						method: actionHandler
					};
				});
				keyHandlers = true;
			}
		});

		if (keyHandlers) {
			this._container.on("keydown", this._onKey, this);
		}
		return this;
	}

	show(...args) {
		if (args.length === 0) {
			return;
		}

		var show, names;
		if (js.lang.Types.isBoolean(args[0])) {
			show = args[0];
			names = args.slice(1);
		}
		else {
			show = true;
			names = args;
		}

		names.forEach(name => this.getByName(name).show(show));
		return this._update();
	}

	showOnly(...names) {
		this.getChildren().forEach(action => {
			action.show(names.includes(action.getName()));
		});
		return this._update();
	}

	showAll() {
		this.getChildren().forEach(child => {
			child.show();
		});
		return this._update();
	}

	hide(...names) {
		names.forEach(name => this.getByName(name).hide());
		return this._update();
	}

	hideAll() {
		this.getChildren().forEach(child => {
			child.hide();
		});
		return this._update();
	}

	getControl(name) {
		const control = this.getByName(name);
		$assert(control != null, "com.kidscademy.Actions#getValue", "Missing control |%s| from actions bar.", name);
		$assert(control.hasCssClass("control"), "com.kidscademy.Actions#getValue", "Actions bar child |%s| is not a control.");
		return control;
	}

	getPreviousAction() {
		return this._previousAction;
	}

	fire(name) {
		this._actionHandlers[name]();
	}

	_onKey(ev) {
		const handler = this._keyHanders[ev.key];
		if (handler) {
			if (handler.ctrlKey && !ev.ctrlKey) {
				return;
			}
			handler.method.call(this._container);
			ev.halt();
		}
	}

	_isAction(child) {
		return !child.hasCssClass("control") && !child.hasCssClass("separator");
	}

	_update() {
		// first visible child
		const child = this.getByCss(":scope > :not(.hidden)");
		if (child != null && child.hasCssClass("separator")) {
			child.hide();
		}
		return this;
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.Actions";
	}
};
