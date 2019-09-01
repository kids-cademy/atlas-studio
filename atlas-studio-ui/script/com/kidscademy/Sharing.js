$package("com.kidscademy");

/**
 * Sharing class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.Sharing = class extends js.dom.Element {
	/**
	 * Construct an instance of Sharing class.
	 * 
	 * @param js.dom.Document ownerDoc element owner document,
	 * @param Node node native {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		const image = "@image/facebook-sharing";

		/**
		 * Immutable site sharing Open Graph object.
		 * 
		 * @type Object
		 */
		this._siteSharingObject = {
			url: com.kidscademy.DataSource.getSiteURL(),
			image: "http://kids-cademy.com/media/asset_facebook-sharing.png",
			title: "@string/site-name",
			description: "@string/site-description"
		};

		/**
		 * Optional Open Graph object used to share current fable, default to null. If not null used this object to share a
		 * certain fable instead of entire site.
		 * 
		 * @type Object
		 */
		this._fableSharingObject = null;

		this.getByCss(".facebook").on("click", this._onFacebook, this);
		this.getByCss(".twitter").on("click", this._onTwitter, this);
		this.getByCss(".email").on("click", this._onEmail, this);

		this._events = this.getCustomEvents();
		this._events.register("sent");
	}

	setFable(fable) {
		this._fableSharingObject = {
			url: com.kidscademy.DataSource.getFablePageURL(fable.name),
			image: com.kidscademy.DataSource.getFablePictureURL(fable.name),
			title: fable.content[0].title,
			description: js.util.Strings.toPlainText(fable.content[0].text, 0, 120) + " ... \r\n\r\n" + fable.content[0].moral
		};
	}

	_onFacebook(ev) {
		this._events.fire("sent");

		const objectToLike = {
			object: this._getOpenGraphObject()
		};
		FB.ui({
			method: 'share_open_graph',
			action_type: 'og.shares',
			action_properties: JSON.stringify(objectToLike)
		});
	}

	_onTwitter(ev) {
		this._events.fire("sent");

		const isFable = this._fableSharingObject !== null;
		const openGraphObject = this._getOpenGraphObject();
		let text;
		if (isFable) {
			text = `Read "${openGraphObject.title}" on Kids Fables:`
		}
		else {
			text = `${openGraphObject.title}. ${openGraphObject.description}`;
		}

		// https://dev.twitter.com/web/tweet-button/web-intent
		WinMain.open("https://twitter.com/intent/tweet", {
			text: text,
			url: openGraphObject.url
		}, {
				top: 200,
				left: 200,
				width: 600,
				height: 400,
				menubar: false,
				toolbar: false
			});
	}

	_onEmail(ev) {
		this._events.fire("sent");

		const openGraphObject = this._getOpenGraphObject();
		WinMain.assign("mailto:", {
			// uses the same subject for both site and fable sharing; site sharing title is site name
			subject: this._siteSharingObject.title,
			body: `${openGraphObject.description}\r\n\r\n${openGraphObject.url}`
		});
	}

	_getOpenGraphObject() {
		const openGraphObject = this._fableSharingObject !== null ? this._fableSharingObject : this._siteSharingObject;
		// ensure fable sharing object is used only once
		this._fableSharingObject = null;
		return openGraphObject;
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.Sharing";
	}
};

$preload(com.kidscademy.Sharing);
