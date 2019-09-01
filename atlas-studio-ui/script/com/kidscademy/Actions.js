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
		 * Name of the action executed previous to current one, possible null if no operation was invoked yet. This value is updated
		 * after every action handler invocation.
		 * This value is returned by {@link this.getPreviousAction()} and can be used by container when current action handler depends
		 * on previous action.
		 * @type {String} 
		 */
		this._previousAction = null;
	}

	bind(container) {
		function handlerName(name) {
			return "_on" + name.replace(/(?:^|\-)(\w)/g, (match, capture) => capture.toUpperCase());
		}

		this.getChildren().forEach(child => {
			if (!this._isAction(child)) {
				return;
			}
			const name = child.getName();
			$assert(name != null, "com.kidscademy.Actions#bind", "Action element without name.");

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
		});
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
		return this;
	}

	showOnly(...names) {
		this.getChildren().forEach(action => {
			action.show(names.includes(action.getName()));
		});
		return this;
	}

	showAll() {
		this.getChildren().forEach(child => {
			child.show();
		});
		return this;
	}

	hide(...names) {
		names.forEach(name => this.getByName(name).hide());
		return this;
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

	_isAction(child) {
		return !child.hasCssClass("control") && !child.hasCssClass("separator");
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
