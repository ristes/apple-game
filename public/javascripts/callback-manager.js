ClickCallbackManager = function() {
	this.callbacks = {};
	this.active = null;

	var self = this;

	this.registerCallback = function(name, callback) {
		if (!callback || !callback.onClick
				|| (typeof callback.onClick !== 'function')) {
			throw new Error("callback with function onClick must be providen!");
		}
		this.callbacks[name] = callback;

		if (this.visualizer && this.visualizer.registerAction) {
			this.visualizer.registerAction(name, self);
		}

	}

	this.activate = function(name,field) {
		if (!this.callbacks[name]) {
			throw new Error('Invalid callback name: ' + name);
		}
		this.active = this.callbacks[name];
		if (typeof this.active.onActivate === 'function') {
			this.active.onActivate(field);
		}
	}

	this.deactivate = function() {
		this.active = null;
	}

	this.onClick = function(e) {
		console.log(this.def)
		if (self.active) {
			self.active.onClick(e, this);
		}
	}
}

OnClickCallbackManager = new ClickCallbackManager();