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

	this.activate = function(name) {
		if (!this.callbacks[name]) {
			throw new Error('Invalid callback name: ' + name);
		}
		this.active = this.callbacks[name];
	}

	this.deactivate = function() {
		this.active = null;
	}

	this.onClick = function(e) {
		if (self.active) {
			self.active.onClick(e, this);
		}
	}
}

OnClickCallbackManager = new ClickCallbackManager();

OperationsPanelComponent = function(x, y, z, atom, columns) {
	Crafty.sprite(2287, 265, "/public/images/game/dolen-element.png", {
		itemsPanel : [ 0, 0, 1, 1 ]
	});

	this.panel = Crafty.e("2D, Canvas, itemsPanel, Mouse").attr({
		x : x,
		y : y,
		z : z,
		w : 570,
		h : 64
	});

	var newPlant = new OperationComponent({
		name : 'plant',
		sprite : {
			url : "/public/images/game/sadnica.png",
			width : 70,
			height : 128
		}
	}, x + 30, y + 17, z + 10, 12, 23, atom, columns);

	this.panel.buttons = [];
	this.panel.buttons.push(newPlant.element);
}

OperationComponent = function(oper, x, y, z, w, h, atom, columns) {
	var sp = {};
	sp[oper.name] = [ 0, 0, 1, 1 ];

	Crafty.sprite(oper.sprite.width, oper.sprite.height, oper.sprite.url, sp);

	OnClickCallbackManager.registerCallback(oper.name, new OperationCallback(
			oper.name, atom, columns));

	this.element = Crafty.e("2D, Canvas, " + oper.name + ", Mouse").attr({
		x : x,
		y : y,
		z : z,
		w : w,
		h : h
	}).bind('Click', function(e) {
		OnClickCallbackManager.activate(oper.name);
	});
}

OperationCallback = function(name, atom, zLevles) {

	function createPlant(atom) {
		return Crafty.e("2D, Canvas, " + name).attr({
			w : atom / 2,
			h : atom
		});
	}

	this.onClick = function(e, landPart) {
		if (!landPart.plant) {
			var plant = createPlant(atom);
			plant.attr({
				x : landPart._x + atom / 4,
				y : landPart._y - (atom / 2 + atom / 8),
				z : landPart._z + zLevles
			});

			landPart.plant = plant;
		} else {
			console.log("duplicate plant is not allowed!")
		}

	};

}
