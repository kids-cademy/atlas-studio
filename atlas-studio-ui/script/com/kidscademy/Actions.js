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

		this._disabled = [];

		/**
		 * Name of the action executed previous to current one, possible null if no operation was invoked yet. This value is updated
		 * after every action handler invocation.
		 * This value is returned by {@link this.getPreviousAction()} and can be used by container when current action handler depends
		 * on previous action.
		 * @type {String} 
		 */
		this._previousAction = null;

		this._args = new com.kidscademy.Actions.Args(this.getParent().getByCss(".actions.args"));
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
				return;
			}

			// create a closure to keep action handler state that include current action name and optional arguments form
			// container handler can obtain current action via this._actions.getCurrentAction()
			const actions = this;
			const actionHandler = function () {
				const argsForm = actions._args.open(name);
				containerHandler.call(container, argsForm != null ? argsForm : arguments[0]);
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

	disable(...names) {
		names.forEach(name => {
			const action = this.getByName(name);
			$assert(action != null, "com.kidscademy.Actions#show", "Action |%s| not found.", name);
			action.hide();
			if (!this._disabled.includes(name)) {
				this._disabled.push(name);
			}
		})
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

		names.forEach(name => {
			if (this._disabled.includes(name)) {
				return;
			}
			const action = this.getByName(name);
			$assert(action != null, "com.kidscademy.Actions#show", "Action |%s| not found.", name);
			action.show(show);
		});
		return this._update();
	}

	showOnly(...names) {
		this.getChildren().forEach(action => {
			if (this._disabled.includes(action.getName())) {
				return;
			}
			action.show(names.includes(action.getName()));
		});
		return this._update();
	}

	showAll() {
		this.getChildren().forEach(child => {
			if (this._disabled.includes(child.getName())) {
				return;
			}
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

	toString() {
		return "com.kidscademy.Actions";
	}
};

com.kidscademy.Actions.Args = class {
	constructor(container) {
		this._container = container;
		if (this._container != null) {
			this._container.getChildren().forEach(child => child.hide());
		}
		/**
		 * Currently selected arguments form data, possible null if named action does not have arguments.
		 * @type {com.kidscademy.FormData}
		 */
		this._argsForm = null;
	}

	/**
	 * Open data form arguments for named action. Returns null if named action does not have arguments.
	 * 
	 * @param {String} actionName action name.
	 * @return {com.kidscademy.FormData} action arguments form or null. 
	 */
	open(actionName) {
		// is legal for action bar to not have arguments at all in which case arguments container is null
		if (this._container == null) {
			return null;
		}

		// is legal to have action without arguments
		if (this._argsForm != null) {
			this._argsForm.hide();
		}

		// by convention arguments form data has a CSS class with action name
		this._argsForm = this._container.getByCss(":scope > .%s", actionName);
		if (this._argsForm != null) {
			this._argsForm.reset().show();
		}
		return this._argsForm;
	}

	toString() {
		return "com.kidscademy.Actions.Args";
	}
};
