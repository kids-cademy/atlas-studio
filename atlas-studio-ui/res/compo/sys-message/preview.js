WinMain.on("load", function() {
	const message = WinMain.doc.getById("sys-message-id");
	message.success("@string/sample-text");

	const actions = WinMain.doc.getById("actions-bar");
	const handlers = new Handlers(actions, message);
});

class Handlers {
	constructor(actions, message) {
		actions.on(this, {
			"&success" : this.onSuccess,
			"&info" : this.onInfo,
			"&warning" : this.onWarning,
			"&danger" : this.onDanger
		});
		this._message = message;
	}

	onSuccess() {
		this._message.success("@string/sample-text");
	}

	onInfo() {
		this._message.info("@string/sample-text");
	}

	onWarning() {
		this._message.warning("@string/sample-text");
	}

	onDanger() {
		this._message.danger("@string/sample-text");
	}
}