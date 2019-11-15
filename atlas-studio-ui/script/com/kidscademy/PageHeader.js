$package("com.kidscademy");

/**
 * PageHeader class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.PageHeader = class extends js.dom.Element {
	/**
	 * Construct an instance of PageHeader class.
	 * 
	 * @param js.dom.Document ownerDoc element owner document,
	 * @param Node node native {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._topMenu = this.getByCssClass("top-menu");

		this._topMenuButton = this.getByCssClass("top-menu-button");
		if(this._topMenuButton) {
			this._topMenuButton.on("click", this._onTopMenuButton, this);
		}
		
		const communityAction = this.getByCss(".community-action a");
		if (communityAction) {
			communityAction.on("click", this._onCommunityAction, this);
		}

		const logoutAction = this.getByCss(".logout-action");
		if (logoutAction) {
			logoutAction.on("click", this._onLogoutAction, this);
		}

		this._collapsed = true;
	}

	_onTopMenuButton(ev) {
		this._collapsed = !this._collapsed;
		if (!this._collapsed) {
			const topMenuHeight = this._topMenu.getFirstChild().style.getHeight();
			this._topMenu.style.set("height", topMenuHeight + "px");
		}
		else {
			this._topMenu.style.set("height", "0");
		}
	}

	_onCommunityAction(ev) {
		AdminService.isAuthenticated(authenticated => WinMain.assign(authenticated ? "collection.htm" : "community.htm"));
	}

	_onLogoutAction(ev) {
		AdminService.logout(() => WinMain.assign("home.htm"));
	}
	
	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.PageHeader";
	}
};

$preload(com.kidscademy.PageHeader);